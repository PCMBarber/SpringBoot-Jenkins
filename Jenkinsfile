if (${GIT_BRANCH} == 'origin/main') {
	ip = "34.142.5.178"
} else if (${GIT_BRANCH} == 'origin/development') {
	ip = "34.142.37.63"
}

pipeline {
	agent any
	environment {
		appIP="${ip}"
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
			echo 'Piers was ere'
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
			docker build -t stratcastor/springdemo:latest .
			'''
			}
        }
		stage('Push Docker Image'){
			steps{
			sh '''
			docker push stratcastor/springdemo:latest
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
			docker run -d -p 8080:8080 --name javabuild stratcastor/springdemo:latest
			'''
			}
		}

	}
}
