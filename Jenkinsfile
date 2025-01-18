pipeline {
    agent {
        docker {
            image 'eclipse-temurin:21-jdk' // Use JDK 21 image
            args '-v /var/run/docker.sock:/var/run/docker.sock' // Mount Docker socket
        }
    }

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

        stage('Start Test Docker Containers') {
            steps {
                sh '''
                # Start a MongoDB container for testing
                docker compose -f appointment-notifications/src/test/resources/docker-compose.yml up -d
                '''
            }
        }

        stage('Build Application') {
            steps {
                sh '''
                # Build the application using Maven with a custom profile
                mvn -s $WORKSPACE/.github/workflows/settings.xml clean package -P common-libraries -DskipTests
                '''
            }
        }

        stage('Run Tests') {
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
            echo "Cleaning up resources..."
            // Stop and remove the MongoDB container
            sh '''
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
