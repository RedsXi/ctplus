package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.CommandSourceStack

abstract class CommandProvider {
    abstract fun getCommand(): ICommand
    fun literal(name: String): ICommand = ICommand(name)
    fun <Type> argument(name: String, arg: ArgumentType<Type>): RequiredArgumentBuilder<CommandSourceStack, Type> = RequiredArgumentBuilder.argument(name, arg)
}