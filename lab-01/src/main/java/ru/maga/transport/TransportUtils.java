package ru.maga.transport;

import java.util.Arrays;

public final class TransportUtils {
    private TransportUtils() { }

    public static double averagePrice(Transport transport) {
        return Arrays.stream(transport.getPrices()).average().orElse(0);
    }

    public static void printModels(Transport transport) {
        for (String model : transport.getModels()) System.out.println(model);
    }

    public static void printPrices(Transport transport) {
        for (double price : transport.getPrices()) System.out.println(price);
    }
}
