package org.redsxi.config

import org.redsxi.config.config.ConfigNotFoundException

class Config {
    private val configs: MutableMap<Int, Any> = HashMap()
    private val keys: MutableList<ConfigKey<*>> = ArrayList()

    operator fun <T> get(key: ConfigKey<T>): T {
        val confIndex = keys.indexOf(key)
        if(confIndex == -1) throw ConfigNotFoundException(key)
        return key.cast(configs[confIndex] ?: (key.default as Any))
    }

    operator fun <T> set(key: ConfigKey<T>, value: T) {
        val confIndex = keys.indexOf(key)
        if(confIndex == -1) throw ConfigNotFoundException(key)
        configs[confIndex] = value as Any
    }

    fun register(key: ConfigKey<*>) {
        keys.add(key)
        configs[keys.indexOf(key)] = key.default as Any
    }

    fun eachKey(consumer: (ConfigKey<*>) -> Unit) = keys.forEach{consumer(it)}
}