/* groovylint-disable NoDef, VariableTypeRequired */
/******************************************************************************
Function name: getGitBranches
Input parameters: No
Output parameters: all name git branchs on repository
Purpose: To generate the AUTOSAR version base on Variant name and project requirement
******************************************************************************/
/* groovylint-disable-next-line CompileStatic */
def getGitBranches = {
    def gitBranches = []
    sh(script: 'git ls-remote --heads origin', returnStdout: true).trim().eachLine { line ->
        /* groovylint-disable-next-line VariableTypeRequired */
        def matcher = line =~ /refs\\/heads\\/(.*)/
        if (matcher.matches()) {
            gitBranches << matcher[0][1]
        }
    }
    return gitBranches.sort()
}

return activeChoicesReactiveParam(
    name: 'BRANCH_NAME',
    description: 'Select the git branch name',
    script: [
        $class: 'GroovyScript',
        script: getGitBranches
    ],
    choiceType: 'PT_SINGLE_SELECT',
    filterLength: 1
)
