package org.redsxi.transitplus.server

import mtr.data.Rail
import org.redsxi.transitplus.server.mixin.RailAccessor

fun Rail.accessor() = this as RailAccessor