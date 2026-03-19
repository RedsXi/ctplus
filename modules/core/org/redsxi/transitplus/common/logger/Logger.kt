package org.redsxi.transitplus.common.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val savedClassLoggers = HashMap<String, Logger>()

fun logger(): Logger {
    val nameFull = callerName()
    val name = nameFull.split(".").last()
    return savedClassLoggers.getOrPut(name) {
        LoggerFactory.getLogger(name)
    }
}

private fun callerName(): String {
    Exception().printStackTrace()

    return Thread.currentThread().stackTrace[4].className
}

val logger get() = logger()
