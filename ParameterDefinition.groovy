import hudson.model.*
import jenkins.model.Jenkins
import groovy.json.JsonSlurper
import groovy.transform.Field
import static groovy.json.JsonOutput.*

class ParameterCollection extends Expando {
    /* This is an Expandable Class */
    def script

    def Render(inputParamNameList) {
        def uniqueInputParamNameList = inputParamNameList as Set
        def listAllProperties = this.properties.keySet()

        def outputParametersList = []
        for (paramName in uniqueInputParamNameList) {
            if (paramName in listAllProperties) {
                outputParametersList.add(this["${paramName}"].composeParameterDefinition())
            } else {
                def warning_message = "\\\"${paramName}\\\" is not defined yet!"
                def directive_description =
                    """<span style="color: yellow"><b>Parameter is not defined yet. Please define "${paramName}" in hjson database below!</b></span><br>
                        <span style="color: yellow"><b><i>\${env.libPath}/ParameterCollection/configs/ParameterDefinition.hjson</i></b></span>
                    """
                this["${paramName}"] = new ActiveChoicesReactiveParameter(
                    name        : paramName,
                    refNames    : "",
                    selectType  : "PT_SINGLE_SELECT",
                    description : directive_description,
                    groovyScript:
                        """return ["${warning_message}"]
                        """)
                outputParametersList.add(this["${paramName}"].composeParameterDefinition())
            }
        }
        return outputParametersList
    }

    def printEnv() {
        this.script.echo("---< THE FOLLOWING BATCH COMMANDS USED FOR SET ENVIRONMENT VARIABLES BEFORE START DEBUGGING ON REMOTE LAB >---")
        this.script.echo(this.script.env.getEnvironment().collect({envVar ->  "SET ${envVar.key}=${envVar.value}"}).join("\n"))
        this.script.echo("---< Done. >---")
    }
}

class ActiveChoicesReactiveParameter {
    //! Note: These below properties already have getters, setters
    /* For example:
            getName,         setName         for property 'name'
            getRefNames,     setRefNames     for property 'refNames'
            getSelectType,   setSelectType   for property 'selectType'
            getDescription,  setDescription  for property 'description'
            getFilterEnable, setFilterEnable for property 'filterEnable'
            getGroovyScript, setGroovyScript for property 'groovyScript'
    */
    def script
    def parameterDefinitionObj
    String name
    String refNames
    String selectType
    String description
    boolean filterEnable
    String groovyScript

    def setSingleSelect() {
        this.selectType = "PT_SINGLE_SELECT"
    }

    def setMultiSelect() {
        this.selectType = "PT_MULTI_SELECT"
        this.description = this.description + ".<br> Hold Ctrl and click to select multiple item"
    }

    def setCheckboxSelect() {
        this.selectType = "PT_CHECKBOX"
    }

    def setRadioSelect() {
        this.selectType = "PT_RADIO"
    }

    def setStaticChoicesList(valueList) {
        this.groovyScript = """\
        return ${valueList.collect {"\"${it}\""}}
        """
    }

    def getValue() {
        def refParamNameWithValue = [:]
        for (refName in this.refNames.tokenize(",")) {
            def trimmedRefName = refName.trim()
            refParamNameWithValue."${trimmedRefName}" = this.script.env."${trimmedRefName}"
        }

        def returnValue = this.parameterDefinitionObj."${this.name}"."value"
        for (refParamName in refParamNameWithValue.keySet()) {
            if (this.parameterDefinitionObj.get(refParamName) == null) {
                this.script.echo("[ERROR] The referenced parameter named \"${refParamName}\" doesn't exists in hjson database. Obtained value show as below")
                this.script.echo(prettyPrint(toJson(returnValue)))
                return null
            }
            if (returnValue.get(refParamNameWithValue[refParamName]) == null) {
                this.script.echo("[ERROR] The parameter named \"${this.name}\" doesn't have a key \"${refParamNameWithValue[refParamName]}\". Obtained value show as below")
                this.script.echo(prettyPrint(toJson(returnValue)))
                return null
            }
            returnValue = returnValue.get(refParamNameWithValue[refParamName])
        }
        return returnValue
    }

    def extend(valueList) {
        def tempGroovyScript = getGroovyScript()
        tempGroovyScript  = tempGroovyScript.replace("return resultList", "return [*resultList, *[\"${valueList.join('", "')}\"]]")
        setGroovyScript(tempGroovyScript)
    }

    def composeParameterDefinition() {
        def option = [$class            : 'CascadeChoiceParameter',
                    choiceType          : "${this.selectType}",
                    description         : "${this.description}",
                    filterLength: 1, 
                    filterable: this.filterEnable,
                    name                : "${this.name}",
                    referencedParameters: "${this.refNames}",
                    script              : [$class: 'GroovyScript',
                        fallbackScript: [
                            classpath: [],
                            sandbox: true,
                            script: 'return ["ERROR! Please check scriptApproval"]'
                        ],
                        script: [
                            classpath: [],
                            sandbox: false,
                            script: """
                                ${this.groovyScript}
                            """.stripIndent()
                        ]
                    ]
                ]
        return option
    }
}

