package org.redsxi.mc.crabgc

import net.minecraft.util.Identifier

val modId: String = "cgcem"

fun idOf(name: String): Identifier {
    return Identifier(modId, name)
}

val ticketBarrierPayDirect = idOf("ticket_barrier_pay_direct")