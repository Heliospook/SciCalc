# Software Production Engineering 
### Mini Project
### **Subhajeet Lahiri, IMT2021022**

Link to the GitHub repo : https://github.com/Heliospook/SciCalc

---
### Introduction

This report walks us through the process of developing a RESTful Java application called **SciCalc** while adopting a **Test-Driven Development (TDD)**. The project utilizes a modern software development workflow incorporating tools such as Git for source control, Jenkins for continuous integration and deployment, Docker for containerization, and Ansible for orchestration. By embracing TDD, we ensure that our application meets functional requirements and maintains high code quality throughout its development lifecycle. This document details each step taken, from setting up the development environment and managing the code repository to configuring automated testing and deployment pipelines. The aim is to create a robust application that is easily maintainable and deployable in various environments.

---
### Outline
Here is a 10000 ft overview of the steps that we are going to take to set up our workflow and develop our application:
1. **Creation of a Java project in our system**
    - **Installation Instructions:** Detailed steps for setting up IntelliJ IDEA, Java, and Maven.
2. **Source Control Management**
    - **Installation Instructions:** How to install Git and set up GitHub.
    - **Creating a remote repository:** Steps for initializing the repository and pushing the initial code.
3. **Adding Controllers and Tests**
    - **Test-Driven Development Approach:** Creating dummy controllers and setting up unit tests using JUnit5 and MockMvc.
4. **Setting up a Jenkins pipeline**
    - **Jenkins Installation:** Installing Jenkins and setting up GitHub authentication.
    - **Creating the pipeline:** Writing the pipeline script to automate testing and deployment.
5. **Finishing up with the code**
    - **Implementing functionality:** Adding logic to the controllers and verifying successful test executions.
6. **Dockerizing our application**
    - **Installation Instructions:** Steps for installing Docker.
    - **Creating a Dockerfile:** Building the Docker image and running the container.
7. **Deployment using Ansible**
    - **Ansible Installation:** Steps to install Ansible.
    - **Creating inventory and playbook:** Configuring the playbook to pull the Docker image and run the container.
8. **Trying out the endpoints over public IP**
    - **Testing the application:** Accessing the application endpoints from different devices using Ngrok.

> [!NOTE]
> To keep the flow consistent, the installation instructions have been added in the relevant sections.

---
### System specifications

All the processes in the workflow outlined in the report run on my laptop running `Ubuntu 24.04 LTS`.

![[Pasted image 20240924073650.png]]

---
## 1. Setting up the development environment

Since we are would be building a Java application, it is a good idea to use `IntelliJ IDEA` as an IDE.
### Installation
The community edition can be installed from the snap store using the following command :
```sh
sudo snap install intellij-idea-community --classic
```

![[Pasted image 20240924075839.png]]


We also need to have Java installed on our system. We can install `OpenJDK JRE` using the following command :
```sh
sudo apt install default-jre -y
```

**![[Pasted image 20240924075441.png]]**

We will also install `Maven` - our build tool.
```sh
sudo apt install maven -y
```
### Project Stub
Since we plan to build a RESTful application, it is a good idea to head over to `start.spring.io` and obtain a good starting point for our spring application.
The following values seem to be appropriate for the fields for the time being :

![[Pasted image 20240924075742.png]]

We download the generated zip file and extract it to the `SciCalc` folder on our system.

---
## 2. Source Control Management

We shall be using `git` for SCM and acquire a remote repository from `GitHub`.
### Installation

Installation of Git on Ubuntu can be done using a single command :
```sh
sudo apt install git-all
```

