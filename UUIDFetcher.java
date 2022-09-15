import com.google.gson.Gson;
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

public class UUIDFetcher {

    private static String URL = "https://api.ashcon.app/mojang/v2/user/%s";
    private static final Gson gson = new Gson();
    public static Map<String, UUID> uuidCache = new HashMap<String, UUID>();
    public static Map<UUID, String> nameCache = new HashMap<UUID, String>();
    private String name;
    private UUID id;

    public static UUID getUUID(String name) {
        UUID uuid = null;

        name = name.toLowerCase();
        if (uuidCache.containsKey(name)) {
            return uuidCache.get(name);
        }

        try {
            URL url = new URL(String.format(URL, name));
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            while (bufferedReader.ready()) {
                sb.append(bufferedReader.readLine());
            }
            String content = sb.toString();
            JSONObject jsonObject = new JSONObject(content);
            uuid = UUID.fromString(jsonObject.getString("uuid"));

            uuidCache.put(name, uuid);
            nameCache.put(uuid, name);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uuid;
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
            while (bufferedReader.ready()) {
                sb.append(bufferedReader.readLine());
            }
            String content = sb.toString();
            JSONObject jsonObject = new JSONObject(content);
            name = jsonObject.getString("username");

            uuidCache.put(name, uuid);
            nameCache.put(uuid, name);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;
    }

}
