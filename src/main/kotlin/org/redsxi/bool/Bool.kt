package org.redsxi.bool

class Bool(private val bool: Boolean) {
    companion object {
        @JvmField
        val TRUE = Bool(true)

        @JvmField
        val FALSE = Bool(false)
    }

    fun get(): Boolean = bool
}