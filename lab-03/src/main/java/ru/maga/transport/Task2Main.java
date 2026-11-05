package ru.maga.transport;

public final class Task2Main {
    public static void main(String[] args) throws Exception {
        TransportSynchronizer synchronizer = new TransportSynchronizer(SampleTransport.create());
        Thread models = new Thread(new ModelRunnable(synchronizer));
        Thread prices = new Thread(new PriceRunnable(synchronizer));
        models.start();
        prices.start();
    }
}
