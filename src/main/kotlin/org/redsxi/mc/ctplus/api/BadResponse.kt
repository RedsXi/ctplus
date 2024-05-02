package org.redsxi.mc.ctplus.api

class BadResponse(e: Exception) : ResponseData {
    val message: String

    override val status = "bad"

    init {
        message = "${e.message} (${e::class.simpleName})"
    }
}