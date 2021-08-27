# SpringBoot-Jenkins

Create 2 Ubuntu 20.04 instances on aws with a public IP, in the same VPC

Name one jenkins and one app

Open ports 22, 8080 on both with the security group

Create a mySql database with public access OFF in the same VPC

ADDITIONAL CONFIGURATION on creation of the mysql instance, create an initial database named tdl

--------------------------------------------------------------------------------

SSH on to app and add a new user called jenkins:

```
sudo useradd -m jenkins
```

Switch into that user

```
sudo su jenkins
cd ~
```

If a .ssh folder doesn't exist, create one, then a new file inside called authorized_keys

```
mkdir ~/.ssh
nano ~/.ssh/authorized_keys
```

Leave this terminal running.

--------------------------------------------------------------------------------

SSH on to jenkins in a new console

save the following in a .sh file:

```
#!/bin/bash
if type apt > /dev/null; then
    pkg_mgr=apt
    java="openjdk-11-jre"
elif type yum /dev/null; then
    pkg_mgr=yum
    java="java"
fi
echo "updating and installing dependencies"
sudo ${pkg_mgr} update
sudo ${pkg_mgr} install -y ${java} wget git > /dev/null
echo "configuring jenkins user"
sudo useradd -m -s /bin/bash jenkins
echo "downloading latest jenkins WAR"
sudo su - jenkins -c "curl -L https://updates.jenkins-ci.org/latest/jenkins.war --output jenkins.war"
echo "setting up jenkins service"
sudo tee /etc/systemd/system/jenkins.service << EOF > /dev/null
[Unit]
Description=Jenkins Server
[Service]
User=jenkins
WorkingDirectory=/home/jenkins
ExecStart=/usr/bin/java -jar /home/jenkins/jenkins.war
[Install]
WantedBy=multi-user.target
EOF
sudo systemctl daemon-reload
sudo systemctl enable jenkins
sudo systemctl restart jenkins
sudo su - jenkins << EOF
until [ -f .jenkins/secrets/initialAdminPassword ]; do
    sleep 1
    echo "waiting for initial admin password"
done
until [[ -n "\$(cat  .jenkins/secrets/initialAdminPassword)" ]]; do
    sleep 1
    echo "waiting for initial admin password"
done
echo "initial admin password: \$(cat .jenkins/secrets/initialAdminPassword)"
EOF
```	

Give yourself executable permission

```
sudo chmod +x
```

Run the script

```
./<script_name_here>.sh
```

Wait for the initialAdminPassword to output

--------------------------------------------------------------------------------

Go to `<jenkins-instance-IP>:8080`

Input adminpassword from script output

Install suggested plugins

Create an account to log in to jenkins with

--------------------------------------------------------------------------------

Go back to your console ssh(ed) into jenkins

Switch user into the newly created jenkins

```
sudo su jenkins
cd ~
```

Create a new ssh key in the location: ~/.ssh/jenkins_key

Leave the password blank

```
ssh-keygen
```

Print the public half of the key to the console:

```
cat ~/.ssh/jenkins_key.pub
```

Copy the public key that prints out

--------------------------------------------------------------------------------

Go back to the console ssh(ed) into app

It should still be using nano to view the authorized_keys file

Paste the public key from jenkins into the authorized_keys file

Save the file and exit

--------------------------------------------------------------------------------

Go back to the console ssh(ed) into jenkins

Try to ssh over to the app instance 

ssh -i "/home/jenkins/.ssh/jenkins_key" jenkins@<your_instance_IP>

Type yes to add the host to the list

Now Jenkins can ssh to our other instance

--------------------------------------------------------------------------------

Go back to `<jenkins-instance-IP>:8080`

Create a new build

Select pipeline build

Set up the pipeline to function using a fork of this repository https://github.com/PCMBarber/SpringBoot-Jenkins

Ensure your pipeline:

```
Is expecting a Github Webhook trigger (GitHub hook trigger for GITScm polling)
Is set up to pull the Jenkinsfile from SCM and is pointing at the 'main' branch (defaults to master)
```

Set up a github webHook on your forked repo to function with your main branch of the repository

Save the pipeline

--------------------------------------------------------------------------------

Clone the fork down to your local machine

Change the Jenkinsfile to:

```
Use your IP for the app instance to ssh to the other machine
Use your repository fork to clone
Use your database endpoint to generate the application-dev.properties file
```

Push the changes up to github


Note: This is secure because our database is not open to public accessiblility,
and our SSH keys used during the pipeline only exist on the instances, the key names used to connect to the instances remain secure.


--------------------------------------------------------------------------------

Go back to `<jenkins-instance-IP>:8080`

Your build should be triggered by the GitHub Webhook

You can click on the boxes to see the output of the stages

When it finishes the app will be on port 8080 on the app instances public IP
	
You can make changes to the Jenkinsfile to change the pipeline
