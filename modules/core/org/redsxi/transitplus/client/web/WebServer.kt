package org.redsxi.transitplus.client.web

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.redsxi.transitplus.client.render.Temporary
import org.redsxi.transitplus.client.render.Temporary.End
import org.redsxi.transitplus.client.render.Temporary.Start
import org.redsxi.transitplus.client.render.Temporary.rad2deg
import org.redsxi.transitplus.client.render.Temporary.radLimit

object WebServer {
    val server = embeddedServer(CIO, 60000) {
        routing {
            get("/chunkZero") {
                call.respond(Temporary.image)
            }
        }
    }

    fun start() {
        print("${rad2deg(Start)},${rad2deg(End)}")
        server.start()
    }

    fun stop() = server.stop()
}