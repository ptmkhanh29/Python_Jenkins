pipeline {
    agent any
    stages { 
        stage('Checkout'){
            steps{
                checkout scmGit(branches: [[name: '*/python_ver']], 
                extensions: [], 
                userRemoteConfigs: [[url: 'https://github.com/ptmkhanh29/Python_Jenkins.git']])
            }
        }
        stage('Build'){
            steps{
                git branch: 'python_ver', url: 'https://github.com/ptmkhanh29/Python_Jenkins.git'
                bat 'python python_cicd.py'
                
            }
        }
        stage('Test'){
            steps{
                echo "The job has been tested"
            }
        }
        
    }
    post {
        always {
            echo 'One way or another, I have finished'
            deleteDir() /* clean up our workspace */
        }           
        success {
            echo 'This will run only if successful'
            writeFile(file: 'log.txt', text: "${env.BUILD_LOG}")
        }
        failure {
            echo 'This will run only if failed'
            writeFile(file: 'log.txt', text: "${env.BUILD_LOG}")
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
            writeFile(file: 'log.txt', text: "${env.BUILD_LOG}")
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}
