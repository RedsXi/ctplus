package org.redsxi.mc.capi

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag

class CardInstance(val card: Card) {

    private var data: Tag? = CompoundTag()



    companion object {
        @JvmStatic
        fun fromData(data: Tag): CardInstance {

        }
    }
}