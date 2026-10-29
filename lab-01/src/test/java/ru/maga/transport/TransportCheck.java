package ru.maga.transport;

import java.util.Arrays;

public final class TransportCheck {
    private static void check(boolean condition) {
        if (!condition) throw new AssertionError();
    }

    private static void verify(Transport transport) throws Exception {
        check(transport.getSize() == 2);
        check(Arrays.equals(transport.getModels(), new String[] {"Model1", "Model2"}));
        transport.setModelName("Model1", "Roadster");
        transport.setPrice("Roadster", 12000);
        check(transport.getPrice("Roadster") == 12000);
        transport.addModel("City", 3000);
        check(transport.getSize() == 3);
        check(TransportUtils.averagePrice(transport) == 5000);
        try { transport.addModel("City", 100); throw new AssertionError(); }
        catch (DuplicateModelNameException expected) { }
        try { transport.setPrice("City", -1); throw new AssertionError(); }
        catch (ModelPriceOutOfBoundsException expected) { }
        try { transport.deleteModel("Unknown"); throw new AssertionError(); }
        catch (NoSuchModelNameException expected) { }
        try { transport.setModelName("City", "Roadster"); throw new AssertionError(); }
        catch (DuplicateModelNameException expected) { }
        transport.deleteModel("Roadster");
        check(transport.getSize() == 2);
        check(Arrays.equals(transport.getModels(), new String[] {"Model2", "City"}));
    }

    public static void main(String[] args) throws Exception {
        verify(new Car("Toyota", 2));
        verify(new Motorcycle("Yamaha", 2));
        Motorcycle motorcycle = new Motorcycle("Honda", 0);
        check(motorcycle.getLastModified() > 0);
        motorcycle.addModel("CB500", 4500);
        check(motorcycle.getLastModified() > 0);
    }
}
