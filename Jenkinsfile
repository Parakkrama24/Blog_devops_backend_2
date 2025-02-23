pipeline {
    agent any

    environment {
        MAVEN_HOME = 'C:\\Program Files\\apache-maven-3.9.9'
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.5'
        DB_HOST = "localhost"
        DB_PORT = "3306"
        DOCKER_HUB_USER = "parakkrama24"
        IMAGE_NAME = "blog-backend"
        IMAGE_TAG = "latest"

        DOCKER_USER = 'parakkrama'
        DOCKER_PASS = 'Para123##'  // Ensure escaping is correct
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Parakkrama24/Blog_devops_backend_2.git'
            }
        }

        stage('Build') {
            steps {
                bat "\"%MAVEN_HOME%\\bin\\mvn\" clean package"
            }
        }

        stage('Test') {
            steps {
                bat "\"%MAVEN_HOME%\\bin\\mvn\" test"
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'
                bat "\"%MAVEN_HOME%\\bin\\mvn\" package"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'mysql-creds', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASSWORD')]) {
                        bat '''
                        docker build -t %DOCKER_HUB_USER%/%IMAGE_NAME%:%BUILD_NUMBER% .
                        '''
                    }
                }
            }
        }

       stage('Login to Docker Hub') {
            steps {
                script {
                    bat label: 'Docker Login', script: "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
                }
            }
        }
    }


        stage('Push Image') {
            steps {
                bat 'docker push %DOCKER_HUB_USER%/%IMAGE_NAME%:%BUILD_NUMBER%'
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'mysql-creds', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASSWORD')]) {
                        bat '''
                        docker stop blog-backend-container || docker rm blog-backend-container
                        docker run -d --name blog-backend-container ^
                        -e MYSQL_USER=%DB_USER% ^
                        -e MYSQL_PASSWORD=%DB_PASSWORD% ^
                        -e MYSQL_DATABASE=blog_db ^
                        -e MYSQL_HOST=%DB_HOST% ^
                        -e MYSQL_PORT=%DB_PORT% ^
                        -p 7070:8080 %DOCKER_HUB_USER%/%IMAGE_NAME%:%BUILD_NUMBER%
                        '''
                    }
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