package org.redsxi.mc.crabgc.blockentity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.redsxi.mc.crabgc.blockEntityTicketBarrierPayDirect

class BlockEntityTicketBarrierPayDirect(pos: BlockPos, state: BlockState) : BlockEntity(
    blockEntityTicketBarrierPayDirect,
    pos, state
) {
    var price: Long = 2

    override fun saveAdditional(nbt: CompoundTag) {
        nbt.putLong("Price", price)
        super.saveAdditional(nbt)
    }

    override fun load(nbt: CompoundTag) {
        price = nbt.getLong("Price")
        super.load(nbt)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(): CompoundTag {
        return saveWithoutMetadata()
    }
}