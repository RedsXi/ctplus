package org.redsxi.transitplus.server.core

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.redsxi.mc.ctplus.generated.RuntimeVariables
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.Rail
import org.redsxi.transitplus.common.getOrCreate
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.server.accessor

class RailwaySystem(val world: ServerLevel) {
    private val rails = HashMap<ChunkPos, ChunkRail>()

    fun rails(pos: ChunkPos) = rails.getOrCreate(pos, ChunkRail(pos))

    companion object {
        private val systems = HashMap<ServerLevel, RailwaySystem>()

        fun init(world: ServerLevel, rails: Map<BlockPos, Map<BlockPos, mtr.data.Rail>>): RailwaySystem { // start, end?
            val system = RailwaySystem(world)
            systems[world] = system
            val savedRail = HashMap<BlockPos, HashMap<BlockPos, Rail>>()
            rails.forEach { k ->
                val pos1 = k.key
                k.value.forEach { l ->
                    val pos2 = l.key
                    val railProto = l.value.accessor()
                    if(savedRail[pos2]?.containsKey(pos1) ?: false) {
                        val rail = savedRail[pos2]?.get(pos1) ?: throw IllegalStateException()
                        rail.backward()
                    } else {
                        val rail = Rail.read(railProto)
                        rail.forward()
                        (savedRail.getOrCreate(pos1, HashMap()))[pos2] = rail
                        system.rails(ChunkPos(0, 0)).rails += rail
                    }
                }
            }
            logger.info("Initialization of Railway System on world ${world.dimensionTypeId()} completed")
            return system
        }

        fun ServerLevel.railwaySystem() = systems[this] ?: throw IllegalStateException()
    }
}