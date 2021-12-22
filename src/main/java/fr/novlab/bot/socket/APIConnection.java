package fr.novlab.bot.socket;

import fr.novlab.bot.socket.subscribers.guilds.GuildsCreate;
import fr.novlab.bot.socket.subscribers.guilds.GuildsDelete;
import fr.novlab.bot.socket.subscribers.guilds.GuildsEdit;
import fr.novlab.bot.socket.subscribers.guilds.GuildsGet;
import fr.novlab.bot.socket.subscribers.heartbeat.HeartbeatRequest;
import fr.novlab.bot.socket.subscribers.heartbeat.HeartbeatTimeout;
import fr.novlab.bot.socket.subscribers.playlists.PlaylistsCreate;
import fr.novlab.bot.socket.subscribers.playlists.PlaylistsDelete;
import fr.novlab.bot.socket.subscribers.playlists.PlaylistsEdit;
import fr.novlab.bot.socket.subscribers.playlists.PlaylistsGet;
import fr.novlab.bot.socket.subscribers.users.UsersCreate;
import fr.novlab.bot.socket.subscribers.users.UsersDelete;
import fr.novlab.bot.socket.subscribers.users.UsersGet;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class APIConnection {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(APIConnection.class);

    private final AppType appType;
    private final String apiKey;

    private Socket socket;
    private Map<String, Map<String, Pair<Thread, IRequestCallback>>> requests;
    private Cache cache;

    public APIConnection(AppType appType, String apiKey) {
        this.appType = appType;
        this.apiKey = apiKey;
    }

    public void start(String uri) {
        try {
            this.cache = new Cache(this);

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

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(60000);

                this.requests.get(event).remove(requestId);
            } catch (InterruptedException e) {}
        });

        this.requests.get(event).put(requestId, Pair.of(thread, callback));

        thread.start();

        // Prepend request id to args
        final int length = args.length;
        args = java.util.Arrays.copyOf(args, length + 1);
        System.arraycopy(args, 0, args, 1, length);
        args[0] = requestId;

        this.socket.emit(event, args);
    }

    public CompletableFuture<Response> sendFuture(String event, Object... args) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final Response[] responseResult = { null };
                CountDownLatch latch = new CountDownLatch(1);
                this.send(event, response -> {
                    responseResult[0] = response;
                    latch.countDown();
                }, args);

                latch.await();
                return responseResult[0];
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Response sendSyncFuture(String event, Object... args) {
        try {
            return this.sendFuture(event, args).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerSubscribers() {
        this.registerSubscriber(new HeartbeatRequest());
        this.registerSubscriber(new HeartbeatTimeout());

        this.registerSubscriber(new UsersCreate());
        this.registerSubscriber(new UsersDelete());
        this.registerSubscriber(new UsersGet());

        this.registerSubscriber(new GuildsCreate());
        this.registerSubscriber(new GuildsDelete());
        this.registerSubscriber(new GuildsGet());
        this.registerSubscriber(new GuildsEdit());

        this.registerSubscriber(new PlaylistsCreate());
        this.registerSubscriber(new PlaylistsDelete());
        this.registerSubscriber(new PlaylistsGet());
        this.registerSubscriber(new PlaylistsEdit());
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
        this.registerRequestChannel("users:delete");
        this.registerRequestChannel("users:get");

        this.registerRequestChannel("guilds:create");
        this.registerRequestChannel("guilds:delete");
        this.registerRequestChannel("guilds:get");
        this.registerRequestChannel("guilds:edit");

        this.registerRequestChannel("playlists:create");
        this.registerRequestChannel("playlists:delete");
        this.registerRequestChannel("playlists:get");
        this.registerRequestChannel("playlists:edit");
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

    public Cache getCache() {
        return cache;
    }
}
