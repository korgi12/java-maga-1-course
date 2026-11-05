package ru.maga.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class SequentialServerMain {
    private SequentialServerMain() {
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Sequential server started on " + port);
            while (true) {
                Socket client = server.accept();
                try {
                    ServerHandler.handle(client);
                } catch (Exception exception) {
                    System.err.println(exception.getMessage());
                }
            }
        }
    }
}
