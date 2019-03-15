package net.adam.wh40k_fightphase

import groovy.json.JsonSlurper

class CombatEngine {
    Dice dice

    CombatEngine() {
        this(new Dice())
    }

    CombatEngine(Dice dice) {
        this.dice = dice
    }

    /** Calculates the damage after feel no pain takes effect */
    int feelNoPain(int damage, int feelNoPain) {
        if (feelNoPain == -1) {
            return damage
        }

        int totalDamage = damage
        damage.times {
            int roll = dice.roll(1, 6)
            if (rollHitsTarget(roll, feelNoPain)) {
                totalDamage--
            }
        }
        return totalDamage
    }

    /** Determines whether an attack hits or not */
    boolean rollHitsTarget(int roll, int target) {
        return roll >= target
    }

    /** Determines whether invulnerable saves are preferable */
    boolean shouldInvulnerableSave(int save, int invulnerableSave, int armourPierce) {
        if (invulnerableSave == -1) {
            return false
        }

        return save - armourPierce > invulnerableSave
    }

    /** Determines whether a save succeeds */
    boolean save(int roll, int save, int armourPierce) {
        return roll + armourPierce >= save
    }

    /** Determines the floor a wound roll must reach */
    int woundRollTarget(int strength, int toughness) {
        // If Strength is double or more than toughness then it's a 2+
        if (strength >= toughness * 2) {
            return 2
        }

        // if it's more then it's a 3+
        if (strength > toughness) {
            return 3
        }

        // if it's the same it's a 4+
        if (strength == toughness) {
            return 4
        }

        // if it's half or less it's a 6+ 
        if (strength <= toughness / 2) {
            return 6
        }

        // if it's less then it's 5+
        if (strength < toughness) {
            return 5
        }

        throw new Exception("Unexpected strength/toughness comparison result. (${strength} vs ${toughness})")
    }
}
