package ru.maga.transport;

import java.io.Serializable;

public interface Transport extends Serializable {
    String getMark();
    void setMark(String mark);
    String[] getModels();
    double[] getPrices();
    double getPrice(String name) throws NoSuchModelNameException;
    void setPrice(String name, double price) throws NoSuchModelNameException;
    void setModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException;
    void addModel(String name, double price) throws DuplicateModelNameException;
    void deleteModel(String name) throws NoSuchModelNameException;
    int getSize();
}
