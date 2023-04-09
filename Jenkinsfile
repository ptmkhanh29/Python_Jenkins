/*
def paramPath = 'C:/Users/khanh.phan-minh/Desktop/Jenkins/Github/Python_Jenkins/ParameterDefinition.groovy'
//groovylint-disable-next-line UnusedVariable 
def branchName = ''
node {
    load "${paramPath}"

    // Get the selected git branch from user input
    branchName = ParameterDefinition.BRANCH_NAME
}
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                /*checkout scmGit(branches: [[name: 'python_ver']],
                //extensions: [],
                //userRemoteConfigs: [[url: 'https://github.com/ptmkhanh29/Python_Jenkins.git']])
                //echo "Checkout Done!"
                checkout scmGit(branches: [[name: branchName]],
                                extensions: [],
                                userRemoteConfigs: [[url: 'https://github.com/ptmkhanh29/Python_Jenkins.git']])
                echo 'Checkout Done!'
            }
        }
        stage('Build') {
            steps {
                //git branch: 'python_ver', url: 'https://github.com/ptmkhanh29/Python_Jenkins.git'
                // if Window OS using bat, linux OS using sh
                bat 'python python_cicd.py'
            }
        }
        /*
        # Create file on local machine
        stage('Capture Console Output') {
            steps{
                script{
                    def logContent = currentBuild.rawBuild.getLog()
                    def directory = "C:/Users/khanh.phan-minh/Desktop/Jenkins/Ouput_Log/${env.JOB_NAME}_
                    ${env.BUILD_NUMBER}"
                    // change directory name here
                    writeFile file: "${directory}/buildConsolelog.txt", text: logContent, charset: 'UTF-8'
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
        stage('Test') {
            steps {
                echo 'The job has been tested'
            }
        }
    }
    post {
        always {
            echo 'One way or another, I have finished'
            deleteDir() //clean up our workspace 
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}*/
pipeline{
    agent any
    parameters {
        string(name: 'PARAMETER_NAME', defaultValue: 'default_value', description: 'Description of parameter')
        activeChoicesReactiveParam('BRANCH', 'Select git branch') {
            description('Select the branch to build')
            choiceType('ET_BRANCH')
            groovyScript("""
                def gitUrl = 'https://github.com/ptmkhanh29/Python_Jenkins'
                def branches = []
                sh "git ls-remote --heads ${gitUrl} | cut -d'/' -f3 | sed 's/\\^{}//g' > branches.txt"
                readFile('branches.txt').eachLine {
                    branches.add(it.trim())
                }
                return branches
            """)
        }
    }
    stages {
        stage('Example Stage') {
            steps {
                echo "Building branch: ${params.BRANCH}"
                echo "The parameter value is: ${params.PARAMETER_NAME}"
            }
        }
    }
}
