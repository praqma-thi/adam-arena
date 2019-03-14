package net.thi.pathfinder

class App {
    static Unit newSquad(int number, String name) {
        def unitParser = new UnitParser()
        def config = unitParser.parse(new File("src/test/resources/net/thi/pathfinder/${name}.json"))
        return new Unit(number, config)
    }

    static void main(String[] args) {
        Unit squadOne = newSquad(1, "bloodthirster")
        Unit squadTwo = newSquad(1, "stompa")
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
            if (gameOver) { return }

            attack.doFightPhase(second, first)
            gameOver = first.remainingModels() <= 0 || second.remainingModels() <= 0

            println "${first.name} - ${first.wound} wound"
            println "${second.name} - ${second.wound} wound"

            Unit spectator = first
            first = second
            second = spectator
        }
    }
}
