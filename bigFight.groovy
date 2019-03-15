#! /usr/bin/env groovy
def result = [:]

String app = "./build/install/wh40k_fightphase/bin/wh40k_fightphase"

112.times {
    String output = "$app ${args[0]} ${args[1]} ${args[2]} ${args[3]}".execute().text
    def matcher = (output =~ /Winner: (.*)/)
    String winner = matcher.size() > 0 && matcher[0].size() > 1 ? matcher[0][1] : "Draw"
    if (!result[winner]) { result[winner] = 0 }
    result[winner] += 1
    println "$winner: ${result[winner]}"
}

println "===== Final Results ====="
result.each { squad, wins ->
    println "$squad: $wins"
}
println "========================="
