package fr.novlab.bot.socket;

import org.json.JSONException;
import org.json.JSONObject;

public interface Subscriber {

    void on(APIConnection connection, JSONObject response) throws JSONException;

    String getChannel();
}
