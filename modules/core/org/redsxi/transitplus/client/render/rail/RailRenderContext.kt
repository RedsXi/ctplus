package org.redsxi.transitplus.client.render.rail

import kotlinx.coroutines.runBlocking
import net.minecraft.world.level.Level
import org.redsxi.transitplus.client.render.RenderContext
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.server.network.NetworkServer
import java.awt.Color
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Deprecated("")
class RailRenderContext(val position: ChunkPos, val currentWorld: Level) {

    init {
        start()
    }

    fun draw(ctx: RenderContext) {
        if (cachedChunkRailData[Pair(position, currentWorld)] != null) {
            // Retrieved
            ctx.drawRect(position.posX.toFloat(), position.posY.toFloat(), 16f, 16f, 0xFF0000FF.toInt())
        } else if (requestQueue.contains(Pair(position, currentWorld))) {
            // Retrieving
            ctx.drawRect(position.posX.toFloat(), position.posY.toFloat(), 16f, 16f, 0xFF000080.toInt())
        } else {
            queueRequest(currentWorld, position)
            ctx.drawRect(position.posX.toFloat(), position.posY.toFloat(), 16f, 16f, Color(position.posX and 0xFF,0,position.posY and 0xFF,255).rgb)
        }



    }

    companion object {
        var running = false

        fun start() {
            if(running) return
            running = true
            thread.start()
        }
        fun stop() {
            running = false
        }

        val requestQueue: Queue<Pair<ChunkPos, Level>> = LinkedList()
        val cachedChunkRailData = ConcurrentHashMap<Pair<ChunkPos, Level>, ChunkRail>()

        val thread = Thread({
            while(running) {
                val pair = requestQueue.poll()
                if(pair != null) {
                    runBlocking {
                        cachedChunkRailData[pair] = NetworkServer.getChunkRail(pair.first, pair.second)
                    }
                }
            }
        }, "ChunkRailRendererThread")

        fun queueRequest(world: Level, pos: ChunkPos) {
            requestQueue += Pair(pos, world)
        }
    }
}