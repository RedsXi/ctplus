package org.redsxi.mc.ctplus.web;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.redsxi.mc.ctplus.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
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
        return Objects.equals(System.getProperty("cgcem.ticketsvr", "false"), "true");
    }

    static {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(2008), 256);
            server.createContext("/user/getOnline", exchange -> {
                JsonObject object = new JsonObject();
                JsonArray array = new JsonArray();
                Map<UUID, String> onlinePlayers = Variables.INSTANCE.getPlayerList();
                for(Map.Entry<UUID, String> single : onlinePlayers.entrySet()) {
                    JsonObject o = new JsonObject();
                    o.addProperty("uuid", single.getKey().toString());
                    o.addProperty("name", single.getValue());
                    array.add(o);
                }
                object.add("list", array);
                byte[] result = gson.toJson(object).getBytes(StandardCharsets.UTF_8);
                int size = result.length;
                exchange.sendResponseHeaders(200, size);
                exchange.getResponseBody().write(result);
                exchange.close();
            });
            server.createContext("/tickets/give", exchange -> {
                if(!Objects.equals(exchange.getRequestMethod(), "POST")) {
                    Headers headers = exchange.getRequestHeaders();
                    exchange.sendResponseHeaders(405, 0);
                    exchange.close();
                }
                InputStream stream = exchange.getRequestBody();
                JsonObject object = gson.fromJson(Arrays.toString(stream.readAllBytes()), JsonObject.class);

            });

        } catch (IOException e) {

        }
    }
}
