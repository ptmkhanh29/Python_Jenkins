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
/*
pipeline{
    agent any
    parameters {
        string(name: 'PARAMETER_NAME', defaultValue: 'default_value', description: 'Description of parameter')
        
    }
    stages {
        stage('Example Stage') {
            steps {
                echo "Building branch: ${params.CHOICE-1}"
                echo "The parameter value is: ${params.PARAMETER_NAME}"
            }
        }
    }
}*/
/* OK
properties([
    parameters([
        choice(
            name: 'ENV',
            choices: [
                'dev',
                'prod'
            ]
        ),
        [$class: 'ChoiceParameter',
            choiceType: 'PT_RADIO',
            filterLength: 1,
            filterable: false,
            name: 'CHOICES',
            script: [
                $class: 'GroovyScript',
                fallbackScript: [
                    classpath: [],
                    sandbox: false,
                    script: 'return ["Check Jenkins ScriptApproval page"]'
                ],
                script: [
                    classpath: [],
                    sandbox: false,
                    script: 'return ["One","Two:selected"]'
                ]
            ]
        ]
    ])
])

pipeline {
    agent any

    stages {
        stage('Print the Values') {
            steps {
                echo "Environment: ${params.ENV}"
                echo "Choice: ${params.CHOICES}"
            }
        }
    }
}*/
properties([
    parameters([
        [
            $class: 'DynamicReferenceParameter',
            description: 'Select a git branch',
            name: 'GIT_BRANCH',
            script: [
                $class: 'GroovyScript',
                fallbackScript: [
                    classpath: [],
                    sandbox: false,
                    script: '''
                    return ["No git branches found"]
                    '''
                ],
                script: '''
                def gitUrl = "https://github.com/ptmkhanh29/Python_Jenkins.git"
                def git = checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: gitUrl]]])
                def branches = git.git("ls-remote --heads ${gitUrl}").split().collect { it.replaceAll(/.*refs\/heads\//, "") }
                if (branches) {
                    return branches
                } else {
                    return ["No git branches found"]
                }
                '''
            ]
        ]
    ])
])

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo "Building git branch ${params.GIT_BRANCH}"
                // thực hiện build với git branch tương ứng được chọn
            }
        }
    }
}

