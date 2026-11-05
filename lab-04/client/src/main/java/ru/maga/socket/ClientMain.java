package ru.maga.socket;

import ru.maga.transport.Car;
import ru.maga.transport.Motorcycle;
import ru.maga.transport.Transport;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class ClientMain {
    private ClientMain() {
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5000;

        Transport car = new Car("Toyota", 0);
        car.addModel("Corolla", 15000);
        car.addModel("Camry", 25000);

        Transport motorcycle = new Motorcycle("Honda", 0);
        motorcycle.addModel("CB500", 7000);
        motorcycle.addModel("CB650R", 9500);

        Transport[] vehicles = {car, motorcycle};
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {
            output.writeObject(vehicles);
            output.flush();
            System.out.println(input.readDouble());
        }
    }
}
