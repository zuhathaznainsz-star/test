pipeline {
    agent any

    tools {
        maven 'Maven_3.9.11'   // Name from Jenkins "Global Tool Configuration"
        jdk 'Java_21'          // Name from Jenkins "Global Tool Configuration"
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/AshwinSubramanyaGS/CI-CD-Try-Java.git'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Version Document') {
            steps {
                bat 'echo Build Version: > version.txt'
                bat 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout >> version.txt'
                bat 'echo Build Time: %DATE% %TIME% >> version.txt'
                archiveArtifacts artifacts: 'version.txt'
            }
        }

        stage('Local Deploy') {
    steps {
        script {
            // Disable echo, get the first .jar file in target
            def jarFile = bat(
                script: '@echo off & for /f "delims=" %%i in (\'dir /B target\\*.jar\') do @echo %%i',
                returnStdout: true
            ).trim()

            if (jarFile) {
                echo "Deploying JAR: ${jarFile}"
                bat "@echo off & java -jar \"target\\${jarFile}\" --server.port=9090"
            } else {
                error "No runnable JAR found in target directory!"
            }
        }
    }
}



    }

    post {
        success {
            echo "Build and Deploy completed successfully!"
        }
        failure {
            echo "Build failed!"
        }
    }
}