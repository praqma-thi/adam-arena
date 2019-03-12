package net.thi.pathfinder

import groovy.json.JsonSlurper

class UnitParser {
    JsonSlurper json = new JsonSlurper()

    Map<String, Object> parse (URL url) {
        parse(new File(url.toURI()))
    }

    Map<String, Object> parse (File file) {
        return json.parse(file)
    }
}
