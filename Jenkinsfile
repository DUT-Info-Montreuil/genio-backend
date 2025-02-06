pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Check Logs Access') {
            steps {
                sh 'ls -l logs/prod/'
                sh 'cat logs/prod/application.log'
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "Déploiement en cours..."'
            }
        }
    }
}