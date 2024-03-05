package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import org.redsxi.mc.ctplus.Variables

object SetTranslationIndexCommand : Command() {
    override fun getCommandImpl(): ArgumentBuilder<CommandSourceStack, *> = literal("setTranslationIndex").then(argument("Index", IntegerArgumentType.integer(0, 255)))

    override fun canExecute() = true

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val index = IntegerArgumentType.getInteger(context, "Index")
        Variables.translationIndex = index
        return 1
    }
}