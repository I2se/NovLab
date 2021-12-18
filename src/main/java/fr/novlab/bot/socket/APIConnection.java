package fr.novlab.bot.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

public class APIConnection {

    private final AppType appType;
    private final String apiKey;

    private Socket socket;

    public APIConnection(AppType appType, String apiKey) {
        this.appType = appType;
        this.apiKey = apiKey;
    }

    public void start(String uri) {
        try {
            this.socket = IO.socket(uri, IO.Options.builder()
                .setAuth(new HashMap<>() {{
                    put("app_type", appType.getName());
                    put("api_key", apiKey);
                }})
                .build());
            this.socket.connect();

            this.socket.on(Socket.EVENT_CONNECT_ERROR, objects -> {
                System.out.println(Arrays.toString(objects));
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void end() {
        if (this.socket != null && this.socket.connected()) {
            this.socket.disconnect();
        }
    }
}
