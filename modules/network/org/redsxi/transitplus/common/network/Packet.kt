package org.redsxi.transitplus.common.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import org.redsxi.transitplus.coroutines.Dispatchers

abstract class Packet {
    open fun defaultData(): CompoundTag = CompoundTag()
    open fun loadData(tag: CompoundTag) {}
    open fun saveData(tag: CompoundTag) {}

    fun getData(): CompoundTag {
        val data = defaultData()
        saveData(data)
        return data
    }

    abstract val id: ResourceLocation

    override fun toString(): String {
        return "${this::class.java.simpleName}${getData()}"
    }

    companion object {
        fun read(type: PacketType, data: CompoundTag): Packet {
            val packet = type.create()
            packet.loadData(data)
            return packet
        }
    }
}