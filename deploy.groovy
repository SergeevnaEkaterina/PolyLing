buildPath = "C:\\Users\\geras\\poly"


node {
    dir(buildPath) {
          stage("Create dirs") {
             bat "mkdir files"
             bat "mkdir software"
             bat "mkdir web-app"
         }
         dir("C:\\Users\\geras\\poly\\software") {
         stage("Get web app") {

             checkout scmGit(branches: [[name: '*/devops']], extensions: [], userRemoteConfigs: [[credentialsId: '4c14cfde-dc53-4f14-8ac9-bec7ab7b9feb', url: 'https://git.spbpu.com/CustomProjects/spbstu/linguistics/software.git']])
         }
          }
         dir("C:\\Users\\geras\\poly\\web-app") {
           stage("Get network app") {

             checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: '4c14cfde-dc53-4f14-8ac9-bec7ab7b9feb', url: 'https://git.spbpu.com/CustomProjects/spbstu/linguistics/web-app.git']])
         }
         }
         stage("Delete temp dirs") {
             bat "rmdir web-app@tmp"
             bat "rmdir software@tmp"
         }
        stage("Run polyling") {
            bat "docker-compose up -d"
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

