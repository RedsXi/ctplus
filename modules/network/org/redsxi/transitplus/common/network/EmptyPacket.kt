package org.redsxi.transitplus.common.network

import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.idOf

/**
 * 用于触发服务器创建会话
 */
object EmptyPacket: Packet() {
    override val id: ResourceLocation
        get() = idOf("empty")
    object Type: PacketType {
        override fun create() = EmptyPacket
        override val id = idOf("empty")
    }
}