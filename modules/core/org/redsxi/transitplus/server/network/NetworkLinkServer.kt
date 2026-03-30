package org.redsxi.transitplus.server.network

import io.netty.handler.codec.EncoderException
import kotlinx.io.IOException
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.server.level.ServerPlayer
import org.redsxi.transitplus.common.network.Package
import org.redsxi.transitplus.common.network.PackageType

object NetworkLinkServer {
    fun <P : Package<P>> registerPackageProcessor(type: PackageType<P>, processor: (P) -> Unit) {
        ServerPlayNetworking.registerGlobalReceiver(type.id) { server, player, listener, buf, sender ->
            val compound = buf.readAnySizeNbt() ?: throw IOException("Malformed packet")
            val payload = compound.get("Payload") ?: throw IOException("Malformed packet")
            val result = type.codec.parse(NbtOps.INSTANCE, payload)
            result.error().ifPresent {
                val msg = it.message()
                throw EncoderException("Failed to decode: $msg $payload")
            }

        }
    }

    fun <P : Package<P>> sendPackage(pack: P, player: ServerPlayer?) {
        val compound = CompoundTag()
        val result = pack.type.codec.encodeStart(NbtOps.INSTANCE, pack)
        result.error().ifPresent {
            val msg = it.message()
            throw EncoderException("Failed to encode: $msg $pack")
        }
        compound.put("Payload", result.result().get())
        val buf = PacketByteBufs.create()
        buf.writeNbt(compound)
        ServerPlayNetworking.send(player, pack.type.id, buf)
    }
}