package org.redsxi.transitplus.client.render.rail

import com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR
import com.mojang.blaze3d.vertex.VertexFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.world.level.Level
import org.redsxi.transitplus.client.network.NetworkClient
import org.redsxi.transitplus.client.render.RenderContext
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail

class RailRenderContext(var rail: ChunkRail?, val position: ChunkPos, val currentWorld: Level) {
    fun draw(ctx: RenderContext) {
        if(rail == null) {
            CoroutineScope(Dispatchers.IO).launch {
                rail = NetworkClient.getChunkRail(position, currentWorld)
            }
        } else {
            ctx.render {
                begin(VertexFormat.Mode.DEBUG_LINE_STRIP, POSITION_COLOR)

                end()
            }
        }
    }
}