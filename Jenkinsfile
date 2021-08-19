pipeline {
	agent any
	stages{
		stage('Build Jar'){
			steps{
			sh 'mvn clean package'
			}
		}
		stage('Moving Jar'){
			steps{
			sh 'mkdir -p /home/jenkins/Wars'
			sh 'mkdir -p /home/jenkins/appservice'
			sh 'mv ./target/*.jar /home/jenkins/Wars/project_war.war'
			sh ''' echo '#!/bin/bash
sudo java -jar /home/jenkins/Wars/project_war.war' > /home/jenkins/appservice/start.sh
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
