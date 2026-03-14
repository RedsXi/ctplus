package org.redsxi.mc.ctplus.util

interface Time {
    val millis: Long
    val seconds: Long
    companion object : Time {
        override val millis: Long
            get() = System.currentTimeMillis()
        override val seconds: Long
            get() = millis / 1000
    }
}