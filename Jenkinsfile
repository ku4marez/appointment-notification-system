pipeline {
    agent none // Use the Jenkins container as the agent
    environment {
        USER_NAME = credentials('USER_NAME')
        ACCESS_TOKEN = credentials('ACCESS_TOKEN')
    }

    stages {
        stage('Install JDK 21') {
            agent { label 'master' } // Run directly on the Jenkins container
            steps {
                sh '''
                # Check if JDK 21 is already installed
                if ! java -version 2>&1 | grep "21" > /dev/null; then
                    echo "Installing JDK 21..."
                    wget -q https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21%2B35/OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz -O /tmp/jdk21.tar.gz
                    mkdir -p /usr/lib/jvm
                    tar -xzf /tmp/jdk21.tar.gz -C /usr/lib/jvm --strip-components=1
                    update-alternatives --install /usr/bin/java java /usr/lib/jvm/bin/java 1
                    update-alternatives --set java /usr/lib/jvm/bin/java
                else
                    echo "JDK 21 already installed."
                fi
                java -version
                '''
            }
        }

        stage('Verify Docker') {
            agent { label 'master' } // Run directly on the Jenkins container
            steps {
                sh '''
                # Verify Docker installation
                docker --version
                docker ps
                '''
            }
        }

        stage('Checkout Code') {
            agent { label 'master' } // Run directly on the Jenkins container
            steps {
                checkout scm
            }
        }

        stage('Start Test Docker Containers') {
            agent { label 'master' } // Run directly on the Jenkins container
            steps {
                sh '''
                # Start the required services using Docker Compose
                docker compose -f appointment-notifications/src/test/resources/docker-compose.yml up -d
                '''
            }
        }

        stage('Build Application') {
            agent { label 'master' } // Run directly on the Jenkins container
            steps {
                sh '''
                # Build the application using Maven
                mvn -s $WORKSPACE/.github/workflows/settings.xml clean package -P common-libraries -DskipTests
                '''
            }
        }

        stage('Run Tests') {
            agent { label 'master' } // Run directly on the Jenkins container
            steps {
                sh '''
                # Run tests using Maven
                mvn -s $WORKSPACE/.github/workflows/settings.xml test
                '''
            }
        }
    }

    post {
        always {
            sh '''
            echo "Cleaning up Docker resources..."
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
