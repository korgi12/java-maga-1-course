package ru.maga.transport;

public final class Task1Main {
    public static void main(String[] args) throws Exception {
        Transport transport = new Car("Toyota", 0);
        for (int i = 1; i <= 20; i++) {
            transport.addModel("Model-" + i, 10000 + i * 1000);
        }
        Thread prices = new PriceThread(transport);
        Thread models = new ModelThread(transport);
        prices.setPriority(Thread.MIN_PRIORITY);
        models.setPriority(Thread.MAX_PRIORITY);
        prices.start();
        models.start();
    }
}
