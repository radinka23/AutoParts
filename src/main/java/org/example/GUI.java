package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {
    private JButton button1, button2, button3, button4, button5, button6;
    private final DBService dbService;
    private final JList<Part> partList;
    private final DefaultListModel<Part> listModel;

    public GUI() {
        dbService = new DBService();
        listModel = new DefaultListModel<>();
        partList = new JList<>(listModel);
        partList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setupFrame();
        setupButtons();
        setupPanels();
    }

    // Основна настройка на прозореца
    private void setupFrame() {
        setTitle("Auto Parts Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    // Създаване и добавяне на бутони с обработка на събития
    private void setupButtons() {
        button1 = new JButton("Search by Name");
        button2 = new JButton("Add Part");
        button3 = new JButton("Remove Part");
        button4 = new JButton("Edit Part");
        button5 = new JButton("List All Parts");

        // Добавяне на слушател за всяко бутонче
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
    }

    // Организиране на панелите и визуализацията
    private void setupPanels() {
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Добавяне на бутоните в панела
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);

        // скролване на списъка с части ако са много ел
        JScrollPane scrollPane = new JScrollPane(partList);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel, scrollPane);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);

        add(splitPane); // Добавяне към прозореца
        setVisible(true); // Показване на интерфейса
    }

    // Обработка на натискане на бутони
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == button1) {
                handleSearchByName();
            } else if (source == button2) {
                handleAddPart(); // Добавяне на част
                listAllParts(); // Обновяване на списъка
            } else if (source == button3) {
                handleRemovePart();
                listAllParts();
            } else if (source == button4) {
                handleEditPart(); // Редакция на част
                listAllParts();
            } else if (source == button5) {
                listAllParts();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Грешка: " + ex.getMessage());
        }
    }

    // Извличане на всички части от базата
    private void listAllParts() throws Exception {
        listModel.clear();
        listModel.addAll(dbService.getAll());
    }

    // Обработка на търсене по име
    private void handleSearchByName() {
        String keyword = JOptionPane.showInputDialog(this, "Въведете име на част:");
        listModel.clear();

        //За всяка намерена част създава нов обект Part и го добавя в списъка.
        if (keyword != null && !keyword.trim().isEmpty()) {
            for (Part result : dbService.searchPartsByName(keyword)) {
                listModel.addElement(new Part(
                        result.getName(),
                        result.getCode(),
                        PartCategory.valueOf(result.getCategory()),
                        result.getSupportedCars(),
                        result.getBuyPrice(),
                        result.getSellPrice(),
                        result.getManufacturer()));
            }
        } else {
            System.out.println("Не е въведен текст.");
        }
    }

    // Добавяне на нова част
    private void handleAddPart() {
        String name = JOptionPane.showInputDialog(this, "Име на част:");
        String code = JOptionPane.showInputDialog(this, "Код на частта:");
        String categoryStr = JOptionPane.showInputDialog(this, "Категория (напр. ENGINE, BRAKE):");
        String buyPriceStr = JOptionPane.showInputDialog(this, "Закупна цена:");
        String sellPriceStr = JOptionPane.showInputDialog(this, "Продажна цена:");
        String manufacturerName = JOptionPane.showInputDialog(this, "Производител:");


        if (name != null && code != null && categoryStr != null && buyPriceStr != null && sellPriceStr != null && manufacturerName != null) {
            PartCategory category = PartCategory.valueOf(categoryStr.toUpperCase());
            double buyPrice = Double.parseDouble(buyPriceStr);
            double sellPrice = Double.parseDouble(sellPriceStr);
            Manufacturer manufacturer = new Manufacturer(manufacturerName, null, null, null, null);

            Part part = new Part(name, code, category, new ArrayList<>(), buyPrice, sellPrice, manufacturer);
            dbService.addPart(part);
        }
    }

    // Премахване на част
    private void handleRemovePart() {
        String code = JOptionPane.showInputDialog(this, "Въведете кода на частта за изтриване:");
        if (code != null && !code.trim().isEmpty()) {
            dbService.deletePart(code);
        }
    }

    // Редактиране на съществуваща част
    private void handleEditPart() {
        String code = JOptionPane.showInputDialog(this, "Код на частта за редакция:");
        if (code == null || code.trim().isEmpty()) return;

        String newName = JOptionPane.showInputDialog(this, "Ново име:");
        String newCategoryStr = JOptionPane.showInputDialog(this, "Нова категория:");
        String newBuyPriceStr = JOptionPane.showInputDialog(this, "Нова закупна цена:");
        String newSellPriceStr = JOptionPane.showInputDialog(this, "Нова продажна цена:");
        String newManufacturer = JOptionPane.showInputDialog(this, "Нов производител:");

        if (newName != null && newCategoryStr != null && newBuyPriceStr != null && newSellPriceStr != null && newManufacturer != null) {
            PartCategory category = PartCategory.valueOf(newCategoryStr.toUpperCase());
            double buyPrice = Double.parseDouble(newBuyPriceStr);
            double sellPrice = Double.parseDouble(newSellPriceStr);
            Manufacturer manufacturer = new Manufacturer(newManufacturer, null, null, null, null);

            Part part = new Part(newName, code, category, new ArrayList<>(), buyPrice, sellPrice, manufacturer);
            dbService.updatePart(part);
        }
    }


    // Стартиране на приложението
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
