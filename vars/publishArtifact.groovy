#!/usr/bin/env groovy 

def call(Map param) {
  param.remote.allowAnyHosts = true
  if ("${param.command}") {
  sshCommand remote: param.remote, command: "${param.command}"
  }
  if ("${param.artifactFrom}") {
  sshRemove remote: param.remote, path: "${param.artifactTo}"
  sshPut remote: param.remote, from: "${param.artifactFrom}", into: "${param.artifactTo}"
  } 
}