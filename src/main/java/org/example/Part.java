package org.example;

import java.util.ArrayList;

public class Part {
    private String name;
    private String code;
    private PartCategory category;
    private ArrayList<Car> supportedCars;
    private double buyPrice;
    private double sellPrice;
    private Manufacturer manufacturer;

    public Part() {
    }

    public Part(String name, String code, PartCategory category, ArrayList<Car> supportedCars, double buyPrice, double sellPrice, Manufacturer manufacturer) {
        this.name = name;
        this.code = code;
        this.category = category;
        this.supportedCars = supportedCars;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.manufacturer = manufacturer;
    }

    public void addSupportedCar(Car car) {
        this.supportedCars.add(car);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category.name();
    }

    public void setCategory(PartCategory category) {
        this.category = category;
    }

    public ArrayList<Car> getSupportedCars() {
        return supportedCars;
    }

    public void setSupportedCars(ArrayList<Car> supportedCars) {
        this.supportedCars = supportedCars;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Part{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", category=" + category +
                ", supportedCars=" + supportedCars +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
