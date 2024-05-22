package org.redsxi.mc.ctplus.util

import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.stream.JsonWriter

fun JsonWriter.value(v: JsonObject) {
    beginObject()
    v.entrySet().forEach { entry ->
        name(entry.key)
        val element = entry.value
        if(element is JsonNull) nullValue()
        if(element is JsonObject) value(element)
        if(element is JsonArray) value(element)
        if(element is JsonPrimitive) {
            if(element.isString) value(element.asString)
            if(element.isBoolean) value(element.asBoolean)
            if(element.isNumber) value(element.asNumber)
        }
    }
    endObject()
}

fun JsonWriter.value(v: JsonArray) {
    beginArray()
    v.asList().forEach { element ->
        if(element is JsonNull) nullValue()
        if(element is JsonObject) value(element)
        if(element is JsonArray) value(element)
        if(element is JsonPrimitive) {
            if(element.isString) value(element.asString)
            if(element.isBoolean) value(element.asBoolean)
            if(element.isNumber) value(element.asNumber)
        }
    }
    endArray()
}