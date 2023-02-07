pipeline {
	agent any
	environment {
		appIP="35.226.220.179";
		containerName="java";
		imageName="java";
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
		stage('Build Application'){
			steps{
			sh 'mvn clean package'
			}
		}
		stage('Docker Build'){
			steps{
			sh '''
			docker build -t ksbhull/$imageName:latest -t ksbhull/$imageName:build-$BUILD_NUMBER .
			'''
			}
		}
		stage('Push Images'){
			steps{
			sh '''
			docker push ksbhull/$imageName:latest
			docker push ksbhull/$imageName:build-$BUILD_NUMBER
			'''
			}
                }
		stage('Restart Container'){
			steps{
			sh '''
			ssh -i "~/.ssh/id_rsa" jenkins@$appIP << EOF
				if [ ! "$(docker ps -a -q -f name=$containerName)" ]; then
    				if [ "$(docker ps -aq -f status=exited -f name=$containerName)" ]; then
        				docker rm -f $containerName
						docker rmi $imageName
    				fi
    				# run your container
    				docker run -d -p 8080:8080 --name $containerName $imageName
				fi
			'''
			}
		}
		stage('Clean Up'){
			steps{
			sh '''
			docker system prune -f
			'''
			}
		}
	}
}
