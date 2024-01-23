package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.Collections
import org.redsxi.mc.ctplus.Variables
import org.redsxi.mc.ctplus.card.SingleJourneyCard

class GetCardCommand : CommandProvider() {
    override fun getCommand(): ICommand = ICommand("getCard").requires{it.hasPermission(2)}
        .then(literal("single")).then(argument("Price", IntegerArgumentType.integer(0))).executes(this::getSingleCard)

    private fun getSingleCard(stack: CommandContext<CommandSourceStack>): Int {
        val player = stack.source.player ?: return -1
        return getSingleCard(player, IntegerArgumentType.getInteger(stack, "Price"))
    }

    private fun getSingleCard(player: ServerPlayer, price: Int): Int {
        val card = Collections.Cards.SINGLE_JOURNEY as SingleJourneyCard
        card.price = price
        val data = card.createData()
        val cardItem = Variables.cardItemList[card] ?: return -2
        val stack = ItemStack(cardItem)
        stack.count = 1
        stack.tag = data
        player.inventory.add(stack)
        return 1
    }
}