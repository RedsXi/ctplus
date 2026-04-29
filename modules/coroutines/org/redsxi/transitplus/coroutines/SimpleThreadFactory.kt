package org.redsxi.transitplus.coroutines

import java.util.concurrent.ThreadFactory

internal class SimpleThreadFactory(val name: String): ThreadFactory {
    var id = 0

    override fun newThread(r: Runnable)
        = Thread(r, "$name #${id++}")
}