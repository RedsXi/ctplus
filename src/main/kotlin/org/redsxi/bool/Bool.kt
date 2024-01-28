package org.redsxi.bool

import java.lang.RuntimeException

@Suppress("USE_KOTLIN_CLASS")
typealias JBoolean = java.lang.Boolean

abstract class Bool {
    abstract fun getJ(): JBoolean
    abstract fun getK(): Boolean


    companion object {
        @JvmStatic
        fun fromJ(bl: JBoolean): Bool {
            return when (bl) {
                true as JBoolean -> True
                false as JBoolean -> False
                else -> throw RuntimeException("Boolean has value that isn't true or false")
            }
        }

        @JvmStatic
        fun fromK(bl: Boolean): Bool {
            return if (bl) True else False
        }
    }
}

// True, dude
object True : Bool() {
    override fun getJ(): JBoolean = true as JBoolean
    override fun getK(): Boolean = true
}

object False : Bool() {
    override fun getJ(): JBoolean = false as JBoolean
    override fun getK(): Boolean = false
}