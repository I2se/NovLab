package fr.novlab.bot.socket;

import org.json.JSONObject;

public class Response {

    private final String requestId;
    private final int statusCode;
    private final JSONObject content;

    public Response(String requestId, int statusCode, JSONObject content) {
        this.requestId = requestId;
        this.statusCode = statusCode;
        this.content = content;
    }

    public String getRequestId() {
        return requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public JSONObject getContent() {
        return content;
    }
}
