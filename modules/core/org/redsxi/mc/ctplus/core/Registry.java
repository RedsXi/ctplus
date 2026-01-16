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
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

    public class Registry<T> implements Iterable<Map.Entry<ResourceLocation, T>>, Closeable {

        private boolean close = false;

        private final T defaultItem;
        private final ResourceLocation defaultLocation;

        private final Map<ResourceLocation, T> map = new HashMap<>();

        private final List<BiConsumer<ResourceLocation, T>> registrationHooks = new ArrayList<>();

        @NotNull
        public Iterator<Map.Entry<ResourceLocation, T>> iterator() {
        return map.entrySet().iterator();
    }

    public void register(T item, ResourceLocation id) {
        if(close) return;
        if(map.containsKey(id)) throw new RuntimeException("Duplicate key " + id.toString());
        map.put(id, item);

        // Hooks
        registrationHooks.forEach((hook) -> hook.accept(id, item));

    }

    public T get(ResourceLocation id) {
        return map.getOrDefault(id, defaultItem);
    }

    public T get(ResourceLocation id, T defaultT) {
        return map.getOrDefault(id, defaultT);
    }

    public ResourceLocation getItemID(T card) {
        ResourceLocation result = defaultLocation;
        for(Map.Entry<ResourceLocation, T> entry : map.entrySet()) {
            if(entry.getValue().equals(card)) result = entry.getKey();
        }
        return result;
    }

    public void close() {
        if(close) return;
        close = true;
    }

    public int registeredItemCount() {
        return map.size();
    }

    public void addRegistrationHook(BiConsumer<ResourceLocation, T> hook) {
        registrationHooks.add(hook);
    }

    public Registry(ResourceLocation location, T defaultItem) {
        register(defaultItem, location);
        this.defaultItem = defaultItem;
        this.defaultLocation = location;
    }
}