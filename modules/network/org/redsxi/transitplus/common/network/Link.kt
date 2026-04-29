package org.redsxi.transitplus.common.network

interface Link {
    fun init()
    fun listen(type: PacketType, listener: suspend (Packet) -> Unit)
    fun send(packet: Packet)
}