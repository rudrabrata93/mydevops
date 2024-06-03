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
        stage('CheckStyle'){
            steps{
                sh 'mvn checkstyle:checkstyle'
            }
        }
        stage('Sonar Code Analysis'){
            environment{
                scannerHome = tool 'sonar4.7'
            }
            steps{
                withSonarQubeEnv('sonar'){
                    sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=vprofile \
                    -Dsonar.projectName=vprofile-repo \
                    -Dsonar.projectVersion=1.0 \
                    -Dsonar.sources=src/ \
                    -Dsonar.java.binaries=target/test-classes/com/visualpathit/account/controllerTest/ \
                    -Dsonar.junit.reportsPath=target/surefire-reports/ \
                    -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                    -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
                }
            }
        }
        stage('QualityGate'){
            steps{
                timeout(time:1, unit:'HOURS'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('UploadArtifact'){
            steps{
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: '172.31.49.130:8081',
                    groupId: 'qa',
                    version: "${env.BUILD_ID}",
                    repository: 'myrepo',
                    credentialsId: 'nexuslogin',
                    artifacts: [
                        [artifactId: 'vprofile-repo',
                         classifier: '',
                         file: 'target/vprofile-v2.war',
                         type: 'war']
                    ]
                )
            }
        }
    }
}