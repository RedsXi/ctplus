package org.redsxi.mc.ctplus.mapping

import net.minecraft.world.entity.item.ItemEntity
import java.util.UUID

object ItemEntityMapper {
    fun setTarget(entity: ItemEntity, player: UUID) {
        entity.setTarget(player)
    }
}