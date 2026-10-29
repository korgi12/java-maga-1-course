package ru.maga.transport;

import java.util.Arrays;
import java.util.Objects;

public class Car implements Transport {
    private static final long serialVersionUID = 1L;
    private String mark;
    private Model[] models;

    private class Model implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private double price;

        private Model(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    public Car(String mark, int size) {
        if (size < 0) throw new IllegalArgumentException("Negative size");
        this.mark = Objects.requireNonNull(mark);
        models = new Model[size];
        for (int i = 0; i < size; i++) models[i] = new Model("Model" + (i + 1), 0);
    }

    @Override public String getMark() { return mark; }
    @Override public void setMark(String mark) { this.mark = Objects.requireNonNull(mark); }
    @Override public int getSize() { return models.length; }

    @Override public String[] getModels() {
        String[] names = new String[models.length];
        for (int i = 0; i < models.length; i++) names[i] = models[i].name;
        return names;
    }

    @Override public double[] getPrices() {
        double[] prices = new double[models.length];
        for (int i = 0; i < models.length; i++) prices[i] = models[i].price;
        return prices;
    }

    private int indexOf(String name) {
        for (int i = 0; i < models.length; i++) {
            if (models[i].name.equals(name)) return i;
        }
        return -1;
    }

    private static void checkPrice(double price) {
        if (!Double.isFinite(price) || price < 0) throw new ModelPriceOutOfBoundsException("Invalid price: " + price);
    }

    @Override public double getPrice(String name) throws NoSuchModelNameException {
        int index = indexOf(name);
        if (index < 0) throw new NoSuchModelNameException(name);
        return models[index].price;
    }

    @Override public void setPrice(String name, double price) throws NoSuchModelNameException {
        checkPrice(price);
        int index = indexOf(name);
        if (index < 0) throw new NoSuchModelNameException(name);
        models[index].price = price;
    }

    @Override public void setModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        Objects.requireNonNull(newName);
        int index = indexOf(oldName);
        if (index < 0) throw new NoSuchModelNameException(oldName);
        if (!oldName.equals(newName) && indexOf(newName) >= 0) throw new DuplicateModelNameException(newName);
        models[index].name = newName;
    }

    @Override public void addModel(String name, double price) throws DuplicateModelNameException {
        Objects.requireNonNull(name);
        checkPrice(price);
        if (indexOf(name) >= 0) throw new DuplicateModelNameException(name);
        models = Arrays.copyOf(models, models.length + 1);
        models[models.length - 1] = new Model(name, price);
    }

    @Override public void deleteModel(String name) throws NoSuchModelNameException {
        int index = indexOf(name);
        if (index < 0) throw new NoSuchModelNameException(name);
        Model[] remaining = new Model[models.length - 1];
        System.arraycopy(models, 0, remaining, 0, index);
        System.arraycopy(models, index + 1, remaining, index, remaining.length - index);
        models = Arrays.copyOf(remaining, remaining.length);
    }
}
