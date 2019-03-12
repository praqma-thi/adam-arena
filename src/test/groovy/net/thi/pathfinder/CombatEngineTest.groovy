package net.thi.pathfinder

import spock.lang.Specification

class CombatEngineTest extends Specification {
    def "roll_vs_weaponskill"() {
        setup:
        def engine = new CombatEngine()

        // [roll, weaponSkill, expectedResult]
        def tests = [
            [2, 3, false],
            [3, 3, true],
            [4, 3, true],
        ]

        tests.each { test ->
            when:
            def result = engine.hitRoll(test[0], test[1])

            then:
            result != null
            result == test[2]
        }
    }

    def "strength_vs_toughness"() {
        setup:
        def engine = new CombatEngine()

        // [strength, toughness, expectedResult]
        def tests = [
            [ 5, 10, 6],
            [ 4,  6, 5],
            [ 5,  5, 4],
            [ 7,  4, 3],
            [10,  5, 2],
        ]

        tests.each { test ->
            when:
            def result = engine.woundRoll(test[0], test[1])

            then:
            result != null
            result == test[2]
        }
    }
}
