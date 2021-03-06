package net.adam.wh40k_fightphase

class Unit {
    int count
    int cost
    int wound
    int maxWound

    Map<String, Object> config

    Unit(int count, Map<String, Object> config) {
        this.count = count
        this.config = config

        this.cost = count * config.cost
        this.maxWound = count * config.attributes.wound
        this.wound = maxWound
    }

    int remainingModels() {
        int damageTaken = maxWound - wound
        int remainingModels = Math.ceil((maxWound - damageTaken) / config.attributes.wound)
        return remainingModels < 0 ? 0 : remainingModels
    }

    Object propertyMissing(String name) {
        return config."$name"
    }
}
