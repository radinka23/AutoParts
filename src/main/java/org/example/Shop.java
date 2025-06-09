package org.example;

import java.util.ArrayList;

public class Shop {
    private String name;
    private DBService parts;

    public Shop(String name, ArrayList<Part> parts) {
        this.name = name;
        this.parts = new DBService();
    }

    public void addPart(Part part){
        this.parts.addPart(part);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", parts=" + parts +
                '}';
    }
}
