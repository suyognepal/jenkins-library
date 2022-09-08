#!/usr/bin/env groovy

def call() {
    if (env.gitlabMergeRequestId) {
        sh "echo 'Merge request detected. Merging...'"
        def credentialsId = scm.userRemoteConfigs[0].credentialsId
        checkout ([
            $class: 'GitSCM',
            //sh "echo ${env.gitlabSourceNamespace}/${env.gitlabSourceBranch}" 
            //branches: branches: [[name: "*/${Branch}"]]
            //branches: [[name: "${env.gitlabSourceNamespace}/${env.gitlabSourceBranch}"]],
            //branches: [[name: "origin/merge-requests/${env.gitlabMergeRequestId}"]],
            branches: [[name: "refs/heads/${env.gitlabSourceBranch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [[$class: 'PreBuildMerge', $class: 'PruneStaleBranch', $class: 'CleanCheckout', options: [fastForwardMode: 'NO_FF', mergeRemote: "${env.gitlabSourceNamespace}/${env.gitlabSourceBranch}", mergeStrategy: 'DEFAULT', mergeTarget: "${env.gitlabTargetBranch}"]]],            
            submoduleCfg: [],               
           /*
           extensions: [
                [$class: 'PruneStaleBranch'],
                [$class: 'CleanCheckout'],
                [
                    $class: 'PreBuildMerge',
                    options: [
                        fastForwardMode: 'NO_FF',
                        mergeRemote: env.gitlabTargetNamespace,
                        mergeTarget: env.gitlabTargetBranch
                    ]
                ]
            ],
            */
             userRemoteConfigs: [
                [
                    credentialsId: credentialsId,
                    name: env.gitlabTargetNamespace,
                    url: env.gitlabTargetRepoHttpURL,
                    //refspec: '+refs/heads/*:refs/remotes/origin/* +refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*'
                    // url: env.gitlabTargetRepoSshURL
                ],
                [
                    credentialsId: credentialsId,
                    name: env.gitlabSourceNamespace,
                    url: env.gitlabSourceRepoHttpURL,
                    //refspec: '+refs/heads/*:refs/remotes/origin/* +refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*'
                    // url: env.gitlabTargetRepoSshURL
                ]
            ]
        ])
    } else {
        sh "echo 'No merge request detected. Checking out current branch'"
        checkout ([
            $class: 'GitSCM',
            branches: scm.branches,
            extensions: [
                    [$class: 'PruneStaleBranch'],
                    [$class: 'CleanCheckout']
            ],
            userRemoteConfigs: scm.userRemoteConfigs
        ])
    }
}
