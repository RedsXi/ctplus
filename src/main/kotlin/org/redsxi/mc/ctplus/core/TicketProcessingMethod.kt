package org.redsxi.mc.ctplus.core

/**
 * Unified passing methods for improving coding quality
 */
interface TicketProcessingMethod {
    enum class PassType {
        PAY_DIRECT,
        ENTRY,
        EXIT
    }

    enum class TicketProcessResult {
        /**
         * Forbid player to pass the barrier.
         */
        PASS,

        /**
         * Forbid player to pass the barrier.
         */
        FORBID;
    }

    /**
     * Method trying passing the barriers. When player can pass, it should return [TicketProcessResult.PASS]. If not, return [TicketProcessResult.FORBID]
     */
    fun tryPass(

    ): TicketProcessResult
}