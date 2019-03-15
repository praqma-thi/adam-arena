node ('docker') {
    stage ('build') {
        sh './gradlew clean build'
    }

    stage ('installDist') {
        sh './gradlew installDist'
    }

    stage ('lychguard vs hormagaunt') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 10 src/test/resources/net/adam/wh40k_fightphase/lychguard.json 60 src/test/resources/net/adam/wh40k_fightphase/hormagaunt.json"
        }
    }    

    stage ('lychguard vs vigilator') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 10 src/test/resources/net/adam/wh40k_fightphase/lychguard.json 20 src/test/resources/net/adam/wh40k_fightphase/vigilator.json"
        }
    }    

    stage ('bloodthirster vs stompa') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 1 src/test/resources/net/adam/wh40k_fightphase/bloodthirster.json 1 src/test/resources/net/adam/wh40k_fightphase/stompa.json"
        }
    }
}
