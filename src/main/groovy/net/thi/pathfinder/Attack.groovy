package net.thi.pathfinder

class Attack { 
    CombatEngine engine

    Attack() {
        this(new CombatEngine()) 
    }

    Attack(CombatEngine engine) {
        this.engine = engine
    }

    /** Returns amount of attacks */
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

    /** Returns amount of damage that made it through feel no pain  */
    int feelNoPain(Unit defender, int incomingDamage) {
        int unitFNP = defender.attributes.feel_no_pain
        if (!unitFNP) {
            return incomingDamage
        }

        int trueDamage = incomingDamage
        incomingDamage.times {
            int FNP = engine.dice.roll (1, 6)
            if (FNP >= unitFNP) {
               trueDamage--
            }
        }
        return trueDamage
    }

    /** Deals damage to defending unit, taking overkill into account */ 
    void kill(Unit defender, int trueDamage) {
        if (trueDamage == 0) {
            return
        }

        int minimumWound = defender.attributes.wound * Math.floor((defender.wound - 1) / defender.attributes.wound)
        int maximumWound = defender.wound - trueDamage
        int actualWound = minimumWound > maximumWound ? minimumWound : maximumWound

        defender.wound = actualWound
    }
} 
