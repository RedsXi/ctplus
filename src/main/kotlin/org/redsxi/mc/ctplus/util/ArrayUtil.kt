package org.redsxi.mc.ctplus.util

object ArrayUtil {
    fun <T> T.asMutableCollection(): MutableCollection<T> {
        val list = ArrayList<T>()
        list.add(this)
        return list
    }
}