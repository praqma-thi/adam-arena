package net.adam.wh40k_fightphase

import org.codehaus.groovy.control.CompilerConfiguration

class Dice {
    Random rng

    Dice() {
        rng = new Random()
    }

    Dice(int seed) {
        rng = new Random(seed)
    }

    int roll (int dice, int sides) {
        int result = 0
        dice.times {
            result += rng.nextInt(sides) + 1
        }
        return result
    }
 
    int roll (String input) {
        String diceMatch = /(\d+)D(\d+)/
        def matches = input =~ diceMatch
        String flat = input.replaceAll(/(\d+)D(\d+)/, '')

        int total = 0
        matches.each { match ->
            total += roll(match[1] as int, match[2] as int)
        }

        total = Eval.me("${flat} + ${total}")
        return total
    }
}
