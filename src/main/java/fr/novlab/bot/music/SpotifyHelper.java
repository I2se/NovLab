package fr.novlab.bot.music;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.novlab.bot.config.Config;
import se.michaelthelin.spotify.SpotifyApi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.function.Consumer;
import java.util.function.Function;

public class SpotifyHelper {

    private static SpotifyApi spotifyApi;

    public static <R> R doRequest(ExceptionFunction<SpotifyApi, R> consumer) throws Exception {
        try {
            return consumer.apply(spotifyApi);
        } catch (Exception ignored) {
            System.out.println("ERROR HAPPENED DURING SPOTIFY REQUEST");
            ignored.printStackTrace();
            System.out.println("RE-LOGIN, AND RE-TRYING...");
        }

        login();
        return consumer.apply(spotifyApi);
    }

    public static void login() {
        spotifyApi = new SpotifyApi.Builder().setAccessToken(requestAccessToken()).build();
    }

    private static String requestAccessToken() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://accounts.spotify.com/api/token").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((Config.CLIENTID + ":" + Config.CLIENTSECRET).getBytes()));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes("grant_type=client_credentials");
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
            return json.get("access_token").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface ExceptionFunction<T, R> {

        R apply(T value) throws Exception;
    }
}

