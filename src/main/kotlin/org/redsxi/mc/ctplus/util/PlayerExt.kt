package org.redsxi.mc.ctplus.util

import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack

fun ServerPlayer.putItem(item: ItemStack) {
    addItem(item)
    playSound(SoundEvents.ITEM_PICKUP)
}