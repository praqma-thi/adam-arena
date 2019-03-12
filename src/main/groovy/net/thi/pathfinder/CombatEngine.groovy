package net.thi.pathfinder

import groovy.json.JsonSlurper

class CombatEngine {
    /** Determines whether an attack hits or not */
    boolean hitRoll(int roll, int weaponSkill) {
        return roll >= weaponSkill
    }

    int woundRoll(int strength, int toughness) {
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
