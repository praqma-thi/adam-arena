package net.thi.pathfinder

import spock.lang.Specification

class UnitParserTest extends Specification {
    def "parses_basic_stats"() {
        setup:
        def unitParser = new UnitParser()
        def file = this.getClass().getResource('vigilator.json')

        when:
        def result = unitParser.parse(file)

        then:
        result != null
        result.name == "Vigilator"
        result.attributes.wound == 1
        result.attacks.size() == 2
        result.attacks.first().name == "Executioner Greatblade"
    }
}