class ActiveChoicesReactiveReferenceParameter extends ActiveChoicesReactiveParameter {
    def composeParameterDefinition() {
        def option = [$class            : 'DynamicReferenceParameter',
                    choiceType          : "${this.selectType}",
                    description         : "${this.description}",
                    name                : "${this.name}",
                    referencedParameters: "${this.refNames}",
                    script: [
                        $class: 'GroovyScript',
                        fallbackScript: [
                            classpath: [],
                            sandbox: true,
                            script: 'return ["ERROR! Please check scriptApproval"]'
                        ],
                        script: [
                            classpath: [],
                            sandbox: false,
                            script: """
                                    ${this.groovyScript}
                                """.stripIndent()
                        ]
                    ],
                    omitValueField: true
                ]
        return option
    }
}

class SimpleParameter {
    String className
    String name
    def defaultValue
    String description

    SimpleParameter(className, name, defaultValue, description) {
        this.className = className
        this.name = name
        this.defaultValue = defaultValue
        this.description = description
    }

    def composeParameterDefinition() {
        def option = [$class    : "${className}",
                    name        : "${this.name}",
                    defaultValue: this.defaultValue,
                    description : "${this.description}"
                ]
        return option
    }
}

class StringParameter extends SimpleParameter {
    StringParameter(name, defaultValue, description) {
        super("StringParameterDefinition", name, defaultValue, description)
    }
}

class BooleanParameter extends SimpleParameter {
    BooleanParameter(name, defaultValue, description) {
        super("BooleanParameterDefinition", name, defaultValue, description)
    }
}

/* define input hjson file */
def ParameterDefinitionFileP_hjson = "${env.libPath}\\ParameterCollection\\configs\\ParameterDefinition.hjson"

/* define output hjson file */
def ParameterDefinitionFileP_json = "${env.libPath}\\ParameterCollection\\logs\\ParameterDefinition.json"

/* define path of the tool convert hjson to json */
def hjson_to_json_tool_p = "${env.libPath}\\ParameterCollection\\tools\\hjson.exe"

/* define the command convert hjson to json */
def command = "${hjson_to_json_tool_p} -j ${ParameterDefinitionFileP_hjson} > ${ParameterDefinitionFileP_json}"
bat(label: "Generate json from hjson", script: "${command}")

def parameterDefinitionObj = readJSON(file: "${ParameterDefinitionFileP_json}")

ParameterCollection parameterCollection =  new ParameterCollection(script: this)

for (paramName in parameterDefinitionObj.keySet()) {
    def className = parameterDefinitionObj."${paramName}"."className"
    // Check for mandatory jobs, if any
    def mandatoryValue
    try {
        mandatoryValue = parameterDefinitionObj."${paramName}"."onlyJobs"
    } catch(Exception ex) {
        // No need to exit job as this is just support checking
        mandatoryValue = null
    }
    // Common Env will true
    def required = true
    if (null != mandatoryValue){
        // This value will occur some jobs only.
        // set false first
        required = false
        mandatoryValue.each{name ->
            // if job name contain string defined at hjson -> true
            if (env.job_name.contains(name)) {
                required = true
            }
        }
    }
    if (['CascadeChoiceParameter', 'DynamicReferenceParameter'].contains(className)) {
        def constructor_map_prop = [
            script: this,
            parameterDefinitionObj: parameterDefinitionObj,
            name: paramName,
            refNames: parameterDefinitionObj."${paramName}"."refNames",
            selectType: parameterDefinitionObj."${paramName}"."selectType",
            description: parameterDefinitionObj."${paramName}"."description",
            filterEnable: parameterDefinitionObj."${paramName}"."filterEnable",
            groovyScript: parameterDefinitionObj."${paramName}"."groovyScript".replace("__placeholder_json_database__", "\$/${parameterDefinitionObj[paramName]."value".toString()}/\$")
        ]
        if (className == "CascadeChoiceParameter") {
            parameterCollection["${paramName}"] = new ActiveChoicesReactiveParameter(constructor_map_prop)
        } else if (className == "DynamicReferenceParameter") {
            parameterCollection["${paramName}"] = new ActiveChoicesReactiveReferenceParameter(constructor_map_prop)
        }
        if (parameterCollection["${paramName}"].getDescription() == "env") {
            // Add the value to environment variables
            if ("${this.env.RELOAD_JOB_PARAMETERS}" == "false" && required) {
                // only add value to environment variables when job running actually, not just load parameters
                this.env."${paramName}" = parameterCollection["${paramName}"].getValue()[0]
            }
        }
    } else if (className == 'StringParameterDefinition') {
        parameterCollection["${paramName}"] = new StringParameter(
            paramName,
            parameterDefinitionObj."${paramName}"."value", /* this is default value */
            parameterDefinitionObj."${paramName}"."description")
    } else if (className == 'BooleanParameterDefinition') {
        parameterCollection["${paramName}"] = new BooleanParameter(
            paramName,
            parameterDefinitionObj."${paramName}"."value", /* this is default value */
            parameterDefinitionObj."${paramName}"."description")
    }
}

// Set envinroment variable PYTHONPATH
this.env.PYTHONPATH = "${libPath.replace("/", "\\")}"

// Exclude paths from PATH env that impact to jenkins steps/commands
// TODO: Add more paths if any into the list below
def exclude_path_list = ["C:\\Python38\\Lib"]
this.env.PATH = this.env.PATH.split(";").minus(exclude_path_list).join(";")

return parameterCollection
