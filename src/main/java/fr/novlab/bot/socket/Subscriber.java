package fr.novlab.bot.socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public interface Subscriber {

    void on(APIConnection connection, JSONObject response) throws JSONException, ExecutionException, InterruptedException;

    String getChannel();
}
