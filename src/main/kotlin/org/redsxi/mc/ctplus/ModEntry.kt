package org.redsxi.mc.ctplus

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import org.redsxi.mc.ctplus.command.CTPlusCommand

fun init() {
    CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
        dispatcher.register(CTPlusCommand.getCommand() as LiteralArgumentBuilder<CommandSourceStack>)
    }
}

fun clientInit() {

}

fun serverInit() {

}