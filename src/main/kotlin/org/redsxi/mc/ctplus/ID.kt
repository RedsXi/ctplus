package org.redsxi.mc.ctplus

import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

val modId: String = "ctplus"

fun idOf(name: String): ResourceLocation {
    return ResourceLocation(modId, name)
}

val ticketBarrierPayDirect = idOf("ticket_barrier_pay_direct")
val card = idOf("card")