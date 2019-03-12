pipeline {
    agent {
        label 'docker'
    }

    stages {
        stage ('build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage ('installDist') {
            steps {
                sh './gradlew installDist'
            }
        }
    }
}
