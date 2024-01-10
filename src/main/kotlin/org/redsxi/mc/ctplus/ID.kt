package org.redsxi.mc.ctplus

import net.minecraft.resources.ResourceLocation

const val modId: String = "ctplus"

fun idOf(name: String): ResourceLocation = ResourceLocation(modId, name)

fun mcIdOf(name: String): ResourceLocation = ResourceLocation("minecraft", name)

val ticketBarrierPayDirect = idOf("ticket_barrier_pay_direct")
val card = idOf("card")
val whiteCard = idOf("white_card")

val modelItemGenerated = mcIdOf("item/generated")