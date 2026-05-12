@file:JvmName("MainCommon")

package org.redsxi.transitplus.common

import mtr.data.RailwayData
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.redsxi.transitplus.common.network.Network
import org.redsxi.transitplus.server.accessor
import org.redsxi.transitplus.server.core.RailwaySystem

fun entry() {
    Network.global()
    ServerLifecycleEvents.SERVER_STARTED.register {
        val overworld = it.overworld()
        RailwaySystem.init(overworld, RailwayData.getInstance(overworld).accessor().rails())
    }
}