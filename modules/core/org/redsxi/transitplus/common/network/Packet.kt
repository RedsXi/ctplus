package org.redsxi.transitplus.common.network

import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation

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
}