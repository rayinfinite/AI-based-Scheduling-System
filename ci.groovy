pipeline {
    agent any

    stages {
        stage('checkout') {
            steps {
                git branch: 'leelaker', url: 'https://github.com/rayinfinite/AI-based-Scheduling-System.git'
            }
        }

        stage('frontend') {
            steps {
                dir("frontend") {
                    sh '''
                    npm install
                    npm run build
                    cp -R ./dist/* ../src/main/resources/static/
                    '''
                }
            }
        }

        stage('backend') {
            steps {
                sh '''
                mvn clean package
                '''
            }
        }

        stage('check') {
            steps {
                echo 'Checking contents of the static and target directories...'
                sh '''
                ls -R ./src/main/resources/static/
                ls ./target
                '''
            }
        }

//        stage('docker') {
//            steps {
//                sh '''
//                docker build -t scheduling-system .
//                docker run -d -p 8080:8080 scheduling-system
//                '''
//            }
//        }
    }
}