package org.redsxi.mc.ctplus.blockentity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.redsxi.bool.Bool
import org.redsxi.mc.ctplus.Collections

open class BlockEntityTicketBarrierPayDirect(pos: BlockPos, state: BlockState, isTransitPlus: Bool) : BlockEntity(
    if(isTransitPlus.getK()) Collections.BlockEntities.TICKET_BARRIER_PAY_DIRECT_TP else Collections.BlockEntities.TICKET_BARRIER_PAY_DIRECT,
    pos, state
) {
    var price: Int = 2

    override fun saveAdditional(nbt: CompoundTag) {
        nbt.putInt("Price", price)
        super.saveAdditional(nbt)
    }

    override fun load(nbt: CompoundTag) {
        price = nbt.getInt("Price")
        super.load(nbt)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(): CompoundTag {
        return saveWithoutMetadata()
    }
}