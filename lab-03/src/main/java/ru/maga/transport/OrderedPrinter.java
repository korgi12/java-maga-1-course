package ru.maga.transport;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class OrderedPrinter {
    private final Transport transport;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition modelsDone = lock.newCondition();
    private boolean finishedModels;

    public OrderedPrinter(Transport transport) { this.transport = transport; }

    public Runnable models() {
        return () -> {
            lock.lock();
            try {
                for (String name : transport.getModels()) System.out.println("Model: " + name);
                finishedModels = true;
                modelsDone.signalAll();
            } finally { lock.unlock(); }
        };
    }

    public Runnable prices() {
        return () -> {
            lock.lock();
            try {
                while (!finishedModels) modelsDone.await();
                for (double price : transport.getPrices()) System.out.println("Price: " + price);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } finally { lock.unlock(); }
        };
    }
}
