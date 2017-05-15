pipeline {
    agent any

    triggers {
        pollSCM 'H/2 * * * *'
    }

    tools {
        nodejs 'NodeJS-7.10'
    }

    stages {
        stage('Stamp Version') {
            steps {
                echo 'Getting version...'
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
            }
        }
        stage('Test') {
            steps {
                echo 'No tests yet'
            }
        }
        stage('Publish Images') {
            steps {
                echo 'Pushing to ACR...'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying to Azure'
            }
        }
    }
}