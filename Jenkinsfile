pipeline {
    agent any

    environment {
        AWS_REGION         = 'us-east-2'
        S3_BUCKET_NAME     = 'delivery-system-frontend'
        ECR_REPOSITORY_URI = '776410620122.dkr.ecr.us-east-2.amazonaws.com/delivery-system/backend'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Limpiando el espacio de trabajo y clonando el repositorio...'
                cleanWs()
                git branch: 'main',
                    url: 'https://github.com/RudyReyes28/delivery_system_ayd1.git',
                    credentialsId: 'github-credentials-ci'
            }
        }

        stage('Build Backend') {
            steps {
                dir('SistemasEntregas') {
                    // ESTA IMAGEN SÍ EXISTE - Verificada en Docker Hub oficial
                    sh 'docker run --rm -v $PWD:/app -w /app maven:eclipse-temurin-21 mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'docker run --rm -v $PWD:/app -w /app node:22-alpine sh -c "npm install && npm run build"'
                }
            }
        }

        stage('Deploy Frontend to S3') {
            steps {
                echo "Subiendo archivos estáticos al bucket S3: ${S3_BUCKET_NAME}"
                sh "aws s3 sync frontend/dist/frontend/ s3://${S3_BUCKET_NAME} --delete"
            }
        }

        stage('Build and Push Backend Image to ECR') {
            steps {
                script {
                    def imageUriWithTag = "${ECR_REPOSITORY_URI}:${BUILD_NUMBER}"
                    sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY_URI}"
                    sh "docker build -f Dockerfile.backend -t ${imageUriWithTag} ."
                    sh "docker push ${imageUriWithTag}"
                }
            }
        }

        stage('Deploy Backend to Production') {
            steps {
                echo "IMAGEN LISTA PARA DESPLEGAR: ${ECR_REPOSITORY_URI}:${BUILD_NUMBER}"
                echo "Próximo paso: Configurar Elastic Beanstalk."
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado. Limpiando espacio de trabajo.'
            cleanWs()
        }
    }
}