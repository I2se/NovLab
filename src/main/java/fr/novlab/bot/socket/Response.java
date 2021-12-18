package fr.novlab.bot.socket;

import com.google.gson.JsonObject;

public class Response {

    private final int requestId;
    private final int status;
    private final JsonObject content;

    public Response(int requestId, int status, JsonObject content) {
        this.requestId = requestId;
        this.status = status;
        this.content = content;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getStatus() {
        return status;
    }

    public JsonObject getContent() {
        return content;
    }
}
