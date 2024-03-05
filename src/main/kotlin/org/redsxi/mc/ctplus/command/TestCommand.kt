package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import org.redsxi.mc.ctplus.generated.RuntimeVariables
import org.redsxi.mc.ctplus.mapping.Text

object TestCommand : Command() {
    override fun getCommandImpl(): ArgumentBuilder<CommandSourceStack, *> = literal("test")
    override fun canExecute(): Boolean = true
    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        context.source.sendSystemMessage(Text.literal("Debug mode: ${RuntimeVariables.DEBUG}"))
        return 1
    }

    override fun getChild(): Array<Command> {
        return arrayOf()
    }
}