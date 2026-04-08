@file:JvmName("MainCommon")

package org.redsxi.transitplus.common

import org.redsxi.transitplus.server.network.NetworkLinkServer

fun entry() {
    NetworkLinkServer.init()
}