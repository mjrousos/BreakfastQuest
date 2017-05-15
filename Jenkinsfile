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
                sh 'sudo chmod 777 $WORKSPACE/composeVersion.sh'
                sh '$WORKSPACE/composeVersion.sh'
            }
        }
        stage('Build') {
            steps {
                sh 'cd $WORKSPACE/WebClient && npm install && npm run build'
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
