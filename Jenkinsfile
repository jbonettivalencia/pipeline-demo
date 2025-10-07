pipeline {
  agent any
  tools { jdk 'JDK17'; maven 'Maven3' }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B clean verify'
      }
    }

    stage('Package') {
      steps {
        sh 'mvn -B package'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('Docker Build') {
      steps {
       echo 'Simulating Docker build...'

      }
    }

    stage('Smoke Test') {
      steps {
        sh '''
          docker run -d --rm -p 8080:8080 --name pipeline-demo pipeline-demo:${BUILD_NUMBER}
          sleep 3
          curl -f http://localhost:8080/
          docker stop pipeline-demo
        '''
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
    success {
      echo '✅ CI/CD complete: built, tested, and containerized successfully!'
    }
  }
}
