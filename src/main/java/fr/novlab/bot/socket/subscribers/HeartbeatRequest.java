package fr.novlab.bot.socket.subscribers;

import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Response;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

public class HeartbeatRequest implements Subscriber {

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException {
        String msg = response.getString("msg");
        int timeLeft = response.getInt("timeLeft");
        String question = response.getString("question");

        System.out.println("Received heartbeat request : ");
        System.out.println("msg : " + msg);
        System.out.println("timeLeft : " + timeLeft);
        System.out.println("question : " + question);

        connection.send("heartbeat:answer", (response1 -> {
            System.out.println("Heartbeat answer result : " + response1.getStatusCode());
        }), question);
    }

    @Override
    public String getChannel() {
        return "heartbeat:request";
    }
}
