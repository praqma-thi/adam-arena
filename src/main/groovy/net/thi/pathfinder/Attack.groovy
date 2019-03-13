package net.thi.pathfinder

class Attack {
    CombatEngine engine

    Attack() {
        this(new CombatEngine())
    }

    Attack(CombatEngine engine) {
        this.engine = engine
    }

    int numberOfAttacks(Unit attacker) {
        int extraLeaderAttack = attacker.remainingModels() > 0 ? 1 : 0
        return extraLeaderAttack + (attacker.attributes.attacks * attacker.remainingModels())
    }

    /** Returns amount of succeeded attacks */
    int attackRoll(Unit attacker) {
        int succeededRolls = 0
        int attacks = numberOfAttacks(attacker)
        attacks.times {
            int roll = engine.dice.roll(1, 6)
            int target = attacker.attributes.weapon_skill
            if (engine.rollHitsTarget(roll, target)) {
                succeededRolls++
            }
        }
        return succeededRolls
    }

    /** Returns amount of damage dealt */
}
