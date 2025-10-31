package org.redsxi.mc.ctplus.core.ticketProcessing

import mtr.data.TicketSystem
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

import org.redsxi.mc.ctplus.core.ticketProcessing.TicketProcessingMethod.PassType
import org.redsxi.mc.ctplus.core.ticketProcessing.TicketProcessingMethod.TicketProcessingResult

import org.redsxi.mc.ctplus.core.ticketProcessing.TicketProcessingMethod.TicketProcessingResult.*
import org.redsxi.mc.ctplus.core.ticketProcessing.TicketProcessingMethod.PassType.*

import mtr.data.TicketSystem.EnumTicketBarrierOpen
import net.minecraft.sounds.SoundSource
import net.minecraft.world.scores.Score
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.mapping.Text

/**
 * Using default MTR method.
 */
object MtrDefault: TicketProcessingMethod {
    override fun tryPass(
        level: Level,
        pos: BlockPos,
        player: Player,
        passType: PassType,
        passSound: SoundEvent
    ): TicketProcessingResult {
        when(passType) {
            // Invoking logics from MTR
            ENTRY,
            EXIT -> {
                return TicketSystem.passThrough(
                    level,
                    pos,
                    player,
                    passType == ENTRY,
                    passType == EXIT,
                    passSound,
                    passSound,
                    passSound,
                    passSound,
                    null,
                    false
                ).convertIntoCTPlusTicketProcessingResult()
            }

            // Constructing logics by myself
            PAY_DIRECT -> {
                TicketSystem.addObjectivesIfMissing(level)
                val barrierPDBlockEntity = level.getBlockEntity(pos)
                if(barrierPDBlockEntity is BlockEntityTicketBarrierPayDirect) {
                    val price = barrierPDBlockEntity.price
                    val balanceScore = getScore(level, player)
                    if(balanceScore.score < price) {
                        player.displayClientMessage(
                            Text.translatable(
                                Text.GUI,
                                "mtr",
                                "insufficient_balance",
                                balanceScore.score
                            ), true
                        )
                        return FORBID
                    }
                    balanceScore.add(-price)
                    player.displayClientMessage(
                        Text.translatable(Text.GUI, "enter_barrier", price),
                        true
                    )
                    level.playSound(player, pos, passSound, SoundSource.BLOCKS)
                    return PASS
                }
            }
        }
        return FORBID
    }

    private fun getScore(level: Level, player: Player): Score {
        val objective = level.scoreboard.getObjective(TicketSystem.BALANCE_OBJECTIVE) ?: throw RuntimeException("WTF")
        return level.scoreboard.getOrCreatePlayerScore(player.gameProfile.name,objective)
    }

    private fun EnumTicketBarrierOpen.convertIntoCTPlusTicketProcessingResult() = when(this) {
        EnumTicketBarrierOpen.OPEN,
        EnumTicketBarrierOpen.OPEN_CONCESSIONARY -> {
            PASS
        }
        EnumTicketBarrierOpen.CLOSED -> {
            FORBID
        }
    }
}