package org.redsxi.transitplus

import org.redsxi.transitplus.client.web.WebServer

fun main() {
    WebServer.start()
    (WebServer as Object).wait()
}