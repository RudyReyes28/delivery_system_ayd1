pipeline {
    agent any

    environment {
        SPRING_DATASOURCE_URL      = "jdbc:mysql://delivery-system-rds.cdgw46wiku38.us-east-2.rds.amazonaws.com:3306/delivery_system_db"
        SPRING_DATASOURCE_USERNAME = credentials('db-user')
        SPRING_DATASOURCE_PASSWORD = credentials('db-password')
        SERVER_PORT                = "8081"

        AWS_REGION                 = "us-east-2"
        AWS_ACCESS_KEY_ID          = credentials('aws-key')
        AWS_SECRET_ACCESS_KEY      = credentials('aws-secret')
        S3_BUCKET_FRONTEND         = "delivery-system-frontend "
        S3_BUCKET_BACKEND          = "mi-proyecto-backend-storage"

        // ------------------------
        // Backend y Frontend
        // ------------------------
        CORS_ALLOWED_ORIGINS  = "http://${S3_BUCKET_FRONTEND}.s3-website-${AWS_REGION}.amazonaws.com"

        STORAGE_TYPE               = "s3"
        RUTA_LOCAL                 = "/tmp/uploads/"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // stage('Compile & Test') {
        //     steps {
        //         sh 'cd mvn test'
        //     }
        // }


        stage('Deploy') {
             steps {
                sh 'cd SistemasEntregas && mvn package -DskipTests'
                sh 'sudo -E docker-compose down'
                sh 'sudo -E docker-compose up --build -d'
                echo "Despliegue a DEV exitoso"
            }
        }
    }
}