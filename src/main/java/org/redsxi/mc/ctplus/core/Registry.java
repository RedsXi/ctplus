package org.redsxi.mc.ctplus.core;/*
package org.redsxi.mc.ctplus.core

import net.minecraft.resources.ResourceLocation

class Registry<T> : Iterable<Map.Entry<ResourceLocation, T>> {
    private val map: Map<ResourceLocation, T> = HashMap()

    override fun iterator(): Iterator<Map.Entry<ResourceLocation, T>> {
        return map.iterator()
    }

    fun register(item: T, id: ResourceLocation) {
        map.put(id, item)
    }
}
 */

import net.minecraft.resources.ResourceLocation;
import org.redsxi.mc.ctplus.IDKt;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Registry<T> implements Iterable<Map.Entry<ResourceLocation, T>>, Closeable {

    private boolean close = false;

    private final T defaultItem;

    private final Map<ResourceLocation, T> map = new HashMap<>();

    public Iterator<Map.Entry<ResourceLocation, T>> iterator() {
        return map.entrySet().iterator();
    }

    private void checkClose() throws IOException {
        if(close) throw new IOException("Closed");
    }

    public T register(T item, ResourceLocation id) {
        try {
            checkClose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(map.containsKey(id)) throw new RuntimeException("Duplicate key " + id.toString());
        map.put(id, item);
        return item;
    }

    public T get(ResourceLocation id) {
        return map.get(id);
    }

    public boolean contains(ResourceLocation id) {
        return map.containsKey(id);
    }

    public void close() throws IOException {
        checkClose();
        close = true;
    }

    public int registeredItemCount() {
        return map.size();
    }

    public Registry(String defaultItemName, T defaultItem) {
        register(defaultItem, new ResourceLocation(IDKt.modId, defaultItemName));
        this.defaultItem = defaultItem;
    }
}