    options {
        skipDefaultCheckout()
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    sh "echo ${env.gitlabSourceNamespace}/${env.gitlabSourceBranch}" 
                    if (env.gitlabActionType == 'MERGE') {
                        // building merge requests
                        checkout changelog: true, poll: true, scm: [
                                $class                           : 'GitSCM',
                                branches                         : [[name: "origin/merge-requests/${env.gitlabMergeRequestIid}"]],
                                doGenerateSubmoduleConfigurations: false,
                                extensions                       : [[$class: 'PreBuildMerge', options: [fastForwardMode: 'FF_ONLY', mergeRemote: 'origin', mergeStrategy: 'DEFAULT', mergeTarget: "${env.gitlabTargetBranch}"]]],
                                submoduleCfg                     : [],
                                userRemoteConfigs                : [[
                                                                            credentialsId: 'cLQ9ZXSTc6Xk9bawYr9u', url: "${env.gitlabTargetRepoHttpUrl}",
                                                                            refspec      : '+refs/heads/*:refs/remotes/origin/* +refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*'
                                                                    ]],
                        ]
                    } else if (env.gitlabSourceBranch != null) {
                        // merge the branch to master and build it
                        checkout changelog: true, poll: true, scm: [ 
                                $class                           : 'GitSCM',
                                branches                         : [[name: "origin/${env.gitlabSourceBranch}"]],
                                doGenerateSubmoduleConfigurations: false,
                                extensions                       : [[$class: 'PreBuildMerge', options: [fastForwardMode: 'FF_ONLY', mergeRemote: 'origin', mergeStrategy: 'DEFAULT', mergeTarget: "${env.gitlabTargetBranch}"]]],
                                submoduleCfg                     : [],
                                userRemoteConfigs                : [[credentialsId: 'cLQ9ZXSTc6Xk9bawYr9u', url: "${env.gitlabSourceRepoHttpUrl}"]],
                        ]

                    } else {
                        // default checkout when build now button is pressed.
                        checkout(scm)
                    }
                }

            }
        }