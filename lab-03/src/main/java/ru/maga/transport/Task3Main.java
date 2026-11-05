package ru.maga.transport;

public final class Task3Main {
    public static void main(String[] args) throws Exception {
        OrderedPrinter ordered = new OrderedPrinter(SampleTransport.create());
        Thread models = new Thread(ordered.models());
        Thread prices = new Thread(ordered.prices());
        prices.start();
        models.start();
    }
}
