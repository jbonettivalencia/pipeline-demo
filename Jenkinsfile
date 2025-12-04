pipeline {
    agent any

    environment {
        // Add Maven path so Jenkins can find mvn
        PATH = "/opt/homebrew/bin:${env.PATH}"

        STAGING_PORT = "8081"
        PROD_PORT    = "8082"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                sh "mvn -B clean test"
            }
        }

        stage('Package') {
            steps {
                sh "mvn -B package"
            }
        }

        stage('Deploy to STAGING') {
            steps {
                sh "chmod +x scripts/deploy.sh"
                sh "./scripts/deploy.sh staging"
            }
        }

        stage('Health Check (STAGING)') {
            steps {
                sh "sleep 2"
                sh "curl -f http://localhost:${STAGING_PORT}/health"
            }
        }

        stage('Load Test (STAGING)') {
            steps {
                sh '''
                  echo "Starting simple load test on STAGING..."
                  for i in $(seq 1 50); do
                    curl -s http://localhost:'"${STAGING_PORT}"'/ > /dev/null
                  done
                  echo "Load test finished."
                '''
            }
        }

        stage('Show STAGING Logs') {
            steps {
                sh "echo 'Last 10 lines of staging log:'"
                sh "tail -n 10 app-staging.log || true"
            }
        }

        stage('Approve Promotion to PROD') {
            steps {
                input(message: 'Promote this build to PRODUCTION?', ok: 'Deploy')
            }
        }

        stage('Deploy to PROD') {
            steps {
                sh "./scripts/deploy.sh prod"
            }
        }

        stage('Health Check (PROD)') {
            steps {
                sh "sleep 2"
                sh "curl -f http://localhost:${PROD_PORT}/health"
            }
        }
    }

    post {
        failure {
            echo "Pipeline failed. Re-run the last successful build as a simple rollback."
        }
    }
}

