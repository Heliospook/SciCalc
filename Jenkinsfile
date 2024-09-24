pipeline {
    agent any

    environment {
        // Set your credentials for the private repository
        GIT_CREDENTIALS_ID = 'githubCredsSSH'
        DOCKER_IMAGE = 'subhajeetlahiri/scicalc:latest'
        ANSIBLE_INVENTORY = 'inventory.ini' // Path to your Ansible inventory file
        ANSIBLE_PLAYBOOK = 'deploy_scicalc.yml'
        CONTAINER_NAME = 'scicalc_cont'
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

        stage('7. Deploy with Ansible') {
            steps {
                script {
                    sh "ansible-playbook -i ${ANSIBLE_INVENTORY} ${ANSIBLE_PLAYBOOK}"
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
