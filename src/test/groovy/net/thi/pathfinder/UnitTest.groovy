package net.thi.pathfinder

import spock.lang.Specification

class UnitTest extends Specification {
    Unit newSquad(int number, String name) {
        def unitParser = new UnitParser()
        def config = unitParser.parse(this.getClass().getResource("${name}.json"))
        return new Unit(number, config)
    }

    def "remainingModels"() {
        setup:
        def unitParser = new UnitParser()

        // [model, modelCount, damage, expectedRemainingModels]
        def tests = [
            ["vigilator", 10,  0, 10],
            ["vigilator", 10,  6,  4],
            ["vigilator", 10, 11,  0],
            ["seeker",    10,  1, 10],
            ["seeker",     5,  4,  3],
            ["seeker",     5, 10,  0],
            ["seeker",     5, 16,  0],
        ]

        tests.each { test ->
            when:
            def squad = newSquad(test[1], test[0])
            squad.wound -= test[2]

            then:
            assert squad.remainingModels() == test[3]
        }
    }

    def "basic_unit_creation"() {
        setup:
        def unitParser = new UnitParser()
        def vigilator = unitParser.parse(this.getClass().getResource('vigilator.json'))

        when:
        def squad = new Unit(10, vigilator)

        then:
        squad.name == "Vigilator"
        squad.cost == 150
        squad.wound == 10
        squad.attributes.weapon_skill == 3
        squad.attributes.feel_no_pain == null
    }
}
