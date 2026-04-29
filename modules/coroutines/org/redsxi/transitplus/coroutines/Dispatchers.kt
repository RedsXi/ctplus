package org.redsxi.transitplus.coroutines

import kotlinx.coroutines.asCoroutineDispatcher

object Dispatchers {
    val RCP_RAIL_RENDERER = ThreadPools.RCP_RAIL_RENDERER.asCoroutineDispatcher()
    val NETWORK = ThreadPools.NETWORK.asCoroutineDispatcher()
}