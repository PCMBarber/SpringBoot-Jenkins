pipeline {
	agent any
	environment {
		appIP="";
		gitRepo="";
		repoName="";
		appIP="35.228.153.41";
	}
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
		stage('War Build'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@$appIP << EOF
			rm -rf $repoName
			git clone $gitRepo
			cd $repoName
			rm -f ./src/main/resources/application-dev.properties
			sh '''
			mvn clean package
			'''
			}
		}
		stage('Moving War'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@$appIP	 << EOF
			cd $repoName
			sh '''
			mkdir -p /home/jenkins/Wars
			mv ./target/*.war /home/jenkins/Wars/project_war.war
			'''
			}
                }
		stage('Stopping Service'){
        }
		stage('Build Docker Image'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@$appIP << EOF
			cd $repoName
			bash stopservice.sh
			sh '''
			docker build -t rubindergr/springdemo:latest .
			'''
			}
		}
		stage('Create new service file'){
        }
		stage('Push Docker Image'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@$appIP << EOF
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
			sh '''
			docker push -t rubinderg/springdemo:latest
			'''
			}
        }
		stage('Stopping Container'){
			steps{
			sh '''ssh -i "~/.ssh/id_rsa" jenkins@$appIP << EOF
			docker rm -f javabuild
			'''
			}
		}
		stage('Reload and restart service'){
		stage('Restart App'){
			steps{
			sh '''ssh -i "~/.ssh/jenkins_key" jenkins@$appIP << EOF
			sudo systemctl daemon-reload
			sudo systemctl restart myApp
			sh '''ssh -i "~/.ssh/id_rsa" jenkins@$appIP << EOF
			docker run -d -p 80:8080 --name javabuild rubinderg/springdemo:latest
			'''
			}
		}