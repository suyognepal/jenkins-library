#!/usr/bin/env groovy

def call(Map param) {
    def ROOT = "${param.artifactTo}".minus(".war")
    param.remote.allowAnyHosts = true
    def check = sh(script: "test -d ${ROOT} || test -f ${ROOT}", returnStatus:true)
    printf "${check}"
    if ("${check}" == "1") {
        sshRemove remote: param.remote, path: "${param.artifactTo}", failOnError: true
    }
    if ("${param.propsFrom}") {
        sshPut remote: param.remote, from: "${param.propsFrom}", into: "${param.propsTo}"
        sshPut remote: param.remote, from: "${param.artifactFrom}", into: "${param.artifactTo}"
    //    sshCommand remote: param.remote, command: "bash /home/restart_tom.sh ${env.server_username}"
    } else {
    //   sshCommand remote: param.remote, command: "bash ${param.tomcat_location}/restart_tom.sh ${env.module}"
        sshPut remote: param.remote, from: "${param.artifactFrom}", into: "${param.artifactTo}"
    }
}