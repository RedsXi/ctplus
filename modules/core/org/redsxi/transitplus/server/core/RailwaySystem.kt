package org.redsxi.transitplus.server.core

import mtr.data.Rail
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.redsxi.mc.ctplus.generated.RuntimeVariables
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail

class RailwaySystem(val world: ServerLevel) {
    private val rails = HashMap<ChunkPos, ChunkRail>()

    fun rail(pos: ChunkPos) {

    }

    companion object {
        private val systems = HashMap<ServerLevel, RailwaySystem>()

        fun init(world: ServerLevel, rails: Map<BlockPos, Map<BlockPos, Rail>>) { // start, end?
            if(RuntimeVariables.DEBUG) {
                rails.forEach {
                    it.value.forEach { it2 ->

                    }
                }
            }
        }
    }
}