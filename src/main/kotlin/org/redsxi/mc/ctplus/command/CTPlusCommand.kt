package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import org.redsxi.mc.ctplus.generated.BuildProps
import org.redsxi.mc.ctplus.mapping.Text

object CTPlusCommand : Command() {
    override fun getCommandImpl(): ArgumentBuilder<CommandSourceStack, *> = LiteralArgumentBuilder.literal("ctplus")
    override fun getChild(): Array<Command> {
        return arrayOf(GetCardCommand, TestCommand, SetTranslationIndexCommand)
    }

    override fun canExecute(): Boolean = true
    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        context.source.sendSystemMessage(Text.literal("CrabMTR Transit+ Version ${BuildProps.VERSION}"))
        return 1
    }
}