package ru.maga.socket;

import ru.maga.transport.Transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public final class ServerHandler {
    private ServerHandler() {
    }

    public static void handle(Socket socket) throws IOException, ClassNotFoundException {
        try (socket;
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            Object request = input.readObject();
            if (!(request instanceof Transport[] vehicles)) {
                throw new IOException("Expected a Transport array");
            }
            output.writeDouble(VehicleAverages.calculate(vehicles));
            output.flush();
        }
    }
}
