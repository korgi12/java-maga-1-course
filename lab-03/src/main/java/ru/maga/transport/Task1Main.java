package ru.maga.transport;

public final class Task1Main {
    public static void main(String[] args) throws Exception {
        Transport transport = SampleTransport.create();
        Thread prices = new PriceThread(transport);
        Thread models = new ModelThread(transport);
        prices.setPriority(Thread.MIN_PRIORITY);
        models.setPriority(Thread.MAX_PRIORITY);
        prices.start();
        models.start();
    }
}
