pipeline{
    agent any
    tools{
        maven 'MAVEN3'
        jdk 'java11'
    }

    stages{
        stage('Fetch Code'){
            steps{
                git branch: 'main', url: 'https://github.com/hkhcoder/vprofile-project.git'
            }
        }
        stage('Build'){
            steps{
                sh 'mvn install -DskipTests'
            }
            post{
                success{
                    echo "Archiving artifacts now"
                    archiveArtifacts artifacts: '**/*.war'
                }
            }
        }
        stage('Unit Test'){
            steps{
                sh 'mvn test'
            }
        }
    }
}