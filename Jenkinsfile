pipeline {
    agent any

    // ============================================================
    // TOOLS: Jenkins will use these pre-installed tools
    // We configure Maven & JDK in Jenkins Global Tool Configuration
    // ============================================================
    tools {
        maven 'Maven-3.9'       // Name must match Jenkins config
        jdk   'JDK-17'          // Name must match Jenkins config
    }

    // ============================================================
    // ENVIRONMENT VARIABLES
    // ============================================================
    environment {
        APP_NAME    = 'hello-service'
        APP_VERSION = '1.0.0'
        DOCKER_IMAGE = "hello-service:${BUILD_NUMBER}"
    }

    // ============================================================
    // PIPELINE STAGES
    // ============================================================
    stages {

        // STAGE 1: Checkout code from GitHub
        stage('Checkout') {
            steps {
                echo '========== STAGE 1: Checking out code from GitHub =========='
                checkout scm
                sh 'ls -la'
            }
        }

        // STAGE 2: Compile the Java code
        stage('Build') {
            steps {
                echo '========== STAGE 2: Compiling Java code =========='
                sh 'mvn clean compile -B'
            }
        }

        // STAGE 3: Run unit tests
        stage('Unit Tests') {
            steps {
                echo '========== STAGE 3: Running unit tests =========='
                sh 'mvn test -B'
            }
            post {
                always {
                    // Publish test results in Jenkins UI
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        // STAGE 4: Package as JAR
        stage('Package') {
            steps {
                echo '========== STAGE 4: Packaging JAR =========='
                sh 'mvn package -DskipTests -B'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        // STAGE 5: Build Docker image
        stage('Docker Build') {
            steps {
                echo '========== STAGE 5: Building Docker image =========='
                sh "docker build -t ${APP_NAME}:${BUILD_NUMBER} ."
                sh "docker build -t ${APP_NAME}:latest ."
                sh "docker images | grep ${APP_NAME}"
            }
        }

        // STAGE 6: Run smoke test on Docker container
        stage('Smoke Test') {
            steps {
                echo '========== STAGE 6: Running smoke test =========='
                sh """
                    docker run -d --name ${APP_NAME}-test -p 9090:8080 ${APP_NAME}:${BUILD_NUMBER}
                    sleep 15
                    curl -f http://localhost:9090/actuator/health || (docker logs ${APP_NAME}-test && exit 1)
                    curl -f http://localhost:9090/health
                    curl -f http://localhost:9090/api/status
                    echo "Smoke tests passed!"
                """
            }
            post {
                always {
                    sh "docker stop ${APP_NAME}-test || true"
                    sh "docker rm ${APP_NAME}-test || true"
                }
            }
        }

        // ============================================================
        // FUTURE STAGES (will add when AWS/EKS is ready):
        // - Push to ECR
        // - Push JAR to JFrog
        // - Deploy to EKS via Helm
        // ============================================================
    }

    // ============================================================
    // POST ACTIONS (run after all stages)
    // ============================================================
    post {
        success {
            echo """
            =========================================
             PIPELINE SUCCESS
             App: ${APP_NAME}
             Version: ${APP_VERSION}
             Build: #${BUILD_NUMBER}
             Docker Image: ${APP_NAME}:${BUILD_NUMBER}
            =========================================
            """
        }
        failure {
            echo """
            =========================================
             PIPELINE FAILED
             Build: #${BUILD_NUMBER}
             Check logs above for errors
            =========================================
            """
        }
        always {
            // Clean workspace after build
            cleanWs()
        }
    }
}
