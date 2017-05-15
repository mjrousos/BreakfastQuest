pipeline {
    agent any

    triggers {
        pollSCM 'H/2 * * * *'
    }

    tools {
        node 'NodeJS-7.10'
        docker 'Docker'
        ansible 'Ansible 2.0.0.2'
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