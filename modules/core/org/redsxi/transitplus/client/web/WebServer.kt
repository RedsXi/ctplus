package org.redsxi.transitplus.client.web

import io.ktor.http.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.minecraft.core.BlockPos
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NbtUtils
import org.redsxi.transitplus.client.render.Temporary.End
import org.redsxi.transitplus.client.render.Temporary.Start
import org.redsxi.transitplus.client.render.rail.RailRenderTask
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.Rail
import java.io.PrintStream
import javax.imageio.ImageIO

@Deprecated("")
object WebServer {
    val server = embeddedServer(CIO, 60000) {
        routing {
            get("chunkZero") {
                val c = ChunkRail(ChunkPos(0, 0))
                val nbt = NbtUtils.snbtToStructure("{Start: {R: 1.83697019872103E-15d, Start: -15.0d, H: -1.0d, End: -14.999999999999998d, K: -1.2246467991473532E-16d, Straight: 1b, Reverse: 0b}, End: {R: 15.0d, Start: -23.561944901923447d, H: 14.999999999999996d, End: -47.12388980384689d, K: 15.0d, Straight: 0b, Reverse: 1b}, Direction: 3}")
                val rail = Rail.CODEC.decode(NbtOps.INSTANCE, nbt).result().get().first
                c.rails[Pair(BlockPos(0,0,0), BlockPos(0,0,0))] = rail
                val image = RailRenderTask.render(c)
                call.respondOutputStream {
                    val po = PrintStream(this)
                    for (i in image) {
                        for (v in i) {
                            po.println("(${v.first}, ${v.second})")
                        }
                        po.println()
                    }
                }
            }
        }
    }

    fun start() {
        println("${StrictMath.atan2(StrictMath.cos(Start), StrictMath.sin(Start)) / StrictMath.PI * 180.0}")
        server.start()
    }

    fun stop() = server.stop()
}