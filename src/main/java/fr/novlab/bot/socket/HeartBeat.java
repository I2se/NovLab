package fr.novlab.bot.socket;

import fr.novlab.bot.Main;
import io.socket.client.Socket;

public class HeartBeat {

    public static Socket socket = Main.getSocket();

    public static void main(String[] args) {
        socket.on("heartbeat:request", objects -> {

        });
    }
}
