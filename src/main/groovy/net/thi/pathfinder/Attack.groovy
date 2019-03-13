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

    /** Returns amount of outgoing wounds */
    int woundRoll(Unit attacker, Unit defender, int numberOfAttacks) {
        def weapon = attacker.weapons.first() // TODO: Allow for choosing weapons

        int strength = weapon.strength ?: attacker.attributes.strength
        int strengthBonus = weapon.strength_bonus ?: 0
        int toughness = defender.attributes.toughness
        int target = engine.woundRollTarget(strength, toughness)

        int woundRoll = 0
        numberOfAttacks.times {
            int roll = engine.dice.roll(1, 6)
            if (engine.rollHitsTarget(roll, target)) {
                woundRoll++
            }
        }
        return woundRoll
    }

    /** Returns amount of wounds that made it through saves */
    int saveRoll(Unit attacker, Unit defender, int wounds) {
        def weapon = attacker.weapons.first() // TODO: Allow for choosing weapons

        int save = defender.attributes.save ?: 0
        int invSave = defender.attributes.invulnerable_save ?: -1
        int ap = weapon.armour_piercing ?: 0

        boolean shouldInvSave = engine.shouldInvulnerableSave(save, invSave, ap)

        int incomingWounds = 0
        wounds.times {
            int roll = engine.dice.roll(1, 6)
            boolean saved = shouldInvSave ? engine.rollHitsTarget(roll, invSave) : engine.rollHitsTarget(roll, save + ap)
            if (!saved) {
                incomingWounds++
            }
        }
        return incomingWounds
    }
}
