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
import org.redsxi.transitplus.client.render.rail.RailRenderTask
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.Rail

object WebServer {
    val server = embeddedServer(CIO, 60000) {
        routing {
            get("/chunkZero") {
                val c = ChunkRail(ChunkPos(0, 0))
                call.respond(RailRenderTask.render(c))
            }
        }
    }

    fun start() {
        print("${rad2deg(Start)},${rad2deg(End)}")
        server.start()
    }

    fun stop() = server.stop()
}