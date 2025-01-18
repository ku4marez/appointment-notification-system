pipeline {
    agent any

    environment {
        USER_NAME = credentials('USER_NAME')
        ACCESS_TOKEN = credentials('ACCESS_TOKEN')
    }

    stages {
        stage('Install Docker') {
            agent {
                label 'linux'
            }
            steps {
                sh '''
                # Update package lists and install prerequisites
                apt-get update
                apt-get install -y \
                    ca-certificates \
                    curl \
                    gnupg \
                    lsb-release

                # Add Docker's GPG key and set up the repository
                curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
                echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list

                # Install Docker
                apt-get update
                apt-get install -y docker-ce docker-ce-cli containerd.io

                # Add Jenkins user to the Docker group
                usermod -aG docker jenkins

                # Restart Docker service
                service docker restart
                '''
            }
        }

        stage('Test Docker Installation') {
            agent {
                label 'linux'
            }
            steps {
                sh 'docker --version'
            }
        }

        stage('Pipeline with Docker Agent') {
            agent {
                docker {
                    image 'eclipse-temurin:21-jdk'
                    args '-u root:root'
                }
            }
            stages {
                stage('Check Branch') {
                    when {
                        expression {
                            return env.GIT_BRANCH ==~ /(origin\/master|origin\/dev)/
                        }
                    }
                    steps {
                        echo "Branch is ${env.GIT_BRANCH}. Proceeding with the build."
                    }
                }

                stage('Checkout') {
                    steps {
                        checkout scm
                    }
                }

                stage('Install Docker Compose Plugin') {
                    steps {
                        sh '''
                        mkdir -p ~/.docker/cli-plugins/
                        curl -fsSL "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o ~/.docker/cli-plugins/docker-compose
                        chmod +x ~/.docker/cli-plugins/docker-compose
                        docker compose version
                        '''
                    }
                }

                stage('Verify Docker Setup') {
                    steps {
                        sh 'docker info'
                    }
                }

                stage('Adjust System Settings') {
                    steps {
                        sh '''
                        sysctl -w vm.max_map_count=1677720
                        echo "Skipped adjusting swappiness as it is not supported on this runner."
                        '''
                    }
                }

                stage('Start MongoDB with Docker Compose') {
                    steps {
                        dir('appointment-notifications/src/test/resources') {
                            sh 'docker compose up -d'
                        }
                    }
                }

                stage('Build and Package Application') {
                    steps {
                        dir('appointment-notifications') {
                            sh '''
                            mvn -s $WORKSPACE/.github/workflows/settings.xml clean package -DskipTests
                            '''
                        }
                    }
                }

                stage('Run Tests') {
                    steps {
                        dir('appointment-notifications') {
                            sh '''
                            mvn -s $WORKSPACE/.github/workflows/settings.xml test
                            '''
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Cleaning up resources..."
            dir('appointment-notifications/src/test/resources') {
                sh 'docker compose down'
            }
        }
        success {
            echo "Build and tests executed successfully!"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
