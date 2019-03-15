package net.adam.wh40k_fightphase

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

    def "play_attack_round"() {
        setup:
        Unit vigilators = newSquad(10, "vigilator")
        Unit lychguard = newSquad(5, "lychguard")
        Attack attack = newAttack(1337)

        when:
        attack.doFightPhase(vigilators, lychguard)
        
        then:
        assert lychguard.wound == 2
        assert lychguard.remainingModels() == 1

    }

    def "damageRoll"() {
        setup:
        Attack attack = newAttack(1337)

        // [unit, wound, expectedDamage]
        def tests = [
            ["vigilator", 2],
            ["seeker",    1],
            ["lychguard", 1]
        ]

        tests.each { test ->
            when:
            Unit squad = newSquad(10, test[0])
            int damage = attack.damageRoll(squad)

            then:
            assert damage == test[1]
        }
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
            ["lychguard",  5,  0, 10]
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

    def "#20: vigilators_save_on_one"() {
        setup:
        def vigilator = newSquad(1, "vigilator")
        def lychguard = newSquad(1, "lychguard")
        def attack = newAttack(1337)

        when:
        // First roll is 2, second is one
        attack.engine.dice.roll(1, 6)
        def wounds = attack.saveRoll(lychguard, vigilator, 1)

        then:
        assert wounds == 1
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

    def "feel_no_pain"() {
        setup:
        def attack = newAttack(1337)

        // unit, models, incoming damage, expectedRemainingDamage
        def tests = [
            ["vigilator", 10, 2,  2],
            ["seeker",    10, 3,  3],
            ["giant_chaos_spawn", 1, 10, 5]
        ]

        tests.each { test ->
            when:
            Unit squad = newSquad(test[1], test[0])
            int trueDamage = attack.feelNoPain(squad, test[2])

            then:
            assert trueDamage == test[3]
        }
    }
}
