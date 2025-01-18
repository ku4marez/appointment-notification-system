pipeline {
    agent any

    environment {
        USER_NAME = credentials('USER_NAME')
        ACCESS_TOKEN = credentials('ACCESS_TOKEN')
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Fetch the latest code from GitHub
                checkout scm
            }
        }

        stage('Install Docker CLI') {
            steps {
                sh '''
                # Check if Docker is already installed
                if ! [ -x "$(command -v docker)" ]; then
                    echo "Installing Docker CLI..."
                    apt-get update
                    apt-get install -y docker.io
                else
                    echo "Docker CLI is already installed."
                fi

                # Verify Docker installation
                docker --version
                '''
            }
        }

        stage('Start Test Docker Containers') {
            agent {
                docker {
                    image 'eclipse-temurin:21-jdk'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh '''
                # Start a MongoDB container for testing
                docker compose -f appointment-notifications/src/test/resources/docker-compose.yml up -d
                '''
            }
        }

        stage('Build Application') {
            agent {
                docker {
                    image 'eclipse-temurin:21-jdk'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh '''
                # Build the application using Maven with a custom profile
                mvn -s $WORKSPACE/.github/workflows/settings.xml clean package -P common-libraries -DskipTests
                '''
            }
        }

        stage('Run Tests') {
            agent {
                docker {
                    image 'eclipse-temurin:21-jdk'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh '''
                # Run application tests using Maven
                mvn -s $WORKSPACE/.github/workflows/settings.xml test
                '''
            }
        }
    }

    post {
        always {
            sh '''
            echo "Cleaning up resources..."
            docker compose -f appointment-notifications/src/test/resources/docker-compose.yml down
            '''
        }
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
