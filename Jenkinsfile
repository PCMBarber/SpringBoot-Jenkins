pipeline {
	agent any
	stages{
		stage('Test Application'){
			steps{
			sh 'mvn clean test'
			}
		}
		stage('Save Tests'){
			steps{
			sh 'mkdir -p /home/jenkins/Tests/${BUILD_NUMBER}_tests/'
			sh 'mv ./target/surefire-reports/*.txt /home/jenkins/Tests/${BUILD_NUMBER}_tests/'
			}
		}
		stage('SSH Build Deploy'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@<***your_instance_IP***> << EOF
			rm -rf <your_repository_name>
			git clone https://github.com/<your_user_name>/<your_repository_name>.git
			cd <your_repository_name>
			rm -f ./src/main/resources/application-dev.properties
			echo 'spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2

spring.datasource.url=jdbc:mysql://<***your_database_endpoint_here***>/tdl
spring.datasource.data=classpath:data-dev.sql
spring.datasource.username=<***your_username_here***>
spring.datasource.password=<***your_password_here***>' > ./src/main/resources/application-dev.properties
			mvn clean package
			EOF'''
			}
		}
		stage('Moving War'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@<your_instance_IP> << EOF
			cd <your_repository_name>
			mkdir -p /home/jenkins/Wars
			mv ./target/*.war /home/jenkins/Wars/project_war.war
			EOF'''
			}
                }
		stage('Stopping Service'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@<your_instance_IP> << EOF
			cd <your_repository_name>
			bash stopservice.sh
			EOF'''
			}
		}
		stage('Create new service file'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@<your_instance_IP> << EOF
			mkdir -p /home/jenkins/appservice
			echo '#!/bin/bash
sudo java -jar /home/jenkins/Wars/project_war.war' > /home/jenkins/appservice/start.sh
sudo chmod +x /home/jenkins/appservice/start.sh
echo '[Unit]
Description=My SpringBoot App

[Service]
User=ubuntu
Type=simple

ExecStart=/home/jenkins/appservice/start.sh

[Install]
WantedBy=multi-user.target' > /home/jenkins/myApp.service
sudo mv /home/jenkins/myApp.service /etc/systemd/system/myApp.service
			EOF'''
			}
		}
		stage('Reload and restart service'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@<your_instance_IP> << EOF
			sudo systemctl daemon-reload
			sudo systemctl restart myApp
			EOF'''
			}
		}

	}
}
