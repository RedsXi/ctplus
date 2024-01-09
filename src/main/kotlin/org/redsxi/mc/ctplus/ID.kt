package org.redsxi.mc.crabgc

import net.minecraft.resources.ResourceLocation

val modId: String = "cgcem"

fun idOf(name: String): ResourceLocation {
    return ResourceLocation(modId, name)
}

val ticketBarrierPayDirect = idOf("ticket_barrier_pay_direct")
val card = idOf("card")