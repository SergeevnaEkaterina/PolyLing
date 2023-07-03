task_branch = "${BRANCH_NAME}"
buildPath = "C:\\Users\\geras\\IdeaProjects\\PolyLing"
def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()
currentBuild.displayName = "$branch_cutted"
base_git_url = "https://github.com/SergeevnaEkaterina/PolyLing.git"


node {
    withEnv(["branch=${branch_cutted}", "base_url=${base_git_url}"]) {
        stage("Checkout Branch") {
            if (!"$branch_cutted".contains("master")) {
                try {
                    getProject("$base_git_url", "$branch_cutted")
                } catch (err) {
                    echo "Failed get branch $branch_cutted"
                    throw ("${err}")
                }
            } else {
                echo "Current branch is master"
            }
        }
        dir(buildPath) {
            stage("Clean") {
                bat "mvn clean"
            }


            try {
                stage("Smoke tests run") {

                    bat 'mvn test -D\$cucumber.filter.tags=@smoke'


                }
                stage("Tests run") {
                    parallel firstBranch: {
                        bat 'mvn test -D\$cucumber.filter.tags=@chrome'
                    }, secondBranch: {
                        bat 'mvn test -D\$cucumber.filter.tags=@firefox'
                    }, thirdBranch: {
                        bat 'mvn test -D\$cucumber.filter.tags=@edge'
                    },  fourthBranch: {
                        bat 'mvn test -D\$cucumber.filter.tags=@functional'
                    },
                            failFast: false

                }
            } finally {
                stage("Generate report") {
                    bat "mvn site"
                }
                stage("Allure") {
                    generateAllure()
                }



            }
        }

    }



}

def getProject(String repo, String branch) {
    cleanWs()
    checkout scm: [
            $class           : 'GitSCM', branches: [[name: branch]],
            userRemoteConfigs: [[
                                        url: repo
                                ]]
    ]
}

def getTestStages(testTags) {
    def stages = [:]
    testTags.each { tag ->
        stages["${tag}"] = {
            runTestWithTag(tag)
        }
    }
    return stages
}


def runTestWithTag(String tag) {
    try {
        bat "mvn test -P ${tag}"
    } finally {
        echo "some failed tests"
    }
}


def generateAllure() {
    allure([
            includeProperties: true,
            jdk              : '',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'target/allure-results']]
    ])
}