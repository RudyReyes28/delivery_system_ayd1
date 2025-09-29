pipeline {
    agent any

    environment {
        SPRING_DATASOURCE_URL      = "jdbc:mysql://delivery-system-rds.cdgw46wiku38.us-east-2.rds.amazonaws.com:3306/delivery_system_db"
        SPRING_DATASOURCE_USERNAME = credentials('db-user')
        SPRING_DATASOURCE_PASSWORD = credentials('db-password')
        SERVER_PORT                = "8081"
        CORS_ALLOWED_ORIGINS       = "http://localhost:4200"

        AWS_ACCESS_KEY_ID          = credentials('aws-key')
        AWS_SECRET_ACCESS_KEY      = credentials('aws-secret')
        AWS_REGION                 = "us-east-1"

        STORAGE_TYPE               = "s3"
        S3_BUCKET_ARCHIVOS         = "archivos-ayd1-proyecto1"
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
                sh 'cd SistemasEntregas && cd target/config-dev-docker && sudo docker-compose down'
                sh 'cd SistemasEntregas && cd target/config-dev-docker && sudo docker-compose up --build -d'
                echo "Despliegue a DEV exitoso"

            }
        }
    }
}