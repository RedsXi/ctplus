package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.builder.ArgumentBuilder
import net.minecraft.commands.CommandSourceStack

object GetCardCommand : Command() {
    override fun getCommandImpl(): ArgumentBuilder<CommandSourceStack, *> = literal("getCard")
}