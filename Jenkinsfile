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
        stage('Capture Console Output') {
            steps{
                script {
                    println "Starting capture console output"
                    def logContent = currentBuild.rawBuild.getLog()
                    // ...
                    writeFile file: directory + "/buildConsolelog.txt", text: logContent, charset: 'UTF-8'
                    println "Console output saved to file"
                }
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
            //deleteDir() /* clean up our workspace */
        }           
        success {
            echo 'This will run only if successful'
            script {
                bat "type log.txt"
            }
        }
        failure {
            echo 'This will run only if failed'
            script {
                bat "type log.txt"
            }
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
            script {
                bat "type log.txt"
            }
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
            script {
                bat "type log.txt"
            }
        }
    }
}
