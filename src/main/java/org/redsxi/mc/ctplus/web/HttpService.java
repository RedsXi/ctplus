package org.redsxi.mc.ctplus.web;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.redsxi.mc.ctplus.Collections;
import org.redsxi.mc.ctplus.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class HttpService {

    private static final Logger LOGGER = LoggerFactory.getLogger("HttpService-CrabMTR");

    private static final Gson gson = new Gson();

    static HttpServer server;

    public static void start() {
        LOGGER.info("HttpService Starting...");
        if(!isEnabled()) {
            LOGGER.warn("Not enabled, Stopping");
            return;
        }
        server.start();
        LOGGER.info("HttpService successfully started at port 2008");
    }

    public static boolean isEnabled() {
        return Objects.equals(System.getProperty("ctplus.transit_plus_svc", "false"), "true");
    }

    private static final String WEBAPI_URL;

    static {

        WEBAPI_URL = System.getProperty("ctplus.ticketserver.APIEndpointURI", "http://mtr-api.crabapi.cn");
        LOGGER.info("WebAPI url is " + WEBAPI_URL);

        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(2008), 256);
            server.createContext("/user/getOnline", exchange -> {
                exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                JsonObject object = getJsonObject();
                byte[] result = gson.toJson(object).getBytes(StandardCharsets.UTF_8);
                int size = result.length;
                exchange.sendResponseHeaders(200, size);
                exchange.getResponseBody().write(result);
                exchange.close();
            });
            server.createContext("/ticket/order", exchange -> {
                exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                if(!Objects.equals(exchange.getRequestMethod(), "POST")) {
                    exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                    String str = "{\"code\": 1, \"msg\": \"Invalid method\"}";
                    exchange.sendResponseHeaders(200, str.length());
                    exchange.getResponseBody().write(str.getBytes(StandardCharsets.UTF_8));
                    exchange.close();
                }
                InputStream stream = exchange.getRequestBody();
                JsonObject object = gson.fromJson(Arrays.toString(stream.readAllBytes()), JsonObject.class);
                if(object.has("orderId")) {
                    String orderId = object.get("orderId").getAsString();
                    JsonObject resp;
                    try {
                        resp = request(new URL(WEBAPI_URL + "/v1/verifyOrder"), "{\"orderId\": \"" + orderId + "\"}");
                        Objects.requireNonNull(resp);
                    } catch (Exception e) {
                        LOGGER.error("Error while requesting", e);
                        exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                        String str = "{\"code\": 1, \"msg\": \"Server error\"}";
                        exchange.sendResponseHeaders(200, str.length());
                        exchange.getResponseBody().write(str.getBytes(StandardCharsets.UTF_8));
                        exchange.close();
                        return;
                    }
                    if(resp.getAsJsonPrimitive("status").getAsInt() == 1) {
                        JsonObject data = resp.getAsJsonObject("data");
                        if(!Variables.INSTANCE.getPlayerList().containsKey(UUID.fromString(data.getAsJsonPrimitive("playerUUID").getAsString()))) {
                            ServerPlayer player = Variables.INSTANCE.getPlayerList().get(UUID.fromString(data.getAsJsonPrimitive("playerUUID").getAsString()));
                            ItemStack itemStack = new ItemStack(Variables.INSTANCE.getCardItemList().get(Collections.Cards.SINGLE_JOURNEY));
                            CompoundTag tag = itemStack.getTag();
                            if (tag != null) {
                                tag.putInt("Price", 50);
                                tag.putBoolean("IsUsed", false);
                            }
                            itemStack.setTag(tag);
                            itemStack.setCount(1);
                            boolean bl = player.getInventory().add(itemStack);
                            ItemEntity itemEntity;
                            if (bl && itemStack.isEmpty()) {
                                itemStack.setCount(1);
                                itemEntity = player.drop(itemStack, false);
                                if (itemEntity != null) {
                                    itemEntity.makeFakeItem();
                                }
                                //player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                player.containerMenu.broadcastChanges();
                            } else {
                                itemEntity = player.drop(itemStack, false);
                                if (itemEntity != null) {
                                    itemEntity.setNoPickUpDelay();
                                    itemEntity.setTarget(player.getUUID());
                                }
                            }

                            exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                            String str = "{\"code\": 0, \"msg\": \"\"}";
                            exchange.sendResponseHeaders(200, str.length());
                            exchange.getResponseBody().write(str.getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                        } else {
                            exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                            String str = "{\"code\": 1, \"msg\": \"player is offline\"}";
                            exchange.sendResponseHeaders(200, str.length());
                            exchange.getResponseBody().write(str.getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                        }
                    } else {
                        exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                        String str = "{\"code\": 1, \"msg\": \"orderId invalid\"}";
                        exchange.sendResponseHeaders(200, str.length());
                        exchange.getResponseBody().write(str.getBytes(StandardCharsets.UTF_8));
                        exchange.close();
                    }
                } else {
                    exchange.getRequestHeaders().set("Server", "CrabMTRTransitPlus/2.0");
                    String str = "{\"code\": 1, \"msg\": \"missing argument orderId\"}";
                    exchange.sendResponseHeaders(200, str.length());
                    exchange.getResponseBody().write(str.getBytes(StandardCharsets.UTF_8));
                    exchange.close();
                }
            });

        } catch (IOException e) {
            LOGGER.error("Cannot start", e);
        }
    }

    @NotNull
    private static JsonObject getJsonObject() {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        Map<UUID, ServerPlayer> onlinePlayers = Variables.INSTANCE.getPlayerList();
        for(Map.Entry<UUID, ServerPlayer> single : onlinePlayers.entrySet()) {
            JsonObject o = new JsonObject();
            o.addProperty("uuid", single.getKey().toString());
            o.addProperty("name", single.getValue().getName().getString());
            array.add(o);
        }
        object.add("list", array);
        return object;
    }

    private static JsonObject request(URL url, String postMsg) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(60000);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(postMsg.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        String resp = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        connection.disconnect();

        return new Gson().fromJson(resp, JsonObject.class);
    }
}
