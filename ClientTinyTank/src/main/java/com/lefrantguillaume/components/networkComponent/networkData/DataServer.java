package com.lefrantguillaume.components.networkComponent.networkData;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lefrantguillaume.utils.stockage.Pair;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by Guillaume on 10/03/2015.
 *
 * REST client to the master server. Migrated from Jersey 1.x (EOL, JAXB-dependent)
 * to the JDK's built-in java.net.http.HttpClient + Jackson for JSON (de)serialization.
 */
public class DataServer {
    private static final String MASTER_SERVER = "http://tinytank.lefrantguillaume.com/api/client/";
    private static final String AUTH_USER = "T0N1jjOQIDmA4cJnmiT6zHvExjoSLRnbqEJ6h2zWKXLtJ9N8ygVHvkP7Sy4kqrv";
    private static final String AUTH_PASSWORD = "lMhIq0tVVwIvPKSBg8p8YbPg0zcvihBPJW6hsEGUiS6byKjoZcymXQs5urequUo";

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = buildMapper();

    private String id = null;

    public DataServer() {
    }

    private static ObjectMapper buildMapper() {
        ObjectMapper m = new ObjectMapper();
        // The DTOs expose plain fields (no consistent getters), so map by field.
        m.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return m;
    }

    private static String postJson(Object st, String path) throws Exception {
        String payload = (st == null) ? "{}" : MAPPER.writeValueAsString(st);
        String basicAuth = Base64.getEncoder()
                .encodeToString((AUTH_USER + ":" + AUTH_PASSWORD).getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MASTER_SERVER + path))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic " + basicAuth)
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
        }
        return response.body();
    }

    public static Pair<Boolean, String> authentification(String u, String p) {
        try {
            AuthSnd st = new AuthSnd(u, p, "TheSecretStringIsMuchStrongerThanOneMillionOfTanks");

            AuthRcv output = MAPPER.readValue(postJson(st, "login"), AuthRcv.class);

            if (output.getErr() != null) {
                return new Pair<>(false, output.getErr());
            } else {
                return new Pair<>(true, output.getRes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(false, e.getMessage());
        }
    }

    public static Pair<Boolean, String> getServerList() {
        try {
            ServerListRcv output = MAPPER.readValue(postJson(null, "list_servers"), ServerListRcv.class);

            if (output.getErr() != null) {
                return new Pair<>(false, output.getErr());
            } else {
                return new Pair<>(true, output.getRes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(false, e.getMessage());
        }
    }

}
