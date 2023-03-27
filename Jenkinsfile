pipeline {
    agent any
    stages {
        stage('Checkout'){
            steps{
                dir('/Users/Admin/Desktop/python_cicd/Jenkins_Test'){
                    checkout scmGit(branches: [[name: '*/python_ver']], 
                                extensions: [], 
                                userRemoteConfigs: [[url: 'https://github.com/ptmkhanh29/Python_Jenkins.git']])
                }
            }
        }
        stage('Configure Git user') {
            steps {
                sh 'git config user.name "ptmkhanh29'
                sh 'git config user.email "ptmkhanh29@gmail.com"'
            }
        }
    
        stage('Build'){
            steps{
                //git branch: 'python_ver', url: 'https://github.com/ptmkhanh29/Python_Jenkins.git'
                bat 'python python_cicd.py'
            }
        }
        stage('Test'){
            steps{
                echo "The job has been tested"
            }
        }
    }
}
