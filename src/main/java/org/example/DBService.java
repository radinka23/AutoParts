package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private final String jdbcURL = String.format("jdbc:mysql://%s:%s/%s",
            System.getenv().getOrDefault("DB_HOST", "localhost"),
            System.getenv().getOrDefault("DB_PORT", "3306"),
            System.getenv().getOrDefault("DB_NAME", "Autoparts"));

    private final String dbUser = System.getenv().getOrDefault("DB_USER", "root");
    private final String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "radibobi69");

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
    }

    // Добавяне на нова част в базата данни
    public void addPart(Part part) {
        String sql = "INSERT INTO Part (name, code, category, buy_price, sell_price, manufacturer) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Задава стойности за параметрите и изпълнява заявката.
            stmt.setString(1, part.getName());
            stmt.setString(2, part.getCode());
            stmt.setString(3, part.getCategory());
            stmt.setDouble(4, part.getBuyPrice());
            stmt.setDouble(5, part.getSellPrice());
            stmt.setString(6, part.getManufacturer().getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Актуализиране на съществуваща част по нейната кодова стойност
    public void updatePart(Part part) {
        String sql = "UPDATE Part SET name = ?, category = ?, buy_price = ?, sell_price = ?, manufacturer = ? WHERE code = ?";
        //Този ред използва конструкцията try-with-resources, която автоматично затваря ресурсите след използването им
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, part.getName());
            stmt.setString(2, part.getCategory());
            stmt.setDouble(3, part.getBuyPrice());
            stmt.setDouble(4, part.getSellPrice());
            stmt.setString(5, part.getManufacturer().getName());
            stmt.setString(6, part.getCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Изтриване на част от базата по нейната кодова стойност
    public void deletePart(String code) {
        String sql = "DELETE FROM Part WHERE code = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Търсене на части по име
    public List<Part> searchPartsByName(String keyword) {
        //Създава списък с резултати
        List<Part> results = new ArrayList<>();
        String sql = "SELECT * FROM Part WHERE name LIKE ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            //%keyword% означава: "всякакъв текст преди и след търсената дума" маслен филтър изпълнение
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            // Прочитане на резултатите от заявката
            while (rs.next()) {
                int partId = rs.getInt("id");
                String name = rs.getString("name");
                String code = rs.getString("code");
                String categoryStr = rs.getString("category");
                double buyPrice = rs.getDouble("buy_price");
                double sellPrice = rs.getDouble("sell_price");
                String manufacturerStr = rs.getString("manufacturer");

                // Преобразуване на данни
                PartCategory category = PartCategory.valueOf(categoryStr);
                Manufacturer manufacturer = new Manufacturer(manufacturerStr, null, null, null, null);
                ArrayList<Car> supportedCars = getSupportedCarsByPartId(partId, conn);

                // Добавяне на частта в резултатите
                Part part = new Part(name, code, category, supportedCars, buyPrice, sellPrice, manufacturer);
                results.add(part);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Извличане на всички части от базата данни
    public List<Part> getAll() throws Exception {
        String query = "SELECT * FROM Part";
        List<Part> parts = new ArrayList<>();

        // създава връзка с базата  подготвя SQL заявката. изпълнява заявката и връща резултатите.
        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Прочитане на резултатите от заявката
            while (rs.next()) {
                String name = rs.getString("name");
                String code = rs.getString("code");
                String categoryStr = rs.getString("category");
                double buyPrice = rs.getDouble("buy_price");
                double sellPrice = rs.getDouble("sell_price");
                String manufacturerName = rs.getString("manufacturer");

                //Обработка и създаване на обекти
                PartCategory category = PartCategory.valueOf(categoryStr);
                Manufacturer manufacturer = new Manufacturer(manufacturerName, null, null, null, null);
                ArrayList<Car> supportedCars = new ArrayList<>();

                // Създаване на обект и добавяне към списъка
                Part part = new Part(name, code, category, supportedCars, buyPrice, sellPrice, manufacturer);
                parts.add(part);
            }

        } catch (SQLException e) {
            throw new Exception("Грешка при извличане на части: " + e.getMessage(), e);
        }

        return parts;
    }

    // Връща колите, поддържани от дадена част по ID
    private ArrayList<Car> getSupportedCarsByPartId(int partId, Connection conn) throws SQLException {
        ArrayList<Car> cars = new ArrayList<>();
        String carSql = "SELECT car_model FROM Part_SupportedCar WHERE part_id = ?";

        //Изпълнение на заявката
        try (PreparedStatement stmt = conn.prepareStatement(carSql)) {
            stmt.setInt(1, partId);
            //Задаване на параметър
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String model = rs.getString("car_model");
                cars.add(new Car(model, null, null));
            }
        }
        return cars;
    }
}
