package fr.novlab.bot.socket;

import org.json.JSONException;

public interface IRequestCallback {

    void execute(Response response) throws JSONException;
}
