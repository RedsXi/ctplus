package org.redsxi.mc.ctplus.util

import mtr.data.RailwayData
import mtr.data.Station
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import java.util.Optional

object MTROptionalData {
    @JvmStatic
    fun getRailwayData(level: Level): Optional<RailwayData> {
        val data = RailwayData.getInstance(level) ?: return Optional.empty()
        return Optional.of(data)
    }

    @JvmStatic
    fun getStation(railwayData: Optional<RailwayData>, position: BlockPos): Optional<Station> {
        if (!railwayData.isPresent) return Optional.empty()
        val data = railwayData.get()
        val station = RailwayData.getStation(data.stations, data.dataCache, position) ?: return Optional.empty()
        return Optional.of(station)
    }
}