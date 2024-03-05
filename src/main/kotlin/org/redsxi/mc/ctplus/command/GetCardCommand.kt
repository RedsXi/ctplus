package org.redsxi.mc.ctplus.command

object GetCardCommand : Command() {
    override fun getCommandImpl() = literal("getCard")
}