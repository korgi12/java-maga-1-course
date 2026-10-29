package ru.maga.transport;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;

public final class Main {
    private static boolean equivalent(Transport left, Transport right) {
        return left.getMark().equals(right.getMark())
            && Arrays.equals(left.getModels(), right.getModels())
            && Arrays.equals(left.getPrices(), right.getPrices());
    }

    public static void main(String[] args) throws Exception {
        Transport original = new Motorcycle("Yamaha", 0);
        original.addModel("MT-07", 9000);
        original.addModel("R3", 6500);
        Path directory = Files.createTempDirectory("lab-02-");
        Path binary = directory.resolve("transport.bin");
        try (OutputStream out = Files.newOutputStream(binary)) { TransportIO.outputTransport(original, out); }
        Transport restoredBinary;
        try (InputStream in = Files.newInputStream(binary)) { restoredBinary = TransportIO.inputTransport(in); }
        Path text = directory.resolve("transport.txt");
        try (Writer out = Files.newBufferedWriter(text)) { TransportIO.writeTransport(original, out); }
        Transport restoredText;
        try (Reader in = Files.newBufferedReader(text)) { restoredText = TransportIO.readTransport(in); }
        Path serialized = directory.resolve("transport.ser");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(serialized))) {
            out.writeObject(original);
        }
        Transport deserialized;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(serialized))) {
            deserialized = (Transport) in.readObject();
        }
        System.out.println("Binary: " + equivalent(original, restoredBinary));
        System.out.println("Text: " + equivalent(original, restoredText));
        System.out.println("Serialization: " + equivalent(original, deserialized));
        TransportIO.writeTransport(original, new OutputStreamWriter(System.out));
        if (args.length > 0 && args[0].equals("stdin")) {
            Transport fromConsole = TransportIO.readTransport(new InputStreamReader(System.in));
            TransportUtils.printModels(fromConsole);
        }
    }
}
