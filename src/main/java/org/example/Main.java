package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBService();

        try {
            // 1. Създаване на нов производител и част
            Manufacturer manufacturer = new Manufacturer("ATE", "Germany", "ate@example.com", "0987654321", "9876543210");
            ArrayList<Car> supportedCars = new ArrayList<>();
            supportedCars.add(new Car("Volkswagen Golf", "VW", 2018));
            supportedCars.add(new Car("Skoda Octavia", "Skoda", 2017));

            Part newPart = new Part(
                    "Disk Brake Set",
                    "BP002",
                    PartCategory.BRAKES,
                    supportedCars,
                    50.0,
                    75.0,
                    manufacturer
            );

            // 2. Добавяне на част
            dbService.addPart(newPart);
            System.out.println(" Added part: " + newPart.getName());

            // 3. Извличане и показване на всички части
            List<Part> allParts = dbService.getAll();
            System.out.println("\n All Parts:");
            for (Part part : allParts) {
                System.out.println(" - " + part.getName() + " (" + part.getCode() + ")");
            }

            // 4. Търсене на част по име
            List<Part> searchedParts = dbService.searchPartsByName("Brake");
            System.out.println("\n Search Results:");
            for (Part part : searchedParts) {
                System.out.println(" - " + part.getName());
            }

            // 5. Актуализиране на част
            newPart.setBuyPrice(55.0);
            newPart.setSellPrice(80.0);
            dbService.updatePart(newPart);
            System.out.println("\n Updated part: " + newPart.getName());

            // 6. Изтриване на част
            dbService.deletePart("BP002");
            System.out.println("\n Deleted part with code: BP002");

        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
