package org.redsxi.transitplus.server.data

import mtr.data.RailwayData
import net.minecraft.server.level.ServerLevel
import org.redsxi.transitplus.server.mixin.accessor

object Railway {

    fun load(level: ServerLevel) {
        val data = RailwayData.getInstance(level)
        data.accessor().rails()
    }
}