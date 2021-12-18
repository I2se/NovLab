package fr.novlab.bot.socket;

import org.json.JSONObject;

public class Response {

    private final String requestId;
    private final String statusCode;
    private final JSONObject content;

    public Response(String requestId, String statusCode, JSONObject content) {
        this.requestId = requestId;
        this.statusCode = statusCode;
        this.content = content;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public JSONObject getContent() {
        return content;
    }
}
