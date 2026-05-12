package org.redsxi.transitplus.server

import mtr.data.Rail
import mtr.data.RailwayData
import org.redsxi.transitplus.server.mixin.RailAccessor
import org.redsxi.transitplus.server.mixin.RailwayDataAccessor

fun RailwayData.accessor() = this as RailwayDataAccessor
fun Rail.accessor() = this as RailAccessor