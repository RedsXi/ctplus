package org.redsxi.transitplus.client.network

import io.netty.handler.codec.EncoderException
import kotlinx.io.IOException
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import org.redsxi.transitplus.common.network.Package
import org.redsxi.transitplus.common.network.PackageType

object NetworkLinkClient {
    fun <P : Package<P>> registerPackageProcessor(type: PackageType<P>, processor: (P) -> Unit) {
        ClientPlayNetworking.registerGlobalReceiver(type.id) { client, listener, buf, sender ->
            val compound = buf.readAnySizeNbt() ?: throw IOException("Malformed packet")
            val payload = compound.get("Payload") ?: throw IOException("Malformed packet")
            val result = type.codec.parse(NbtOps.INSTANCE, payload)
            result.error().ifPresent {
                val msg = it.message()
                throw EncoderException("Failed to decode: $msg $payload")
            }
            processor(result.result().get())
        }
    }

    fun <P : Package<P>> sendPackage(pack: P) {
        val compound = CompoundTag()
        val result = pack.type.codec.encodeStart(NbtOps.INSTANCE, pack)
        result.error().ifPresent {
            val msg = it.message()
            throw EncoderException("Failed to encode: $msg $pack")
        }
        compound.put("Payload", result.result().get())
        val buf = PacketByteBufs.create()
        buf.writeNbt(compound)
        ClientPlayNetworking.send(pack.type.id, buf)
    }
}