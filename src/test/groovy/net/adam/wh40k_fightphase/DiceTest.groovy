package net.adam.wh40k_fightphase

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


    def "text_rolls"() {
        setup:
        def dice = new Dice(1337)

        // [input, expectedResult]
        def tests = [
            ["1D6",    2],
            ["2+1D6",  3],
            ["5+5D6", 31],
            ["1D3",    2],
            ["2+1D3",  4],
            ["1D3-1",  0],
            ["1D3-5", -3],
        ]

        tests.each { test ->
            when:
            int result = dice.roll(test[0])

            then:
            assert result == test[1]
        }
    }
}
