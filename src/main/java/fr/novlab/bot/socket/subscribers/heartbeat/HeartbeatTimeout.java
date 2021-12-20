package fr.novlab.bot.socket.subscribers.heartbeat;

import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class HeartbeatTimeout implements Subscriber {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HeartbeatTimeout.class);

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        LOGGER.warn("TIMEOUT: " + response.getString("error"));
    }

    @Override
    public String getChannel() {
        return "heartbeat:timeout";
    }
}
