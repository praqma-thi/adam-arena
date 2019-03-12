package net.thi.pathfinder

import spock.lang.Specification

class DiceTest extends Specification {
    def "1d6_rolls_within_bounds"() {
        setup:
        def dice = new Dice()

        when:
        def result = dice.roll(1, 6)

        then:
        result != null
        result > 0
        result < 7
    }

    def "1d3_rolls_within_bounds"() {
        setup:
        def dice = new Dice()

        when:
        def result = dice.roll(1, 3)

        then:
        result != null
        result > 0
        result < 4
    }


    def "2d6_rolls_within_bounds"() {
        setup:
        def dice = new Dice()

        when:
        def result = dice.roll(2, 6)

        then:
        result != null
        result > 1
        result < 13
    }
}
