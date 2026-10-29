package ru.maga.transport;

public final class TransportSynchronizer {
    private final Transport transport;
    private final Object lock = new Object();
    private int current;
    private boolean priceTurn;

    public TransportSynchronizer(Transport transport) { this.transport = transport; }

    public void printModel() throws InterruptedException {
        synchronized (lock) {
            while (priceTurn && current < transport.getSize()) lock.wait();
            if (current >= transport.getSize()) return;
            System.out.println("Model: " + transport.getModels()[current]);
            priceTurn = true;
            lock.notifyAll();
        }
    }

    public void printPrice() throws InterruptedException {
        synchronized (lock) {
            while (!priceTurn && current < transport.getSize()) lock.wait();
            if (current >= transport.getSize()) return;
            System.out.println("Price: " + transport.getPrices()[current++]);
            priceTurn = false;
            lock.notifyAll();
        }
    }

    public boolean canPrintModel() {
        synchronized (lock) { return current < transport.getSize(); }
    }

    public boolean canPrintPrice() {
        synchronized (lock) { return current < transport.getSize(); }
    }
}
