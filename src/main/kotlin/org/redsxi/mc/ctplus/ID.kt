package org.redsxi.mc.ctplus

import net.minecraft.resources.ResourceLocation

const val modId: String = "ctplus"

fun idOf(name: String): ResourceLocation = ResourceLocation(modId, name)


val ticketBarrierPayDirect = idOf("ticket_barrier_pay_direct")
val card = idOf("card")
val whiteCard = idOf("white")
val singleJourneyCard = idOf("single_journey")
val prepaidCard = idOf("prepaid")
val ctPlus = idOf("ct_plus")

val ticketBarrierEntranceTp = idOf("ticket_barrier_entrance_tp")
val ticketBarrierExitTp = idOf("ticket_barrier_exit_tp")
val ticketBarrierPayDirectTp = idOf("ticket_barrier_pay_direct_tp")


val main = idOf("main")