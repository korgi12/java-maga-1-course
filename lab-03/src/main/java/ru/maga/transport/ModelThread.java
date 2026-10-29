package ru.maga.transport;

public class ModelThread extends Thread {
    private final Transport transport;
    public ModelThread(Transport transport) { this.transport = transport; }
    @Override public void run() {
        for (String model : transport.getModels()) System.out.println("Model: " + model);
    }
}
