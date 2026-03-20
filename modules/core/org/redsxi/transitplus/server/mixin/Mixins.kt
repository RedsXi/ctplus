package org.redsxi.transitplus.server.mixin

import mtr.data.Rail
import mtr.data.RailwayData

fun RailwayData.accessor() = this as RailwayDataAccessor
fun Rail.accessor() = this as RailAccessor