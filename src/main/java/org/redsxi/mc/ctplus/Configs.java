package org.redsxi.mc.ctplus;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.redsxi.config.Config;

public final class Configs {
    private Configs() {}

    public static final Config COMMON_CONFIG = new Config();

    @Environment(EnvType.CLIENT)
    public static final Config CLIENT_CONFIG = new Config();


}
