package org.redsxi.config

class ConfigKey<T>(val name: String, val default: T) {
    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any): T = obj as T
}