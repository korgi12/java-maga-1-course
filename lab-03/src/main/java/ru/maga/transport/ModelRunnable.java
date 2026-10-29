package ru.maga.transport;

public class ModelRunnable implements Runnable {
    private final TransportSynchronizer synchronizer;
    public ModelRunnable(TransportSynchronizer synchronizer) { this.synchronizer = synchronizer; }
    @Override public void run() {
        try {
            while (synchronizer.canPrintModel()) synchronizer.printModel();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
