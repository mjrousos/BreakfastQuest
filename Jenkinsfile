pipeline {
    agent any

    triggers {
        pollSCM "H/2 * * * *"
    }

    tools {
        nodejs "NodeJS-7.10"
    }

    stages {
        stage("Stamp Version") {
            steps {
                sh "sudo chmod 777 $WORKSPACE/composeVersion.sh"
                sh "$WORKSPACE/composeVersion.sh"
            }
        }
        stage("Build") {
            steps {
                sh "cd $WORKSPACE/WebClient && npm install && npm run build"
            }
        }
        stage("Build Docker Images") {
            environment {
                BQ_TAG = readFile("$WORKSPACE/generated/version.txt").trim()
                BQ_INGRESS_PORT = "8080"
            }
            steps {
                sh "docker-compose -f docker-compose.yml -f docker-compose.build.yml build"
            }
        }
        stage("Test") {
            steps {
                echo "No tests yet"
            }
        }
        stage("Publish Images") {
            environment {
                BQ_TAG = readFile("$WORKSPACE/generated/version.txt").trim()
                BQ_INGRESS_PORT = "8080"
            }
            steps {
                withDockerRegistry([credentialsId: 'MJRContainers', url: 'http://mjrcontainers.azurecr.io']) {
                    sh "docker-compose -f docker-compose.yml -f docker-compose.build.yml push"
                    sh "docker rm -f \$(docker ps -a -q) || echo 'No docker images to remove'"
                    sh "docker rmi -f \$(docker images -q) || echo 'Warning: Docker rmi failed - likely images were listed multiple times'"
                }
            }
        }
        stage("Deploy") {
            steps {
                echo "Deploying to Azure"
            }
        }
    }
}
