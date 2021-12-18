package fr.novlab.bot.socket.subscribers;

import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONObject;

public class HeartbeatTimeout implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) {
        System.out.println("TIMEOUT!");
    }

    @Override
    public String getChannel() {
        return "heartbeat:timeout";
    }
}
