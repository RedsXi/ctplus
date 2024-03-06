package org.redsxi.mc.ctplus.network


import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.minecraft.network.FriendlyByteBuf
import org.redsxi.mc.ctplus.setTranslationIndex

class SetTranslationIndexS2CPacket(val index: Int) : FabricPacket {
    constructor(buf: FriendlyByteBuf) : this(buf.readInt())

    override fun write(buf: FriendlyByteBuf) {
        buf.writeInt(index)
    }

    override fun getType(): PacketType<*> = TYPE

    companion object {
        @JvmField
        val TYPE: PacketType<SetTranslationIndexS2CPacket> = PacketType.create(setTranslationIndex){SetTranslationIndexS2CPacket(it)}
    }
}