package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import java.util.function.Predicate

class ICommand(literal: String) : LiteralArgumentBuilder<CommandSourceStack>(literal) {
    override fun requires(requirement: Predicate<CommandSourceStack>): ICommand = super.requires(requirement) as ICommand
    override fun then(argument: ArgumentBuilder<CommandSourceStack, *>?): ICommand = super.then(argument) as ICommand
    override fun executes(command: Command<CommandSourceStack>?): ICommand = super.executes(command) as ICommand

}