pipeline {
    agent any

    environment {
        // ------------------------
        // Variables de Base de Datos
        // ------------------------
        DB_URL      = "jdbc:mysql://delivery-system-rds.cdgw46wiku38.us-east-2.rds.amazonaws.com:3306/delivery_system_db"
        DB_USERNAME = "admin"
        DB_PASSWORD = credentials('db-password') // credencial de Jenkins para la contraseña

        // ------------------------
        // Variables AWS / Storage
        // ------------------------
        AWS_REGION           = "us-east-2"
        //AWS_ACCESS_KEY_ID    = credentials('aws-access-key-id') // Evita error si no está configurado
        //AWS_SECRET_ACCESS_KEY = credentials('aws-secret-access-key') // Evita error si no está configurado
        S3_BUCKET_FRONTEND   = "delivery-system-frontend "
        S3_BUCKET_BACKEND    = "mi-proyecto-backend-storage"

        // ------------------------
        // Backend y Frontend
        // ------------------------
        SERVER_PORT           = "8081"
        CORS_ALLOWED_ORIGINS  = "http://${S3_BUCKET_FRONTEND}.s3-website-${AWS_REGION}.amazonaws.com"

        // ------------------------
        // JAVA y PATH
        // ------------------------
        JAVA_HOME = "/usr/lib/jvm/jdk-24"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }

    tools {
        maven 'Maven'      // Nombre de tu instalación de Maven en Jenkins
        nodejs 'NodeJS-22' // Nombre de tu instalación de NodeJS en Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                echo '🔄 Clonando repositorio...'
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/RudyReyes28/delivery_system_ayd1.git'
            }
        }

        stage('Build Backend') {
            steps {
                echo 'Construyendo Backend (Spring Boot)...'
                dir('SistemasEntregas') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Test Backend') {
            steps {
                echo '🧪 Ejecutando tests del Backend...'
                dir('SistemasEntregas') {
                    sh 'mvn test'
                }
            }
        }

        stage('Deploy Backend') {
            steps {
                echo ' Desplegando Backend...'
                dir('SistemasEntregas') {
                    sh """
                        # Detener proceso anterior
                        pkill -f 'java.*SistemasEntregas' || true

                        # Iniciar aplicación en background pasando variables de entorno
                        nohup java -jar target/*.jar \
                            --spring.datasource.url=${DB_URL} \
                            --spring.datasource.username=${DB_USERNAME} \
                            --spring.datasource.password=${DB_PASSWORD} \
                            --server.port=${SERVER_PORT} \
                            --cors.allowed.origins=${CORS_ALLOWED_ORIGINS} \
                            --storage.type=s3 \
                            --storage.path=${S3_BUCKET_BACKEND} \
                            --aws.region=${AWS_REGION} \
                            > /var/log/backend.log 2>&1 &

                        # Esperar a que inicie
                        sleep 15

                        # Verificar que está corriendo
                        curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1
                    """
                }
            }
        }

        stage('Build Frontend') {
            steps {
                echo 'Construyendo Frontend (Angular)...'
                dir('frontend') {
                    sh '''
                        npm ci
                        npm run build -- --configuration production
                    '''
                }
            }
        }

        stage('Deploy Frontend to S3') {
            steps {
                echo ' Desplegando Frontend a S3...'
                withAWS(credentials: 'aws-credentials', region: "${AWS_REGION}") {
                    dir('frontend/dist') {
                        sh """
                            aws s3 sync . s3://${S3_BUCKET_FRONTEND}/ --delete
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completado exitosamente!'
            echo "Frontend disponible en: http://${S3_BUCKET_FRONTEND}.s3-website-${AWS_REGION}.amazonaws.com"
            echo "Backend disponible en: http://TU-IP-EC2:${SERVER_PORT}"
        }
        failure {
            echo 'Pipeline falló. Revisa los logs.'
        }
        always {
            echo 'Limpiando workspace...'
            cleanWs()
        }
    }
}
