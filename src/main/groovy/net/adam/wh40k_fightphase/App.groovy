package net.adam.wh40k_fightphase

class App {
    static Unit newSquad(int number, File configFile) {
        def unitParser = new UnitParser()
        def config = unitParser.parse(configFile)
        return new Unit(number, config)
    }

    static void main(String[] args) {
        if (!args || args.size() != 4) {
            println "Usage:"
            println "wh40k_fightphase <unit 1 model count> <unit 1 model file> <unit 2 model count> <unit 2 model file>"
            return
        }

        int unit1Count = args[0] as int
        File unit1File = new File(args[1])
        int unit2Count = args[2] as int
        File unit2File = new File(args[3])

        Unit squadOne = newSquad(unit1Count, unit1File)
        Unit squadTwo = newSquad(unit2Count, unit2File)
        Attack attack = new Attack()

        boolean heads = new Dice().roll(1, 2) == 1

        Unit first
        Unit second
        if (heads) {
            first = squadOne
            second = squadTwo
        } else {
            first = squadTwo
            second = squadOne
        }

        boolean gameOver = false
        while(!gameOver) {
            attack.doFightPhase(first, second)
            gameOver = first.remainingModels() <= 0 || second.remainingModels() <= 0

            println "${first.name} - ${first.wound} wound"
            println "${second.name} - ${second.wound} wound"
            if (gameOver) { break }

            attack.doFightPhase(second, first)
            gameOver = first.remainingModels() <= 0 || second.remainingModels() <= 0

            println "${first.name} - ${first.wound} wound"
            println "${second.name} - ${second.wound} wound"

            Unit spectator = first
            first = second
            second = spectator
        }

        println squadOne.remainingModels() > 0 ? "Winner: ${squadOne.name}" : "Winner: ${squadTwo.name}"
    }
}
