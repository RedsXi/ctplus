package org.redsxi.mc.ctplus.util

import java.text.SimpleDateFormat
import java.util.Date

object Date {
    operator fun get(time: Long): String {
        val dateFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss")
        val date = Date(time)
        return dateFormat.format(date)
    }
}