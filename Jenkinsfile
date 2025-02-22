pipeline {
    agent any

    environment {
        MAVEN_HOME = 'C:\\Program Files\\apache-maven-3.9.9'  // Update if different
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.5'      // Update if different
        DB_HOST = "localhost"
        DB_PORT = "3306"
        DOCKER_HUB_USER = "parakkrama dasanayaka"  // Update with your Docker Hub username
        IMAGE_NAME = "blog-backend"
        IMAGE_TAG = "latest"
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
                        docker build -t blog-backend .
                        '''
                    }
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'mysql-creds', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASSWORD')]) {
                        bat '''
                        docker stop blog-backend-container || true
                        docker rm blog-backend-container || true
                        docker run -d --name blog-backend-container ^
                        -e MYSQL_USER=%DB_USER% ^
                        -e MYSQL_PASSWORD=%DB_PASSWORD% ^
                        -e MYSQL_DATABASE=blog_db ^
                        -e MYSQL_HOST=%DB_HOST% ^
                        -e MYSQL_PORT=%DB_PORT% ^
                        -p 7070:8080 blog-backend
                        '''
                    }
                }
            }
        }

        stage('Docker Login') {
            steps {
                echo 'Logging into Docker Hub...'
                bat '''
                echo "PVL7iX.DtGif2zA" | docker login -u %DOCKER_HUB_USER% --password-stdin
                '''
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                bat '''
                copy target\\*.jar C:\\path\\to\\deployment\\
                '''
            }
        }

         stage('Docker Push') {
            steps {
                echo 'Pushing image to Docker Hub...'
                bat '''
                docker push %DOCKER_HUB_USER%/%IMAGE_NAME%:%IMAGE_TAG%
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
