package net.thi.pathfinder

import spock.lang.Specification

class CombatEngineTest extends Specification {
    def "feel_no_pain"() {
        setup:
        def dice = new Dice(1337)
        def engine = new CombatEngine(dice)

        when:
        def result = engine.feelNoPain(5, 6)

        then:
        assert result == 4
    }

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
            def result = engine.rollHitsTarget(test[0], test[1])

            then:
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
            def result = engine.woundRollTarget(test[0], test[1])

            then:
            result == test[2]
        }
    }

    def "normal_saves"() {
        setup:
        def engine = new CombatEngine()

        // [roll, save, armourPierce, expectedResult]
        def tests = [
            [ 3, 4, 0, false],
            [ 4, 4, 0,  true],
            [ 6, 4, 0,  true],
            [ 3, 4, 1, false],
            [ 5, 4, 1,  true],
            [ 4, 4, 1, false],
        ]

        tests.each { test ->
            when:
            def result = engine.save(test[0], test[1], test[2])

            then:
            result == test[3]
        }
    }

    def "decider_picks_saves"() {
        setup:
        def engine = new CombatEngine()

        // [save, invulnerableSave, armourPierce, expectedResult]
        def tests = [
            [ 4,  4, 0, false],
            [ 6,  5, 0,  true],
            [ 3,  5, 2, false],
            [ 3,  5, 1, false],
            [ 2,  4, 0, false],
            [ 2,  4, 4,  true],
            [ 4, -1, 0, false],
            [ 6, -1, 0, false],
            [ 6, -1, 6, false],
        ]

        tests.each { test ->
            when:
            def result = engine.shouldInvulnerableSave(test[0], test[1], test[2])

            then:
            result == test[3]
        }
    }
}
