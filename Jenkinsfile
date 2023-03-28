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
                script{
                    def logContent = currentBuild.rawBuild.getLog()
                    def directory = "${env.WORKSPACE}/Logs" // change directory name here
                    writeFile file: "${directory}/buildConsolelog.txt", text: logContent
                    //script {
                        //def logContent = Jenkins.getInstance().getItemByFullName(env.JOB_NAME).getBuildByNumber(
                            //Integer.parseInt(env.BUILD_NUMBER)).logFile.text
                        // copy the log in the job's own workspace
                        //writeFile file: directory + "/buildConsolelog.txt",
                            //text: logContent
                    //}
                    def consoleOutput = readFile directory + '/buildConsolelog.txt'
                    echo 'Console output saved in the buildConsolelog file'
                    echo '--------------------------------------'
                    echo consoleOutput
                    echo '--------------------------------------'
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
