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
        def attacks = attack.attackRoll(vigilators)

        then:
        assert attacks == 13
    }

    def "wound_roll"() {
        setup:
        def vigilators = newSquad(10, "vigilator")
        def seekers = newSquad(10, "seeker")
        def attack = newAttack(1337)

        when:
        def wounds = attack.woundRoll(vigilators, seekers, 13)

        then:
        assert wounds == 8
    }

    def "save_roll"() {
        setup:
        def vigilators = newSquad(10, "vigilator")
        def seekers = newSquad(10, "seeker")
        def attack = newAttack(1337)

        when:
        def wounds = attack.saveRoll(vigilators, seekers, 8)

        then:
        assert wounds == 3
    }

    def "kill"() {
        setup:
        def attack = newAttack(1337)

        // unit, models, lost wounds, incoming damage, expectedWounds, expectedRemainingModels
        def tests = [
            ["vigilator", 10, 0,  0, 10, 10],
            ["vigilator", 10, 0,  1,  9,  9],
            ["vigilator", 10, 0,  2,  9,  9],
            ["vigilator", 10, 0, 10,  9,  9],
            ["vigilator", 10, 1,  1,  8,  8],
            ["vigilator", 10, 1,  2,  8,  8],
            ["seeker",    10, 0,  0, 20, 10],
            ["seeker",    10, 0,  1, 19, 10],
            ["seeker",    10, 0,  2, 18,  9],
            ["seeker",    10, 0, 10, 18,  9],
            ["seeker",    10, 1,  2, 18,  9],
            ["seeker",    10, 2,  2, 16,  8],
        ]

        tests.each { test ->
            when:
            Unit squad = newSquad(test[1], test[0])
            squad.wound -= test[2]
            attack.kill(squad, test[3])

            then:
            assert squad.wound == test[4]
            assert squad.remainingModels() == test[5]
        }
    }
}