The more cumbersome part is setting up GitHub.
We first install `github-cli` using the command mentioned [here](https://github.com/cli/cli/blob/trunk/docs/install_linux.md). The command is pasted here for reference :

```sh
(type -p wget >/dev/null || (sudo apt update && sudo apt-get install wget -y)) \
	&& sudo mkdir -p -m 755 /etc/apt/keyrings \
	&& wget -qO- https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo tee /etc/apt/keyrings/githubcli-archive-keyring.gpg > /dev/null \
	&& sudo chmod go+r /etc/apt/keyrings/githubcli-archive-keyring.gpg \
	&& echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null \
	&& sudo apt update \
	&& sudo apt install gh -y
```

We then follow the on-screen prompts which come up after we run :
```sh
gh auth login
```

### Creating a remote repository

We first create a private repository on GitHub using the web interface:

![[Pasted image 20240924082446.png]]

We are now set-up to push the initial commit to GitHub. We execute the following commands inside `~/Desktop/SciCalc` :

```sh
git init
git remote add origin https://github.com/Heliospook/SciCalc.git
git add .
git commit -m "Initial commit with spring stub"
git push origin main
```

---
## 3. Adding Controllers and Tests

Since we are taking the Test-Driven Development approach, we will set up the interface and add tests to our application before writing the logic.

We first add a controller with the routes in place. For now, they can contain dummy logic :
```java
@RestController  
public class CalcController {  
    @GetMapping("/root")  
    public String squareRoot(@RequestParam float x) {  
        float result = x; // placeholder logic  
        return "" + result;  
    }  
  
    @GetMapping("/factorial")  
    public String factorial(@RequestParam int x) {  
        int result = x; // placeholder logic  
        return "" + result;  
    }  
  
    @GetMapping("/ln")  
    public String naturalLog(@RequestParam float x) {  
        float result = x; // placeholder logic  
        return "" + result;  
    }  
  
    @GetMapping("/power")  
    public String exponentiation(@RequestParam float x, @RequestParam float b) { 
        float result = x * b; // placeholder logic  
        return "" + result;  
    }  
}
```

We then add `Junit5` as a dependency and use it to write the tests for our application.
```xml
<dependency>  
    <groupId>org.junit.jupiter</groupId>  
    <artifactId>junit-jupiter-params</artifactId>  
    <version>5.7.1</version>  
    <scope>test</scope>  
</dependency>
```

While doing so, we also use `MockMvc` provided by the `Spring Test` package to mock a controller and perform requests on it. A sample test is provided below :

```java
@SpringBootTest  
@AutoConfigureMockMvc  
public class CalcControllerTests {  
    @Autowired  
    private MockMvc mockMvc;  
  
    @ParameterizedTest  
    @CsvSource({  
            "48.56, 6.96",  
            "9.0, 3.00",  
            "0.0, 0.00"  
    })  
    public void testPositiveRoot(float input, String expectedOutput) throws Exception {  
        mockMvc.perform(get("/root")  
                        .param("x", String.valueOf(input)))  
                .andExpect(status().isOk())  
                .andExpect(content().string(expectedOutput));  
    }
}
```

We add tests for all our controllers in a similar fashion and push them to GitHub. We can see that none of the tests pass currently :
![[Pasted image 20240924094831.png]]

---
## 4. Setting up a Jenkins pipeline

### Jenkins installation
We already installed Java,  a necessary prerequisite for `Jenkins`.
We can install `Jenkins` on Debian-based distros using following set of commands available in the Jenkins documentation :
```sh
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update
sudo apt-get install jenkins
```

We can then follow the steps detailed by the Post Installation Set-up wizard, accessed by visiting `https://localhost:8080`.
### GitHub authentication

The first and foremost step is to set-up SSH keys so that Jenkins can pull code from a private GitHub repository.
To do so, we follow the steps below :
1.  Generate a public/private key-pair using `ssh-keygen` : ```
```sh
ssh-keygen -t ed25519
```
The public and private keys will be saved in separate files

2. Add the public key to your GitHub account in the `Settings/SSH and GPG keys` section.
3. Copy the private key to `/var/lib/jenkins/.ssh` and make the `jenkins` user the owner of the file.
```sh
chown -R jenkins:jenkins /var/lib/jenkins/.ssh
```
4. Switch to the `jenkins` user and try connecting to `git@github.com`
```sh
sudo su jenkins
ssh -T git@github.com
```

We will get a similar output if our connection is successful :
![[Pasted image 20240924112359.png]]


> [!NOTE]
> This step is important as it adds github.com to the known hosts file for the jenkins user. If not done, we will face errors while pulling from GitHub, even with the correct credentials set.

5. Add the private key as a global credential in Jenkins.
6.
![[Pasted image 20240924113651.png]]
We can now use the same SSH creds when we create pipelines.

### Setting up our pipeline

We create a new `Pipeline` item and provide the following pipeline script :
```json
pipeline {
    agent any

    environment {
        // Set your credentials for the private repository
        GIT_CREDENTIALS_ID = 'githubCredsSSH'
    }

    stages {
        stage('1. Pull code from SciCalc repo') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM', 
                        branches: [[name: '*/main']], 
                        userRemoteConfigs: [[
                            url: 'git@github.com:Heliospook/SciCalc.git',
                            credentialsId: env.GIT_CREDENTIALS_ID
                        ]]
                    ])
                }
            }
        }

        stage('2. Run Tests') {
            steps {
                script {
                    // Running the tests using Maven
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        success {
            echo 'Tests were successful!'
        }
        failure {
            echo 'Tests failed.'
        }
    }
}
```

This script just pulls the code and runs tests. We will be modifying the same later.
Upon manually running the pipeline, we find that we are able to successfully pull code but as expected, the tests fail.
![[Pasted image 20240924124756.png]]
### Webhook trigger

We will add the following trigger to our pipeline script :
```json
triggers{
	githubPush()
}
```

We will also add the GitHub project URL and check the option `GitHub hook trigger for GITScm polling`.

We also need to set up `ngrok` to make our localhost accessible on a public IP. We can do so using the `snap` store.
```sh
snap install ngrok
ngrok config add-authoken <token>
```

We can then add the following config to `/home/subhajeet-lahiri/.config/ngrok/ngrok.yml`

![[Pasted image 20240924144355.png]]
Running `ngrok start --all` spins up two tunnels :
- We are using the free static domain provided by ngrok for Jenkins.
- The dynamic IP can be used to expose our web application.

We now add a web hook trigger to our GitHub repository :

![[Pasted image 20240924144833.png]]

---
## 5. Finishing up with the code

We now add the relevant implementations in `CalcController.java` and push the changes to GitHub. This triggers a build and we see that the test cases passed.
![[Pasted image 20240924132100.png]]
![[Pasted image 20240924132131.png]]
![[Pasted image 20240924132155.png]]

---
## 6. Dockerizing our application

### Installation

We follow the steps detailed in the documentation provided by Docker. It boils down to running the following commands :
```sh
# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

### Dockerfile

We set our application to use port `3000` instead of the default `8080` as the latter is used by `Jenkins`

```application.properties
server.port = 3000
```

We then add the following Dockerfile :

```Dockerfile
FROM amazoncorretto:21  
WORKDIR /app  
COPY target/SciCalc-1.0.jar /app/SciCalc.jar  
EXPOSE 3000  
ENTRYPOINT ["java", "-jar", "SciCalc.jar"]
```

We can then build a Docker image and then run our docker container using :
```sh
docker build -t scicalc .
docker run -p 3000:3000 -d scicalc
```

As is evident, we have named our image `scicalc` and mapped the host port `3000` to the same port on the container.
We can see that the container boots up and we can try out operations.

![[Pasted image 20240924135223.png]]

Power :
![[Pasted image 20240924135422.png]]

Natural logarithm
![[Pasted image 20240924135501.png]]
and so on...

### Adding the Dockerization step in Jenkins

We now add to our pipeline script so that we build the Docker image using the added Dockerfile and then push the image to DockerHub. The modified pipeline is as follows :

```json
pipeline {
    agent any

    environment {
        // Set your credentials for the private repository
        GIT_CREDENTIALS_ID = 'githubCredsSSH'
        DOCKER_IMAGE = 'subhajeetlahiri/scicalc:latest'
    }
    
    triggers {
        githubPush()
    }

    stages {
        stage('1. Pull code from SciCalc repo') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM', 
                        branches: [[name: '*/main']], 
                        userRemoteConfigs: [[
                            url: 'git@github.com:Heliospook/SciCalc.git',
                            credentialsId: env.GIT_CREDENTIALS_ID
                        ]]
                    ])
                }
            }
        }

        stage('2. Run Tests') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }
        
        stage('3. Run Maven build') {
            steps {
                script {
                    sh 'mvn clean install'
                }
            }
        }
        
        stage('4. Build Docker image') {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}")
                }
            }
        }
        
        stage('5. Push to Docker hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerHubCreds'){
                        dockerImage.push()
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Great success!'
        }
        failure {
            echo 'It smells of failure'
        }
    }
}
```

We also need to do the following things :
- Install the `Docker Pipeline` plugin
- Add the `jenkins` user to the `docker` user group :
```sh
sudo usermod -aG jenkins docker
```
- Restart both Jenkins and Docker :
```sh
sudo systemctl restart jenkins
sudo systemctl restart docker
```

We can now push the changes with the Dockerfile to Github and we shall see that all the stages execute successfully.

![[Pasted image 20240924145854.png]]


---
## 7. Deployment using Ansible

### Ansible Installation

We can install `ansible` using `pipx`. To do so, we execute :
```sh
sudo apt install pipx
pipx install ansible
```
``
### Adding inventory and playbook

We add the `inventory.ini` file to our repo and configure it to use the localhost :
```inventory.ini
[local]  
localhost ansible_connection=local
```

The playbook is configured to pull the Docker image from Docker hub and then run it on our system. Simple shell commands suffice for this task.

```deploy_scicalc.yml
---  
- name: Deploy SciCalc Docker Image  
  hosts: local  
  tasks:  
    - name: Pull the SciCalc Docker image  
      shell : docker pull subhajeetlahiri/scicalc:latest  
  
    - name: Run the SciCalc Docker container  
      shell : docker run -d --init -p 3000:3000 --name scicalc_cont subhajeetlahiri/scicalc:latest
```

If we run the playbook using the following command, we can see that our Docker container is up and running.

```sh
ansible-playbook -i inventory.ini deploy_scicalc.yml
```

![[Pasted image 20240924153047.png]]

### Augmenting the pipeline

We add two more stages to the Jenkins pipeline :
1. If we try building another container with the same name while the one from the previous build is running, we will encounter an error. To circumvent this, we first check if such a container exists and if so, remove it.

```json
stage('6. Check and Delete Existing Container') {
	steps {
		script {
			def containerExists = sh(
				script: "docker ps -a --filter 'name=${CONTAINER_NAME}' --format '{{.Names}}' | grep -w ${CONTAINER_NAME} || true",
				returnStatus: true
			)

			if (containerExists == 0) {
				echo "Container ${CONTAINER_NAME} exists. Deleting..."
				sh "docker rm -f ${CONTAINER_NAME}"
			} else {
				echo "Container ${CONTAINER_NAME} does not exist."
			}
		}
	}
}
```

2. We add a stage to run the ansible playbook.
```json
stage('7. Deploy with Ansible') {
	steps {
		script {
			sh "ansible-playbook -i ${ANSIBLE_INVENTORY} ${ANSIBLE_PLAYBOOK}"
		}
	}
}
```

This rounds out our workflow. We see that if we push changes, we trigger a build and all stages complete successfully.

![[Pasted image 20240924155335.png]]

![[Pasted image 20240924155431.png]]

We can see that our container is running :

![[Pasted image 20240924155458.png]]

## 8. Trying out the endpoints over public IP

Since we have also exposed our calculator application running on port `3000` using `ngrok`, we can access its routes from other devices over the internet.
After we ran, `ngrok start --all`, this would have been displayed :

![[Pasted image 20240924160412.png]]

If we visit the address corresponding to `http://localhost:3000` and add required parameters, we can access the result on our phones.
E.g. : https://66f4-119-161-98-68.ngrok-free.app/root?x=5 gives :
![[Pasted image 20240924160857.png]]

