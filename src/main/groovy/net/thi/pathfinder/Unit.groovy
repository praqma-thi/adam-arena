package net.thi.pathfinder

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

    Object propertyMissing(String name) {
        return config."$name"
    }
}
