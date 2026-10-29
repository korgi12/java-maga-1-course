package ru.maga.transport;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class TransportIO {
    private TransportIO() { }

    private static void writeString(DataOutputStream out, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static String readString(DataInputStream in) throws IOException {
        int length = in.readInt();
        if (length < 0 || length > 1_000_000) throw new IOException("Invalid string length");
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void outputTransport(Transport transport, OutputStream stream) throws IOException {
        DataOutputStream out = new DataOutputStream(stream);
        writeString(out, transport.getMark());
        out.writeInt(transport.getSize());
        String[] names = transport.getModels();
        double[] prices = transport.getPrices();
        for (int i = 0; i < names.length; i++) {
            writeString(out, names[i]);
            out.writeDouble(prices[i]);
        }
        out.flush();
    }

    public static Transport inputTransport(InputStream stream) throws IOException {
        DataInputStream in = new DataInputStream(stream);
        String mark = readString(in);
        int count = in.readInt();
        if (count < 0 || count > 1_000_000) throw new IOException("Invalid model count");
        Transport result = new Car(mark, 0);
        for (int i = 0; i < count; i++) {
            try {
                result.addModel(readString(in), in.readDouble());
            } catch (DuplicateModelNameException | ModelPriceOutOfBoundsException ex) {
                throw new IOException("Invalid transport", ex);
            }
        }
        return result;
    }

    public static void writeTransport(Transport transport, Writer writer) {
        PrintWriter out = new PrintWriter(writer);
        out.println(transport.getMark());
        out.println(transport.getSize());
        String[] names = transport.getModels();
        double[] prices = transport.getPrices();
        for (int i = 0; i < names.length; i++) {
            out.println(names[i]);
            out.println(prices[i]);
        }
        out.flush();
        if (out.checkError()) throw new UncheckedIOException(new IOException("Text output failed"));
    }

    public static Transport readTransport(Reader reader) throws IOException {
        BufferedReader in = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        String mark = in.readLine();
        if (mark == null) throw new EOFException("Missing mark");
        String sizeLine = in.readLine();
        if (sizeLine == null) throw new EOFException("Missing size");
        int count;
        try { count = Integer.parseInt(sizeLine); }
        catch (NumberFormatException ex) { throw new IOException("Invalid model count", ex); }
        if (count < 0 || count > 1_000_000) throw new IOException("Invalid model count");
        Transport result = new Motorcycle(mark, 0);
        for (int i = 0; i < count; i++) {
            String name = in.readLine();
            String priceLine = in.readLine();
            if (name == null || priceLine == null) throw new EOFException("Incomplete model");
            try {
                result.addModel(name, Double.parseDouble(priceLine));
            } catch (NumberFormatException | DuplicateModelNameException | ModelPriceOutOfBoundsException ex) {
                throw new IOException("Invalid model", ex);
            }
        }
        return result;
    }
}
