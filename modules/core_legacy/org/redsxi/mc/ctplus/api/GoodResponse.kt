package org.redsxi.mc.ctplus.api

class GoodResponse(val data: Any?) : ResponseData {
    override val status = "good"
}