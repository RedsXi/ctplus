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

    fun getRail(start: BlockPos, end: BlockPos): Rail?
        = rails(ChunkPos.fromBlock(start)).rails[Pair(start, end)]

    private fun appendRail0(start: BlockPos, end: BlockPos, rail: Rail) {
        rails(ChunkPos.fromBlock(start)).rails[Pair(start, end)] = rail
    }

    fun appendRail(start: BlockPos, end: BlockPos, rail: Rail) {
        val rR = getRail(end, start)

        if (rR == null) {
            rail.forward()
            appendRail0(start, end, rail)
        } else {
            rR.backward()
        }
    }

    fun removeRail(start: BlockPos, end: BlockPos) {
        val rR = getRail(end, start)
        if (rR == null) {
            getRail(start, end)?.removeForward()
        } else {
            rR.removeBackward()
        }
    }

    fun removeNode(pos: BlockPos) {
        rails.values.forEach { cr ->
            val rails = cr.rails
            rails.keys.forEach { key ->
                if (key.first == pos || key.second == pos) rails.remove(key)
            }
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