pipeline {
    agent any

    environment {
        USER_NAME = credentials('USER_NAME')
        ACCESS_TOKEN = credentials('ACCESS_TOKEN')
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

        stage('Set up JDK 21') {
            steps {
                sh '''
                apt update
                apt install -y openjdk-21-jdk
                java -version
                '''
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
