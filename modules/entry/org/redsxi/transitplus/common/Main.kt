@file:JvmName("MainCommon")

package org.redsxi.transitplus.common

import org.redsxi.transitplus.server.network.NetworkLinkServer
import org.redsxi.transitplus.server.network.NetworkServer

fun entry() {
    NetworkLinkServer.init()
    NetworkServer.init()
}