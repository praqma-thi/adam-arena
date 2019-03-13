package net.thi.pathfinder

import spock.lang.Specification

class UnitTest extends Specification {
    def "basic_unit_creation"() {
        setup:
        def unitParser = new UnitParser()
        def vigilator = unitParser.parse(this.getClass().getResource('vigilator.json'))

        when:
        def result = new Unit(10, vigilator)

        then:
        result.name == "Vigilator"
        result.cost == 150
        result.wound == 10
        result.attributes.weapon_skill == 3
        result.attributes.feel_no_pain == null
    }
}
