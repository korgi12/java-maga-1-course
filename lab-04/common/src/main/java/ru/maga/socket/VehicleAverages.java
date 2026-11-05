package ru.maga.socket;

import ru.maga.transport.Transport;

public final class VehicleAverages {
    private VehicleAverages() {
    }

    public static double calculate(Transport[] vehicles) {
        double sum = 0;
        int count = 0;
        for (Transport vehicle : vehicles) {
            if (vehicle == null) throw new IllegalArgumentException("Vehicle is null");
            for (double price : vehicle.getPrices()) {
                sum += price;
                count++;
            }
        }
        return count == 0 ? Double.NaN : sum / count;
    }
}
