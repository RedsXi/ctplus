package org.redsxi.mc.ctplus.util

import mtr.mappings.Text
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.scores.Score
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect

object PassManager {
    fun onEntityPass(pos: BlockPos, level: Level, player: Player, passSound: SoundEvent): Boolean {
        addObjective(level)

        val be = level.getBlockEntity(pos)
        if(be is BlockEntityTicketBarrierPayDirect) {
            val price = be.price
            val balanceScore = getScore(level, player, "mtr_balance")
            if(balanceScore.score < price) {
                player.displayClientMessage(
                    Text.translatable(
                        "gui.mtr.insufficient_balance",
                        balanceScore.score
                    ), true
                )
                return false
            }
            balanceScore.add((0 - price).toInt())
            player.displayClientMessage(
                Text.translatable("gui.cgcem.enter_barrier", price),
                true
            )
            return true
        }
        return false
    }

    private fun addObjective(level: Level) {
        try {
            level.scoreboard.addObjective(
                "mtr_balance",
                ObjectiveCriteria.DUMMY,
                Text.literal("\ufefa Balance 余额 \ufefa"),
                ObjectiveCriteria.RenderType.INTEGER
            )
        } catch (_: Exception) {
        }
    }

    private fun getScore(level: Level, player: Player, name: String): Score {
        return level.scoreboard.getOrCreatePlayerScore(player.gameProfile.name,level.scoreboard.getObjective(name))
    }
}