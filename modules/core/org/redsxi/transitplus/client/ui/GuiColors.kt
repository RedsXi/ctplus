package org.redsxi.transitplus.client.ui

import org.redsxi.mc.ctplus.generated.RuntimeVariables

const val black: Int = 0xFF000000.toInt()
const val white: Int = 0xFFFFFFFF.toInt()
const val bg: Int = 0xFF000000.toInt()
const val cyan: Int = 0xFF008080.toInt()

const val yellow: Int = 0xFFFFFF00.toInt()
const val transparent: Int = 0

val debug = if ( RuntimeVariables.DEBUG ) yellow else transparent