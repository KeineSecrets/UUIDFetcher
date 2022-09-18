import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UUIDFetcher {

    private static String URL = "https://api.ashcon.app/mojang/v2/user/%s";
    private static final Gson gson = new Gson();
    public static Map<String, UUID> uuidCache = new HashMap<String, UUID>();
    public static Map<UUID, String> nameCache = new HashMap<UUID, String>();
    private String name;
    private UUID id;

    private static ExecutorService executor;

    public static void startup() {
        executor = Executors.newFixedThreadPool(1);
    }

    public static void shutdown() {
        executor.shutdown();
    }

    public static UUID getUUID(String name) {
        UUID uuid = null;

        if (uuidCache.containsKey(name)) {
            return uuidCache.get(name);
        }

        try {
            URL url = new URL(String.format(URL, name));
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = bufferedReader.read()) != -1) sb.append((char)cp);
            String content = sb.toString();
            bufferedReader.close();
            JsonElement jsonElement = new JsonParser().parse(content);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

            uuidCache.put(name, uuid);
            nameCache.put(uuid, name);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uuid;
    }

    public static void fetchUUID(String name, Consumer<UUID> consumer) {
        executor.execute(() -> {
            UUID uuid = null;

            if (uuidCache.containsKey(name)) {
                consumer.accept(uuidCache.get(name));
                return;
            }

            try {
                URL url = new URL(String.format(URL, name));
                InputStream inputStream = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = bufferedReader.read()) != -1) sb.append((char)cp);
                String content = sb.toString();
                bufferedReader.close();
                JsonElement jsonElement = new JsonParser().parse(content);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

                uuidCache.put(name, uuid);
                nameCache.put(uuid, name);

            } catch (IOException e) {
                e.printStackTrace();
            }
            consumer.accept(uuid);
        });
    }

    public static void fetchName(UUID uuid, Consumer<String> consumer) {
        executor.execute(() -> {
            String name = null;

            if (nameCache.containsKey(uuid)) {
                consumer.accept(nameCache.get(uuid));
                return;
            }

            try {
                URL url = new URL(String.format(URL, uuid));
                InputStream inputStream = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = bufferedReader.read()) != -1) sb.append((char)cp);
                String content = sb.toString();
                bufferedReader.close();
                JsonElement jsonElement = new JsonParser().parse(content);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                name = (jsonObject.get("username").getAsString());

                uuidCache.put(name, uuid);
                nameCache.put(uuid, name);

            } catch (IOException e) {
                e.printStackTrace();
            }
            consumer.accept(name);
        });
    }

    public static String getName(UUID uuid) {
        String name = null;

        if (nameCache.containsKey(uuid)) {
            return nameCache.get(uuid);
        }

        try {
            URL url = new URL(String.format(URL, uuid));
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = bufferedReader.read()) != -1) sb.append((char)cp);
            String content = sb.toString();
            JsonElement jsonElement = new JsonParser().parse(content);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            name = jsonObject.get("username").getAsString();

            uuidCache.put(name, uuid);
            nameCache.put(uuid, name);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;
    }

    private UUID parseUUIDFromString(String uuidAsString) {
        String[] parts = {
                "0x" + uuidAsString.substring(0, 8),
                "0x" + uuidAsString.substring(8, 12),
                "0x" + uuidAsString.substring(12, 16),
                "0x" + uuidAsString.substring(16, 20),
                "0x" + uuidAsString.substring(20, 32)
        };

        long mostSigBits = Long.decode(parts[0]);
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(parts[1]);
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(parts[2]);

        long leastSigBits = Long.decode(parts[3]);
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(parts[4]);

        return new UUID(mostSigBits, leastSigBits);
    }

}
