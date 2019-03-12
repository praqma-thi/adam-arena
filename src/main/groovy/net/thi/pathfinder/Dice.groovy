package net.thi.pathfinder

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
}