package org.redsxi.mc.ctplus.api

import org.redsxi.mc.ctplus.Variables

interface Players {
    fun getOnlinePlayers(): List<PlayerBaseData>

    companion object : Players {
        override fun getOnlinePlayers(): List<PlayerBaseData> {
            val data = ArrayList<PlayerBaseData>()
            Variables.playerList.entries.forEach {
                data.add(PlayerBaseData(it.value))
            }
            return data
        }
    }
}