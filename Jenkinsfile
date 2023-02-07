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
			sh '''
			if (${GIT_BRANCH} == 'origin/main') then
				ssh -i "~/.ssh/id_rsa" jenkins@34.142.5.178 << EOF
				docker rm -f javabuild
			fi 
			if (${GIT_BRANCH} == 'origin/development') then
				ssh -i "~/.ssh/id_rsa" jenkins@34.142.37.63 << EOF
				docker rm -f javabuild
			fi
			'''
			}
		}
		stage('Restart App'){
			steps{
			sh '''
			if (${GIT_BRANCH} == 'origin/main') then
				ssh -i "~/.ssh/id_rsa" jenkins@34.142.5.178 << EOF
				docker run -d -p 8080:8080 --name javabuild stratcastor/springdemo:latest
			fi
			if (${GIT_BRANCH} == 'origin/development') then
				ssh -i "~/.ssh/id_rsa" jenkins@34.142.37.63 << EOF
				docker run -d -p 8080:8080 --name javabuild stratcastor/springdemo:latest
			fi
			'''
			}
		}

	}
}
