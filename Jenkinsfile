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
			sh 'mkdir -p /home/jenkins/Tests'
			sh 'mv ./target/surefire-reports/*.txt /home/jenkins/Tests/${BUILD_NUMBER}_tests.txt'
			}
		}
		stage('Build War'){
			steps{
			sh 'mvn clean package'
			}
		}
		stage('Moving War'){
			steps{
			sh 'mkdir -p /home/jenkins/Wars'
			sh 'mkdir -p /home/jenkins/appservice'
			sh 'mv ./target/*.war /home/jenkins/Wars/project_war.war'
			sh ''' echo '#!/bin/bash
sudo java -Dserver.port=80 -jar /home/jenkins/Wars/project_war.war' > /home/jenkins/appservice/start.sh
sudo chmod +x /home/jenkins/appservice/start.sh'''
			}
                }
		stage('Stopping Service'){
			steps{
			sh 'bash stopservice.sh'
			}
		}
		stage('Create new service file'){
			steps{
			sh '''echo '[Unit]
Description=My SpringBoot App

[Service]
User=ubuntu
Type=simple

ExecStart=/home/jenkins/appservice/start.sh

[Install]
WantedBy=multi-user.target' > /home/jenkins/myApp.service'''
			sh'sudo mv /home/jenkins/myApp.service /etc/systemd/system/myApp.service'
			}
		}
		stage('Reload and start service'){
			steps{
			sh 'sudo systemctl daemon-reload'
			sh 'sudo systemctl start myApp.service'
			}
		}

	}
}
