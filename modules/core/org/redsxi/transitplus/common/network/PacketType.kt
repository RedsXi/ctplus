package org.redsxi.transitplus.common.network

import net.minecraft.resources.ResourceLocation

interface PacketType {
    fun create(): Packet
    val id: ResourceLocation

    companion object {
        fun createEmpty(id: ResourceLocation): PacketType {
            return object: PacketType {
                override fun create(): Packet = object : Packet() {
                    override val id = id
                }
                override val id = id
            }
        }
    }
}