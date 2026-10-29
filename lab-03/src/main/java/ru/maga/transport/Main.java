package ru.maga.transport;

import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;

public final class Main {
    private static Transport makeTransport() throws DuplicateModelNameException {
        Transport transport = new Car("Toyota", 0);
        transport.addModel("Corolla", 18000);
        transport.addModel("Camry", 28000);
        transport.addModel("RAV4", 33000);
        return transport;
    }

    private static void join(Thread... threads) throws InterruptedException {
        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();
    }

    public static void main(String[] args) throws Exception {
        Transport transport = makeTransport();
        Thread prices = new PriceThread(transport);
        Thread models = new ModelThread(transport);
        prices.setPriority(Thread.MIN_PRIORITY);
        models.setPriority(Thread.MAX_PRIORITY);
        join(prices, models);

        TransportSynchronizer synchronizer = new TransportSynchronizer(transport);
        join(new Thread(new ModelRunnable(synchronizer)), new Thread(new PriceRunnable(synchronizer)));

        OrderedPrinter ordered = new OrderedPrinter(transport);
        join(new Thread(ordered.prices()), new Thread(ordered.models()));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Transport> transports = List.of(transport, new Motorcycle("Honda", 0), new Car("BMW", 0), new Motorcycle("Yamaha", 0));
            for (Transport current : transports) executor.submit(new MarkRunnable(current));
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) throw new IllegalStateException("Pool timeout");
        }

        Path directory = Path.of("marks");
        if (!Files.isDirectory(directory)) directory = Path.of("lab-03", "marks");
        BlockingQueue<Transport> queue = new ArrayBlockingQueue<>(2);
        ExecutorService producers = Executors.newFixedThreadPool(5);
        try {
            for (int i = 0; i < 5; i++) {
                Path file = directory.resolve("mark-" + i + ".txt");
                producers.execute(new FileTransportProducer(file, queue));
            }
            for (int i = 0; i < 5; i++) System.out.println(queue.take().getMark());
        } finally {
            producers.shutdownNow();
        }
    }
}
