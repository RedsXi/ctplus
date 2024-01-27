package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.CommandSourceStack

abstract class Command {
    abstract fun getCommandImpl(): ArgumentBuilder<CommandSourceStack, *>
    open fun getChild(): Array<Command> = arrayOf()
    open fun execute(): Int = 0
    open fun canExecute(): Boolean = false

    fun getCommand(): ArgumentBuilder<CommandSourceStack, *> {
        val cmd = getCommandImpl()
        getChild().forEach {
            cmd.then(it.getCommand())
        }
        if(canExecute()) cmd.executes {
            execute()
        }
        return cmd
    }

    companion object {
        fun literal(name: String): LiteralArgumentBuilder<CommandSourceStack> = LiteralArgumentBuilder.literal(name)
        fun <Type> argument(name: String, argType: ArgumentType<Type>): RequiredArgumentBuilder<CommandSourceStack, Type> = RequiredArgumentBuilder.argument(name, argType)
    }
}