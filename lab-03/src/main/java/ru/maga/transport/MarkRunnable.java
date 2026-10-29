package ru.maga.transport;

public final class MarkRunnable implements Runnable {
    private final Transport transport;
    public MarkRunnable(Transport transport) { this.transport = transport; }
    @Override public void run() { System.out.println(transport.getMark()); }
}
