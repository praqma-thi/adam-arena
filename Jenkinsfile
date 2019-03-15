node ('docker') {
    stage ('checkout') {
        checkout scm
    }

    stage ('build') {
        sh './gradlew clean build'
    }

    stage ('installDist') {
        sh './gradlew installDist'
    }

    stage ('rakah vs hormagaunt') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 1 src/test/resources/net/adam/wh40k_fightphase/bloodthirster_sweep.json 60 src/test/resources/net/adam/wh40k_fightphase/hormagaunt.json"
        }
    }

    stage ('lychguard vs hormagaunt') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 10 src/test/resources/net/adam/wh40k_fightphase/lychguard.json 56 src/test/resources/net/adam/wh40k_fightphase/hormagaunt.json"
        }
    }

    stage ('lychguard vs vigilator') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 10 src/test/resources/net/adam/wh40k_fightphase/lychguard.json 20 src/test/resources/net/adam/wh40k_fightphase/vigilator.json"
        }
    }

    stage ('lychguard vs rakah smash') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 10 src/test/resources/net/adam/wh40k_fightphase/lychguard.json 1 src/test/resources/net/adam/wh40k_fightphase/bloodthirster.json"
        }
    }

    stage ('lychguard vs rakah sweep') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 10 src/test/resources/net/adam/wh40k_fightphase/lychguard.json 1 src/test/resources/net/adam/wh40k_fightphase/bloodthirster_sweep.json"
        }
    }

    stage ('rakah vs stompa') {
        docker.image('groovy:2.5-jre8').inside {
            sh "./bigFight.groovy 1 src/test/resources/net/adam/wh40k_fightphase/bloodthirster.json 1 src/test/resources/net/adam/wh40k_fightphase/stompa.json"
        }
    }
}
