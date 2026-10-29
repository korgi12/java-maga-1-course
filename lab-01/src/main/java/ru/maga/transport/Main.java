package ru.maga.transport;

public final class Main {
    public static void main(String[] args) throws Exception {
        Transport car = new Car("Toyota", 0);
        car.addModel("Corolla", 15000);
        car.addModel("Camry", 25000);
        car.setModelName("Corolla", "Corolla E120");
        car.setPrice("Camry", 27000);
        Transport motorcycle = new Motorcycle("Honda", 0);
        motorcycle.addModel("CB500", 7000);
        motorcycle.addModel("CB650R", 9500);
        motorcycle.deleteModel("CB500");
        System.out.println(car.getMark());
        TransportUtils.printModels(car);
        TransportUtils.printPrices(car);
        System.out.println(TransportUtils.averagePrice(car));
        System.out.println(motorcycle.getMark());
        TransportUtils.printModels(motorcycle);
    }
}
