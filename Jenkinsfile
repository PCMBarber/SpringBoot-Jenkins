pipeline {
	agent any
	environment {
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
		stage('War Build'){
			steps{
			sh '''
			mvn clean package
			'''
			}
		}
		stage('Moving War'){
			steps{
			sh '''
			mkdir -p ./wars
			mv ./target/*.war ./wars/project_war.war
			'''
			}
        }
		stage('Build Docker Image'){
			steps{
			sh '''
			docker build -t rubinderg/springdemo:latest .
			'''
			}
        }
		stage('Push Docker Image'){
			steps{
			sh '''
			docker push -t rubinderg/springdemo:latest
			docker push rubinderg/springdemo:latest
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
		stage('Restart App'){
			steps{
			sh '''ssh -i "~/.ssh/id_rsa" jenkins@$appIP << EOF
			docker run -d -p 80:8080 --name javabuild rubinderg/springdemo:latest
			'''
			}
		}
	}
}