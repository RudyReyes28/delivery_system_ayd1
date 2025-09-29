pipeline {
    agent any // Indica que el pipeline puede ejecutarse en cualquier agente de Jenkins disponible.

    // --- VARIABLES DE ENTORNO GLOBALES ---
    // Centraliza aquí toda tu configuración. ¡ES CRÍTICO QUE REEMPLACES ESTOS VALORES!
    environment {
        AWS_REGION         = 'us-east-2' // Ejemplo: 'us-east-1', 'eu-west-1', etc.
        S3_BUCKET_NAME     = 'delivery-system-frontend' // El nombre único que le diste a tu bucket S3.
        ECR_REPOSITORY_URI = '776410620122.dkr.ecr.us-east-2.amazonaws.com/delivery-system/backend' // El URI que copiaste de la consola de ECR. Ejemplo: '123456789012.dkr.ecr.us-east-1.amazonaws.com/mi-proyecto/backend'
    }

    stages {
        // --- Etapa 1: Obtener el Código Fuente ---
        stage('Checkout') {
            steps {
                echo 'Limpiando el espacio de trabajo y clonando el repositorio...'
                cleanWs() // Buena práctica: borra archivos de builds anteriores.
                git branch: 'main', url: 'https://github.com/RudyReyes28/delivery_system_ayd1.git' // Reemplaza con la URL de tu repo.
            }
        }

        // --- Etapa 2: Construir el Backend ---
        stage('Build Backend') {
            // Usa un agente Docker temporal solo para esta etapa.
            agent { docker { image 'maven:3.9.6-eclipse-temurin-24' } }
            steps {
                // Ejecuta el comando de Maven dentro del directorio del backend.
                dir('SistemasEntregas') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        // --- Etapa 3: Construir el Frontend ---
        stage('Build Frontend') {
            // Usa un agente Docker temporal con Node.js.
            agent { docker { image 'node:22-alpine' } }
            steps {
                // Ejecuta los comandos de npm dentro del directorio del frontend.
                dir('frontend') {
                    sh 'npm install' // Instala dependencias.
                    sh 'npm run build' // Compila los archivos estáticos.
                }
            }
        }

        // --- Etapa 4: Desplegar el Frontend a S3 ---
        stage('Deploy Frontend to S3') {
            steps {
                echo "Subiendo archivos estáticos al bucket S3: ${S3_BUCKET_NAME}"
                // El comando 'aws s3 sync' es muy eficiente. Sube los archivos de la carpeta 'dist'
                // a la raíz de tu bucket. '--delete' elimina archivos viejos del bucket.
                // La autenticación ocurre automáticamente gracias al Rol de IAM de la EC2.
                sh "aws s3 sync frontend/dist/frontend/ s3://${S3_BUCKET_NAME} --delete"
            }
        }

        // --- Etapa 5: Construir y Publicar la Imagen del Backend ---
        stage('Build and Push Backend Image to ECR') {
            steps {
                script {
                    // El URI completo de la imagen, incluyendo una etiqueta única (el número de build de Jenkins).
                    def imageUriWithTag = "${ECR_REPOSITORY_URI}:${BUILD_NUMBER}"

                    // 1. Inicia sesión en el registro de ECR de forma segura.
                    sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY_URI}"

                    // 2. Construye la imagen Docker usando el Dockerfile.backend.
                    sh "docker build -f Dockerfile.backend -t ${imageUriWithTag} ."

                    // 3. Sube (push) la imagen construida al repositorio de ECR.
                    echo "Subiendo imagen ${imageUriWithTag} a ECR..."
                    sh "docker push ${imageUriWithTag}"
                }
            }
        }

        // --- Etapa 6: Despliegue Final (Placeholder) ---
        stage('Deploy Backend to Production') {
            steps {
                // Este es el último paso que configuraremos más adelante.
                // Aquí irían los comandos para decirle a AWS Elastic Beanstalk que se actualice
                // con la nueva imagen que acabamos de subir a ECR.
                echo "IMAGEN LISTA PARA DESPLEGAR: ${ECR_REPOSITORY_URI}:${BUILD_NUMBER}"
                echo "Próximo paso: Configurar Elastic Beanstalk."
            }
        }
    }

    // --- Acciones que se ejecutan al final del pipeline ---
    post {
        always {
            // Esto siempre se ejecuta, ya sea que el pipeline falle o tenga éxito.
            echo 'Pipeline finalizado. Limpiando espacio de trabajo.'
            cleanWs()
        }
    }
}