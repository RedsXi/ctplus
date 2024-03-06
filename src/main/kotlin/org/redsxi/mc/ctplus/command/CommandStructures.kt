package org.redsxi.mc.ctplus.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.Coordinates
import org.redsxi.mc.ctplus.Collections
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.data.CardData
import org.redsxi.mc.ctplus.generated.BuildProps
import org.redsxi.mc.ctplus.generated.RuntimeVariables
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.network.SetTranslationIndexS2CPacket
import org.redsxi.mc.ctplus.setTranslationIndex
import org.redsxi.mc.ctplus.util.Date
import org.redsxi.mc.ctplus.util.Time

object CommandStructures {
    @JvmField
    val SET_PASS_COST: LiteralArgumentBuilder<CommandSourceStack>
    = literal<CommandSourceStack>("setPassCost").requires { it.hasPermission(2) }.then(
        argument<CommandSourceStack, Coordinates>("Position", BlockPosArgument.blockPos()).then(
            argument<CommandSourceStack?, Int?>("Cost", IntegerArgumentType.integer()).executes {
                val cost = IntegerArgumentType.getInteger(it, "Cost")
                val pos = BlockPosArgument.getBlockPos(it, "Position")
                val blockEntity = it.source.level.getBlockEntity(pos)
                if (blockEntity is BlockEntityTicketBarrierPayDirect) {
                    blockEntity.price = cost
                    1
                } else -1
            }
        )
    )

    @JvmField
    val GET_CARD: LiteralArgumentBuilder<CommandSourceStack>
    = literal<CommandSourceStack>("getCard").requires { it.hasPermission(4) }.then(
        literal<CommandSourceStack>("singleJourney").then(
            argument<CommandSourceStack, Int>("Price", IntegerArgumentType.integer(0)).executes {
                val price = IntegerArgumentType.getInteger(it, "Price")
                val context = CardData.create(Collections.Cards.SINGLE_JOURNEY)
                context.data.isUsed = false
                context.data.price = price
                context.update()
                val player = it.source.player ?: return@executes -1
                player.addItem(context.data.stack)
                1
            }
        )
    ).then(
        literal<CommandSourceStack>("prepaid").then(
            argument<CommandSourceStack, Int>("Balance", IntegerArgumentType.integer()).executes {
                val balance = IntegerArgumentType.getInteger(it, "Balance")
                val context = CardData.create(Collections.Cards.PREPAID)
                context.data.lastRechargeTime = Time.millis
                context.data.balance = balance
                context.update()
                val player = it.source.player ?: return@executes -1
                player.addItem(context.data.stack)
                1
            }
        )
    )

    @JvmField
    val CTPLUS: LiteralArgumentBuilder<CommandSourceStack>
    = literal<CommandSourceStack>("ctplus").then(
        literal<CommandSourceStack?>("version").executes {
            it.source.sendSuccess(Text.literal("CrabMTR Transit+ version ${BuildProps.VERSION}"), false)
            if(RuntimeVariables.DEBUG) {
                it.source.sendSuccess(Text.literal("Build time ${Date[BuildProps.BUILD_TIME]}"), false)
                it.source.sendSuccess(Text.literal("\u00a7eYou're running a debug version of CTPlus!"), false)
            }
            1
        }
    )

    @JvmField
    val VARIABLES: LiteralArgumentBuilder<CommandSourceStack>
    = literal<CommandSourceStack>("variables").then(
        literal<CommandSourceStack>("translationIndex").then(
            argument<CommandSourceStack, Int>("Index", IntegerArgumentType.integer(0)).executes {
                ServerPlayNetworking.send(it.source.playerOrException, SetTranslationIndexS2CPacket(IntegerArgumentType.getInteger(it, "Index")))
                1
            }
        )
    )
}