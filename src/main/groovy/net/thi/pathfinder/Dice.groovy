package net.thi.pathfinder

class Dice {
    Random rng = new Random()

    int roll (int dice, int sides) {
        int result = 0

        dice.times {
            result += rng.nextInt(sides) + 1
        }

        return result
    }
}