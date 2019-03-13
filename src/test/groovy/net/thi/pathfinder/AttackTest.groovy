package net.thi.pathfinder

import spock.lang.Specification

class AttackTest extends Specification {
    Unit newSquad(int number, String name) {
        def unitParser = new UnitParser()
        def config = unitParser.parse(this.getClass().getResource("${name}.json"))
        return new Unit(number, config)
    }

    Attack newAttack(int seed) {
        def dice = new Dice(seed)
        def combat = new CombatEngine(dice)
        return new Attack(combat)
    }

    def "number_of_attacks"() {
        setup:
        // [unit, count, damageTaken, expectedAttacks]
        def tests = [
            ["vigilator", 10,  0, 21],
            ["vigilator", 10,  6,  9],
            ["vigilator", 10, 11,  0],
            ["seeker",    10,  1, 21], //TODO: 41 (include steeds)
            ["seeker",     5,  4,  7],
            ["seeker",     5, 10,  0],
            ["seeker",     5, 16,  0],
        ]

        tests.each { test ->
            when:
            def squad = newSquad(test[1], test[0])
            squad.wound -= test[2]
            def numberOfAttacks = new Attack().numberOfAttacks(squad)

            then:
            assert numberOfAttacks == test[3]
        }
    }

    def "attack_roll"() {
        setup:
        def vigilators = newSquad(10, "vigilator")
        def attack = newAttack(1337)

        when:
        def result = attack.attackRoll(vigilators)

        then:
        assert result == 13
    }
}
