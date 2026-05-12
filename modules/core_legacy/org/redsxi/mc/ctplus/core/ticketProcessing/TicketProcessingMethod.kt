package org.redsxi.mc.ctplus.core.ticketProcessing

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * Unified passing methods for improving coding quality
 */
interface TicketProcessingMethod {
    enum class PassType {
        PAY_DIRECT,
        ENTRY,
        EXIT
    }

    enum class TicketProcessingResult {
        /**
         * Allow player to pass the barrier.
         */
        PASS,

        /**
         * Forbid player to pass the barrier.
         */
        FORBID;
    }

    /**
     * Method trying passing the barriers. When player can pass, it should return [TicketProcessingResult.PASS]. If not, return [TicketProcessingResult.FORBID]
     */
    fun tryPass(
        level: Level,
        pos: BlockPos,
        player: Player,
        passType: PassType,
        passSound: SoundEvent
    ): TicketProcessingResult
}