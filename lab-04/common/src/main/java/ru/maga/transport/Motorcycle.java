package ru.maga.transport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;

public class Motorcycle implements Transport {
    private static final long serialVersionUID = 1L;
    private String mark;
    private int size;
    private final Model head;
    private transient long lastModified;

    {
        lastModified = System.currentTimeMillis();
    }

    private class Model implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private double price;
        private Model prev;
        private Model next;

        private Model(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    public Motorcycle(String mark, int initialSize) {
        if (initialSize < 0) throw new IllegalArgumentException("Negative size");
        this.mark = Objects.requireNonNull(mark);
        head = new Model(null, Double.NaN);
        head.prev = head;
        head.next = head;
        for (int i = 0; i < initialSize; i++) append("Model" + (i + 1), 0);
    }

    private void touch() { lastModified = System.currentTimeMillis(); }
    public long getLastModified() { return lastModified; }
    @Override public String getMark() { return mark; }
    @Override public void setMark(String mark) { this.mark = Objects.requireNonNull(mark); touch(); }
    @Override public int getSize() { return size; }

    private void append(String name, double price) {
        Model node = new Model(name, price);
        node.prev = head.prev;
        node.next = head;
        head.prev.next = node;
        head.prev = node;
        size++;
        touch();
    }

    private Model find(String name) {
        for (Model item = head.next; item != head; item = item.next) {
            if (item.name.equals(name)) return item;
        }
        return null;
    }

    private static void checkPrice(double price) {
        if (!Double.isFinite(price) || price < 0) throw new ModelPriceOutOfBoundsException("Invalid price: " + price);
    }

    @Override public String[] getModels() {
        String[] names = new String[size];
        int index = 0;
        for (Model item = head.next; item != head; item = item.next) names[index++] = item.name;
        return names;
    }

    @Override public double[] getPrices() {
        double[] prices = new double[size];
        int index = 0;
        for (Model item = head.next; item != head; item = item.next) prices[index++] = item.price;
        return prices;
    }

    @Override public double getPrice(String name) throws NoSuchModelNameException {
        Model model = find(name);
        if (model == null) throw new NoSuchModelNameException(name);
        return model.price;
    }

    @Override public void setPrice(String name, double price) throws NoSuchModelNameException {
        checkPrice(price);
        Model model = find(name);
        if (model == null) throw new NoSuchModelNameException(name);
        model.price = price;
        touch();
    }

    @Override public void setModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        Objects.requireNonNull(newName);
        Model model = find(oldName);
        if (model == null) throw new NoSuchModelNameException(oldName);
        if (!oldName.equals(newName) && find(newName) != null) throw new DuplicateModelNameException(newName);
        model.name = newName;
        touch();
    }

    @Override public void addModel(String name, double price) throws DuplicateModelNameException {
        Objects.requireNonNull(name);
        checkPrice(price);
        if (find(name) != null) throw new DuplicateModelNameException(name);
        append(name, price);
    }

    @Override public void deleteModel(String name) throws NoSuchModelNameException {
        Model model = find(name);
        if (model == null) throw new NoSuchModelNameException(name);
        model.prev.next = model.next;
        model.next.prev = model.prev;
        size--;
        touch();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        touch();
    }
}
