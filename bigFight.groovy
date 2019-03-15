#! /usr/bin/env groovy
def result = [:]

String app = "./build/install/wh40k_fightphase/bin/wh40k_fightphase"

112.times {
    String output = "$app ${args[0]} ${args[1]} ${args[2]} ${args[3]}".execute().text
    String winner = (output =~ /Winner: (.*)/)[0][1]
    if (!result[winner]) { result[winner] = 0 }
    result[winner] += 1
    println "$winner: ${result[winner]}"
}

println "===== Final Results ====="
result.each { squad, wins ->
    println "$squad: $wins"
}
println "========================="
