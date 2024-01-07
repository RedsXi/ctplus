package org.redsxi.mc.crabgc.blockentity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import org.redsxi.mc.crabgc.blockEntityTicketBarrierPayDirect

class BlockEntityTicketBarrierPayDirect(pos: BlockPos, state: BlockState) : BlockEntity(
    blockEntityTicketBarrierPayDirect,
    pos, state
) {
    var price: Long = 2

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putLong("Price", price)
        super.writeNbt(nbt)
    }

    override fun readNbt(nbt: NbtCompound) {
        price = nbt.getLong("Price")
        super.readNbt(nbt)
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        return createNbt()
    }
}