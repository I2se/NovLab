package fr.novlab.bot.socket;

import fr.novlab.bot.socket.subscribers.heartbeat.HeartbeatRequest;
import fr.novlab.bot.socket.subscribers.heartbeat.HeartbeatTimeout;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class APIConnection {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(APIConnection.class);

    private final AppType appType;
    private final String apiKey;

    private Socket socket;
    private Map<String, Map<String, Pair<Thread, IRequestCallback>>> requests;

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
                for (Object object : objects) {
                    LOGGER.error("Error during connection: " + object);
                }
            });

            this.registerSubscribers();
            this.requests = new HashMap<>();
            this.registerRequestChannels();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // generate random alpha numeric string
    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rndCharAt = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            sb.append(ALPHA_NUMERIC_STRING.charAt(rndCharAt));
        }
        return sb.toString();
    }

    public void send(String event, IRequestCallback callback, Object... args) {
        String requestId = this.generateRandomString(64);

        if (callback != null) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(60000);

                    this.requests.get(event).remove(requestId);
                } catch (InterruptedException e) {}
            });

            this.requests.get(event).put(requestId, Pair.of(thread, callback));

            thread.start();
        }

        // Prepend request id to args
        final int length = args.length;
        args = java.util.Arrays.copyOf(args, length + 1);
        System.arraycopy(args, 0, args, 1, length);
        args[0] = requestId;

        this.socket.emit(event, args);
    }

    public void registerSubscribers() {
        this.registerSubscriber(new HeartbeatRequest());
        this.registerSubscriber(new HeartbeatTimeout());
    }

    public void registerSubscriber(Subscriber subscriber) {
        this.socket.on(subscriber.getChannel(), (Object... objects) -> {
            try {
                subscriber.on(this, (JSONObject) objects[0]);
            } catch (Exception e) {}
        });
    }

    public void registerRequestChannels() {
        this.registerRequestChannel("heartbeat:answer");
        this.registerRequestChannel("users:create");
        this.registerRequestChannel("users:get");
        this.registerRequestChannel("users:delete");
    }

    public void registerRequestChannel(String channel) {
        Map<String, Pair<Thread, IRequestCallback>> requests = new HashMap<>();
        this.requests.put(channel, requests);

        this.socket.on(channel, (Object... objects) -> {
            try {
                JSONObject jsonResponse = (JSONObject) objects[0];
                Response response = new Response(
                    jsonResponse.getString("requestId"),
                    jsonResponse.getInt("statusCode"),
                    jsonResponse.getJSONObject("content")
                );

                if (requests.containsKey(response.getRequestId())) {
                    Pair<Thread, IRequestCallback> request = requests.remove(response.getRequestId());

                    request.getLeft().interrupt();
                    request.getRight().execute(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void end() {
        if (this.socket != null && this.socket.connected()) {
            this.socket.disconnect();
        }
    }
}
