package org.redsxi.mc.ctplus.core;

public class Conversion {
    public static <T, O> T forceConvert(O original, Class<T> clazz) {
        return (T) original;
    }
}
