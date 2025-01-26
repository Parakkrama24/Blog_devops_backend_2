pipeline {
    agent any

    environment {
        MAVEN_HOME = 'C:\\Program Files\\apache-maven-3.9.9'  // Update with your Maven path
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.5\\bin'    // Update with your JDK path
    }

    stages {
        stage('Clone Repository') {
            steps {
                // Clone the Git repository
                git branch: 'main', url: 'https://github.com/Parakkrama24/Blog_devops_backend_2.git'
            }
        }

        stage('Build') {
            steps {
                // Build the project using Maven
                sh "${MAVEN_HOME}/bin/mvn clean package"
            }
        }

        stage('Test') {
            steps {
                // Run tests
                sh "${MAVEN_HOME}/bin/mvn test"
            }
        }

        stage('Package') {
            steps {
                // Package the Spring Boot application
                echo 'Packaging application...'
                sh "${MAVEN_HOME}/bin/mvn package"
            }
        }

        stage('Deploy') {
            steps {
                // Deploy the application (example: copying JAR to a server or running with Docker)
                echo 'Deploying application...'
                sh '''
                # Example deployment script
                cp target/*.jar /path/to/deployment/
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}
