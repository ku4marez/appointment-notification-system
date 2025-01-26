pipeline {
    agent any

    environment {
        USER_NAME = credentials('USER_NAME')
        ACCESS_TOKEN = credentials('ACCESS_TOKEN')
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Setup Maven Configuration') {
            steps {
                sh '''
                mkdir -p ~/.m2
                cp $WORKSPACE/.m2/settings.xml ~/.m2/settings.xml
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

        stage('Install JDK 21') {
            steps {
                sh '''
                if ! java -version 2>&1 | grep "21" > /dev/null; then
                    apt-get install -y wget tar
                    wget -q https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21%2B35/OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz -O /tmp/jdk21.tar.gz
                    mkdir -p /usr/lib/jvm
                    tar -xzf /tmp/jdk21.tar.gz -C /usr/lib/jvm --strip-components=1
                    update-alternatives --install /usr/bin/java java /usr/lib/jvm/bin/java 1
                    update-alternatives --set java /usr/lib/jvm/bin/java
                fi
                java -version
                '''
            }
        }

        stage('Start Test Docker Containers') {
            steps {
                sh '''
                docker compose -f $WORKSPACE/src/test/resources/docker-compose.yml up -d
                '''
            }
        }

        stage('Build Application') {
            steps {
                sh '''
                mvn clean package -DskipTests --settings $WORKSPACE/.m2/settings.xml
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh '''
                mvn test --settings $WORKSPACE/.m2/settings.xml
                '''
            }
        }
    }

    post {
        always {
            sh '''
            docker compose -f $WORKSPACE/src/test/resources/docker-compose.yml down
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
