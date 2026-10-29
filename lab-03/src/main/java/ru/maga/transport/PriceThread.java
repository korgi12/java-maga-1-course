package ru.maga.transport;

public class PriceThread extends Thread {
    private final Transport transport;
    public PriceThread(Transport transport) { this.transport = transport; }
    @Override public void run() {
        for (double price : transport.getPrices()) System.out.println("Price: " + price);
    }
}
