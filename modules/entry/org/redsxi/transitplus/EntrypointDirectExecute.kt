package org.redsxi.transitplus

import org.redsxi.transitplus.client.web.WebServer

fun main() {
    WebServer.start()
    val lock = ""
    synchronized(lock) {
        (lock as Object).wait()
    }
}