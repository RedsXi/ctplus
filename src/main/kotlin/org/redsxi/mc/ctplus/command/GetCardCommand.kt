package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer
import org.redsxi.mc.ctplus.Collections
import org.redsxi.mc.ctplus.card.SingleJourneyCard

class GetCardCommand : CommandProvider() {
    override fun getCommand(): ICommand = ICommand("getCard").requires{it.hasPermission(2)}
        .then(literal("single")).then(argument("Price", IntegerArgumentType.integer(0))).executes(this::getSingleCard)

    private fun getSingleCard(stack: CommandContext<CommandSourceStack>): Int {
        stack.source.player?.let {
            getSingleCard(it, IntegerArgumentType.getInteger(stack, "Price"))
        }
        return 1
    }

    private fun getSingleCard(player: ServerPlayer, price: Int) {
        val card = Collections.Cards.SINGLE_JOURNEY as SingleJourneyCard
        card.price = price
    }
}