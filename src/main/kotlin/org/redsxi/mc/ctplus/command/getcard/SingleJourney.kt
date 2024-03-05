package org.redsxi.mc.ctplus.command.getcard

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import org.redsxi.mc.ctplus.Collections
import org.redsxi.mc.ctplus.command.Command
import org.redsxi.mc.ctplus.data.CardData

object SingleJourney : Command() {
    override fun getCommandImpl(): ArgumentBuilder<CommandSourceStack, *> = literal("singleJourney").then(
        argument("Price", IntegerArgumentType.integer(0))
    )

    override fun canExecute() = true

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player
        val price = IntegerArgumentType.getInteger(context, "Price")
        val cxt = CardData.create(Collections.Cards.SINGLE_JOURNEY)
        cxt.data.price = price
        cxt.data.isUsed = false
        cxt.update()
        player?.let {
            player.addItem(cxt.data.stack)
        }

        return 1
    }
}