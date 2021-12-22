package fr.novlab.bot.socket.subscribers.heartbeat;

import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.Response;
import fr.novlab.bot.socket.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class HeartbeatRequest implements Subscriber {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HeartbeatRequest.class);

    @Override
    public void on(APIConnection connection, JSONObject response) throws JSONException, ExecutionException, InterruptedException {
        String msg = response.getString("msg");
        int timeLeft = response.getInt("timeLeft");
        String question = response.getString("question");

        //LOGGER.info("Rcv HB Req : " + msg + " // " + timeLeft + " // " + question);

        connection.send("heartbeat:answer", response1 -> {
            //LOGGER.info("HB Result : " + response1.getStatusCode() + (response1.getStatusCode().equals("500") ? " // " + response1.getContent().getString("error") : ""));
        }, question);
    }

    @Override
    public String getChannel() {
        return "heartbeat:request";
    }
}
