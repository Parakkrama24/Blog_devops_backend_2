pipeline {
    agent any

    environment {
        MAVEN_HOME = '/usr/share/maven'
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
        DB_HOST = 'jenkins-mysql-db.cc3c8i2skz7p.us-east-1.rds.amazonaws.com'
        DB_PORT = '3306'
        IMAGE_NAME = 'blog-backend'
        IMAGE_TAG = 'latest'
        DOCKER_USER = 'parakkrama'
    }


     stage('Terraform Init') {
            steps {
                script {
                    sh 'terraform init'
                }
            }
        }


    
        // Terraform Apply
        stage('Terraform Apply') {
            steps {
                script {
                    sh 'terraform apply -auto-approve'
                }
            }
        }
    

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Parakkrama24/Blog_devops_backend_2.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh 'mvn package'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'mysql-aws-creds', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASSWORD')]) {
                        sh '''
                        docker build -t $DOCKER_USER/$IMAGE_NAME:$BUILD_NUMBER .
                        '''
                    }
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'docker-hub-password', variable: 'DOCKER_PASS')]) {
                        sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    }
                }
            }
        }

        stage('Push Image') {
            steps {
                sh 'docker push $DOCKER_USER/$IMAGE_NAME:$BUILD_NUMBER'
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
