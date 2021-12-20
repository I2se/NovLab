package fr.novlab.bot.socket.requests;

import fr.novlab.bot.Main;
import fr.novlab.bot.socket.APIConnection;
import fr.novlab.bot.socket.subscribers.heartbeat.HeartbeatRequest;
import org.slf4j.LoggerFactory;

public class UsersController {

    private APIConnection apiConnection = Main.getApiConnection();
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HeartbeatRequest.class);

    public void createUser(String discordId) {
        apiConnection.send("users:create", response -> {
            if(response.getStatusCode().equals("200")) {
                LOGGER.info("The user has been successfully created !");
            } else {
                LOGGER.error(response.getContent().getString("error"));
            }
        }, discordId);
    }

    public void deleteUser(String discordId) {
        apiConnection.send("users:delete", response -> {
            if(response.getStatusCode().equals("200")) {
                LOGGER.info("The user has been successfully delete !");
            } else {
                LOGGER.error(response.getContent().getString("error"));
            }
        }, discordId);
    }
}
