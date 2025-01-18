pipeline {
    agent any

    environment {
        USER_NAME = credentials('USER_NAME')
        ACCESS_TOKEN = credentials('ACCESS_TOKEN')
    }

    stages {
        stage('Build Custom Docker Image') {
            steps {
                script {
                    // Build the custom Docker image
                    sh '''
                    docker build -t jenkins-custom:latest - <<EOF
                    FROM eclipse-temurin:21-jdk

                    # Install Docker CLI
                    USER root
                    RUN apt-get update && apt-get install -y docker.io curl && apt-get clean

                    # Switch back to non-root user if needed
                    USER jenkins
                    EOF
                    '''
                }
            }
        }

        stage('Start Test Docker Containers') {
            steps {
                script {
                    // Use the custom image and start containers
                    sh '''
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock jenkins-custom:latest sh -c "
                    docker compose -f appointment-notifications/src/test/resources/docker-compose.yml up -d
                    "
                    '''
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    // Build the application
                    sh '''
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock jenkins-custom:latest sh -c "
                    mvn -s $WORKSPACE/.github/workflows/settings.xml clean package -P common-libraries -DskipTests
                    "
                    '''
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run tests
                    sh '''
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock jenkins-custom:latest sh -c "
                    mvn -s $WORKSPACE/.github/workflows/settings.xml test
                    "
                    '''
                }
            }
        }
    }

    post {
        always {
            script {
                // Tear down Docker Compose
                sh '''
                docker run --rm -v /var/run/docker.sock:/var/run/docker.sock jenkins-custom:latest sh -c "
                docker compose -f appointment-notifications/src/test/resources/docker-compose.yml down
                "
                '''
            }
        }
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
