package org.redsxi.transitplus.server.core

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.Rail
import org.redsxi.transitplus.common.getOrCreate
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.server.accessor

class RailwaySystem(val world: ServerLevel) {
    private val rails = HashMap<ChunkPos, ChunkRail>()

    fun rails(pos: ChunkPos) = rails.getOrCreate(pos, ChunkRail(pos))

    fun appendRail(start: BlockPos, end: BlockPos, rail: Rail) {
        val rails = rails(ChunkPos.fromBlock(start))
        val railsR = rails(ChunkPos.fromBlock(start))

        val rR = railsR.rails[Pair(end, start)]
        if(rR != null) {
            rR.backward()
        } else {
            rail.forward()
            rails.rails[Pair(start, end)] = rail
        }
    }

    companion object {
        private val systems = HashMap<ServerLevel, RailwaySystem>()

        fun init(world: ServerLevel, rails: Map<BlockPos, Map<BlockPos, mtr.data.Rail>>): RailwaySystem { // start, end?
            val system = RailwaySystem(world)
            systems[world] = system
            rails.forEach { k ->
                val pos1 = k.key
                k.value.forEach { l ->
                    val pos2 = l.key
                    val rail = Rail.read(l.value.accessor())
                    system.appendRail(pos1, pos2, rail)
                }
            }
            logger.info("Initialization of Railway System on world ${world.dimensionTypeId()} completed")
            return system
        }

        fun ServerLevel.railwaySystem() = systems[this] ?: throw IllegalStateException()
    }
}