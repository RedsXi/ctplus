package org.redsxi.mc.ctplus.api

import org.redsxi.mc.ctplus.generated.RuntimeVariables
import java.io.ByteArrayOutputStream
import java.io.PrintStream

open class BadResponse(e: Throwable) : ResponseData {
    val message: String
    val error: String

    override val status = "bad"

    init {
        message = e.message.toString()
        error = e::class.java.simpleName
    }

    class Debug(e: Throwable): BadResponse(e) {
        val stackTrace: String
        init {
            val binaryOut = ByteArrayOutputStream()
            val printOut = PrintStream(binaryOut)
            e.printStackTrace(printOut)
            stackTrace = binaryOut.toString()
        }
    }

    companion object {
        fun new(e: Throwable): BadResponse {
            return if(RuntimeVariables.DEBUG) Debug(e) else BadResponse(e)
        }
    }
}