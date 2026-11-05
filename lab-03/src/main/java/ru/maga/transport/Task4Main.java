package ru.maga.transport;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Task4Main {
    public static void main(String[] args) throws Exception {
        List<Transport> transports = List.of(
                SampleTransport.create(),
                new Motorcycle("Honda", 0),
                new Car("BMW", 0),
                new Motorcycle("Yamaha", 0)
        );
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            for (Transport transport : transports) {
                executor.execute(new MarkRunnable(transport));
            }
        } finally {
            executor.shutdown();
        }
    }
}
