package net.adam.wh40k_fightphase

class Attack { 
    CombatEngine engine

    Attack() {
        this(new CombatEngine()) 
    }

    Attack(CombatEngine engine) {
        this.engine = engine
    }

    /** Run an entire fight phase */
    void doFightPhase(Unit attacker, Unit defender) {
        println "===== Start of [${attacker.name} attacks ${defender.name}] ====="
        int aRoll = attackRoll(attacker)
        int wRoll = woundRoll (attacker, defender, aRoll)
        int sRoll = saveRoll  (attacker, defender, wRoll)
        sRoll.times {
            int dRoll = damageRoll(attacker)
            int fnpRoll = feelNoPain(defender, dRoll)
            kill(defender, fnpRoll) //* THEM ALL*/
        }
        println "===== End of [${attacker.name} attacks ${defender.name}] ====="
    }

    /** Returns amount of attacks */
    int numberOfAttacks(Unit attacker) { 
        int extraLeaderAttack = 1
        if (attacker.attributes.no_leader == true || attacker.remainingModels() <= 0) {
            extraLeaderAttack = 0
        }
        int attacks = extraLeaderAttack + (attacker.attributes.attacks * attacker.remainingModels())
        println "[${attacker.name}] $attacks attacks"
        return attacks
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
        println "[${attacker.name}] $succeededRolls successful attack rolls"
        return succeededRolls
    }

    /** Returns amount of outgoing wounds */
    int woundRoll(Unit attacker, Unit defender, int numberOfAttacks) {
        def weapon = attacker.weapons.first() // TODO: Allow for choosing weapons

        int strength = weapon.strength ?: attacker.attributes.strength
        int strengthBonus = weapon.strength_bonus ?: 0
        int toughness = defender.attributes.toughness
        int target = engine.woundRollTarget(strength + strengthBonus, toughness)

        int woundRoll = 0
        numberOfAttacks.times {
            int roll = engine.dice.roll(1, 6)
            if (engine.rollHitsTarget(roll, target)) {
                woundRoll++
            }
        }

        println "[${attacker.name}] ${woundRoll} wounds"
        return woundRoll
    }

    /** Returns amount of wounds that made it through saves */
    int saveRoll(Unit attacker, Unit defender, int wounds) {
        def weapon = attacker.weapons.first() // TODO: Allow for choosing weapons

        int save = defender.attributes.save
        int invSave = defender.attributes.invulnerable_save ?: -1 
        int ap = weapon.armour_piercing ?: 0

        if (!save) { throw Exception("${defender.name} has no save.") }

        boolean shouldInvSave = engine.shouldInvulnerableSave(save, invSave, ap) 

        int incomingWounds = 0
        wounds.times {
            int roll = engine.dice.roll(1, 6)
            int target = shouldInvSave ? invSave : save - ap
            if (!engine.rollHitsTarget(roll, target)) {
                incomingWounds++
            }
        }

        println "[${attacker.name}] ${incomingWounds} wounds past save"
        return incomingWounds
    }

    /** Returns amount of damage */
    int damageRoll(Unit attacker) {
        def weapon = attacker.weapons.first() // TODO: Allow for choosing weapons

        if (weapon.damage) {
            println "[${attacker.name}] ${weapon.damage} damage"
            return weapon.damage
        }
        
        if (weapon.damage_roll) {
            int damage = engine.dice.roll(weapon.damage_roll)
            println "[${attacker.name}] ${damage} damage"
            return damage
        }

        throw new Exception("Weapon is inert, lacking both damage and damage roll.")
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

        println "[${defender.name}] ${trueDamage} damage past feel no pain"
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

        println "[${defender.name}] ${actualWound} wounds left"
        defender.wound = actualWound
    }

    int leadershipRoll(Unit defender, int deathsThisTurn) {
        int modelsFleeing = deathsThisTurn + engine.dice.roll(1, 6) - defender.attributes.leadership

        println "$defender.name ($deathsThisTurn dead) ${modelsFleeing} > ${defender.remainingModels()}"
        if (modelsFleeing > defender.remainingModels()) { 
            return defender.remainingModels()
        }

        if (modelsFleeing < 0) {
            return 0
        }

        return modelsFleeing
        // deal (1 * defender.attributes.wound) damage modelsFlee.times to defender    
    }

    void doLeadershipPhase(Unit defender, int modelsFleeing) {
        modelsFleeing.times {
            kill(defender, defender.attributes.wound)
        }
    }
}
