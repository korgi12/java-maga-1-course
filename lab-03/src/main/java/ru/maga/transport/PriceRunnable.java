package ru.maga.transport;

public class PriceRunnable implements Runnable {
    private final TransportSynchronizer synchronizer;
    public PriceRunnable(TransportSynchronizer synchronizer) { this.synchronizer = synchronizer; }
    @Override public void run() {
        try {
            while (synchronizer.canPrintPrice()) synchronizer.printPrice();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
