package org.redsxi.transitplus.common

fun <T, U> MutableMap<T, U>.getOrCreate(key: T, default: U): U {
    if(!containsKey(key)) {
        this[key] = default
    }
    return this[key] ?: default
}