package org.redsxi.mc.ctplus.api

import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.Collections
import org.redsxi.mc.ctplus.Variables
import org.redsxi.mc.ctplus.data.CardContext
import org.redsxi.mc.ctplus.data.CardData
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.util.Time
import org.redsxi.mc.ctplus.util.putItem
import java.util.*

interface CardService {
    fun createPrepaidCard(playerUuid: UUID, balance: Int)
    fun createSingleJourneyCard(playerUuid: UUID, price: Int)
    fun getHoldingCard(playerUuid: UUID): CardContext.Web?
    fun getCardContextFromItemStack(stack: ItemStack): CardContext<*, *>?
    companion object : CardService {
        override fun createPrepaidCard(playerUuid: UUID, balance: Int) {
            val context = CardData.create(Collections.Cards.PREPAID)
            context.data.lastRechargeTime = Time.millis
            context.data.balance = balance
            context.update()
            val player = Variables.playerList[playerUuid] ?: throw IllegalArgumentException("No player found")
            player.putItem(context.data.stack)
            player.sendSystemMessage(Text.translatable(Text.GUI, "created_prepaid_card", balance))
        }
        override fun createSingleJourneyCard(playerUuid: UUID, price: Int) {
            val context = CardData.create(Collections.Cards.SINGLE_JOURNEY)
            context.data.isUsed = false
            context.data.price = price
            context.update()
            val player = Variables.playerList[playerUuid] ?: throw IllegalArgumentException("No player found")
            player.putItem(context.data.stack)
            player.sendSystemMessage(Text.translatable(Text.GUI, "created_single_journey_card", price))
        }
        override fun getHoldingCard(playerUuid: UUID): CardContext.Web? {
            val player = Variables.playerList[playerUuid] ?: throw IllegalArgumentException("No player found")
            val ctx = getCardContextFromItemStack(player.mainHandItem) ?: return null
            return ctx.toWeb()
        }

        override fun getCardContextFromItemStack(stack: ItemStack): CardContext<*, *>? {
            val item = stack.item
            return if(item is ItemCard<*, *>) {
                val card = item.card
                card.context(stack)
            } else null
        }
    }
}