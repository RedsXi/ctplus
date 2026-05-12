package org.redsxi.transitplus.coroutines

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadPools {
    private fun createPool(name: String, core: Int = 4, max: Int = 8) = ThreadPoolExecutor(
        core,
        max,
        0L,
        TimeUnit.SECONDS,
        LinkedBlockingQueue(),
        SimpleThreadFactory(name)
    )

    val RCP_RAIL_RENDERER = createPool("RCP Rail Renderer")
    val NETWORK = createPool("CTPlus Network Thread")

}