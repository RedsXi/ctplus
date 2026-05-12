package org.redsxi.transitplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import org.redsxi.transitplus.client.MainClient;
import org.redsxi.transitplus.common.MainCommon;
import org.redsxi.transitplus.server.MainServer;

public class Entrypoint implements
        ModInitializer,
        ClientModInitializer,
        DedicatedServerModInitializer
{
    public void onInitializeClient() {
        MainClient.entry();
    }
    public void onInitializeServer() {
        MainServer.entry();
    }
    public void onInitialize() {
        MainCommon.entry();
    }
}
