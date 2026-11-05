package ru.maga.transport;

public final class SampleTransport {
    private SampleTransport() { }

    public static Transport create() throws DuplicateModelNameException {
        Transport transport = new Car("Toyota", 0);
        transport.addModel("Corolla", 18000);
        transport.addModel("Camry", 28000);
        transport.addModel("RAV4", 33000);
        return transport;
    }
}
