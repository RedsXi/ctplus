package org.redsxi.mc.ctplus.web

sealed interface OrderChecker {
    companion object : OrderChecker {
        @JvmStatic
        fun verifyOrder(orderId: String) {

        }
    }
}