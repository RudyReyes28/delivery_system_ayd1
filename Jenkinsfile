pipeline {
    agent any

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
