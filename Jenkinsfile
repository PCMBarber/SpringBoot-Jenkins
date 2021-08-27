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
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@18.170.42.123 << EOF
			rm -rf SpringBoot-Jenkins
			git clone https://github.com/PCMBarber/SpringBoot-Jenkins.git
			cd SpringBoot-Jenkins
			rm -f ./src/main/resources/application-dev.properties
			echo 'spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2

spring.datasource.url=jdbc:mysql://natwestjenkdb.c7rrpdntrfy4.eu-west-2.rds.amazonaws.com/tdl
spring.datasource.data=classpath:data-dev.sql
spring.datasource.username=admin
spring.datasource.password=natWesty' > ./src/main/resources/application-dev.properties
			mvn clean package
			'''
			}
		}
		stage('Moving War'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@18.170.42.123	 << EOF
			cd SpringBoot-Jenkins
			mkdir -p /home/jenkins/Wars
			mv ./target/*.war /home/jenkins/Wars/project_war.war
			'''
			}
                }
		stage('Stopping Service'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@18.170.42.123 << EOF
			cd SpringBoot-Jenkins
			bash stopservice.sh
			'''
			}
		}
		stage('Create new service file'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@18.170.42.123 << EOF
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
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@18.170.42.123 << EOF
			sudo systemctl daemon-reload
			sudo systemctl restart myApp
			'''
			}
		}

	}
}
