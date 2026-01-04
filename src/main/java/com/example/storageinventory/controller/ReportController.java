package com.example.storageinventory.controller;

import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.Sale;
import com.example.storageinventory.service.DeliveryService;
import com.example.storageinventory.service.ProductService;
import com.example.storageinventory.service.SaleService;
import com.example.storageinventory.util.ReportType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportController {

    // Връзка с елементите от FXML
    @FXML private Label reportTitleLabel;
    @FXML private HBox filterBox; // Кутията с датите (ще я крием за някои справки)
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    // Използваме ? (Wildcard), защото таблицата ще показва различни обекти
    // (веднъж Sales, веднъж Products, веднъж Deliveries)
    @FXML private TableView<?> reportTable;

    @FXML private Label totalLabel1; // За "Оборот" или "Брой"
    @FXML private Label totalLabel2; // За "Печалба"

    private final ProductService productService = new ProductService();
    private final SaleService saleService = new SaleService();
    private final DeliveryService deliveryService = new DeliveryService();

    private ReportType currentType;

    // Този метод се вика от MainMenuController веднага след зареждане
    public void initReport(ReportType type) {
        this.currentType = type;

        // 1. Слагаме заглавието според избраното от менюто
        reportTitleLabel.setText(type.getTitle());

        // 2. Настройка на интерфейса според типа
        setupInterface();
    }

    private void setupInterface() {
        // Тук ще крием/показваме датите
        // Например: За Наличност (INVENTORY) не ни трябват дати
        if (currentType == ReportType.INVENTORY) {
            filterBox.setVisible(false);
            filterBox.setManaged(false); // Да не заема място

            // Ако е наличност, може направо да заредим данните, без бутон
            onGenerate();
        } else {
            // За всички останали (Продажби, Доставки, Финанси) искаме дати
            filterBox.setVisible(true);
            filterBox.setManaged(true);

            // Слагаме дати по подразбиране (Първи и Последен ден от месеца)
            startDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
            endDatePicker.setValue(LocalDate.now());
        }
    }

    @FXML
    public void onGenerate() {
        System.out.println("Генериране на справка: " + currentType);

        reportTable.getColumns().clear();
        reportTable.getItems().clear();

        totalLabel1.setText("");
        totalLabel2.setVisible(false);

        switch (currentType) {
            case INVENTORY:
                loadInventoryReport();
                break;
            case FINANCIAL:
                loadFinancialReport();
                break;
            case SALES:
                loadSalesReport();
                break;
            case DELIVERIES:
                loadDeliveriesReport();
                break;
            case CASH_FLOW:
                loadCashReport();
                break;
            case OPERATORS:
                loadOperatorsReport();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void loadInventoryReport() {
        // 1. Създаваме колоните
        TableColumn<Product, String> colName = new TableColumn<>("Стока");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colName.setPrefWidth(200); // Можеш да зададеш ширина, ако искаш

        TableColumn<Product, Integer> colQty = new TableColumn<>("Наличност");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Double> colPriceIn = new TableColumn<>("Дост. цена");
        colPriceIn.setCellValueFactory(new PropertyValueFactory<>("deliveryPrice"));

        // НОВО: Колона за Продажна цена
        TableColumn<Product, Double> colPriceOut = new TableColumn<>("Прод. цена");
        colPriceOut.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        // Оцветяваме я леко в зелено, за да се отличава (опционално)
        colPriceOut.setStyle("-fx-text-fill: #28a745;");

        TableColumn<Product, Double> colTotal = new TableColumn<>("Стойност (лв)");
        colTotal.setCellValueFactory(cell -> {
            Product p = cell.getValue();
            // Смятаме стойността по доставна цена (колко пари сме инвестирали)
            double total = p.getQuantity() * p.getDeliveryPrice();
            return new javafx.beans.property.SimpleObjectProperty<>(total);
        });

        // 2. Добавяме ВСИЧКИ колони (включително новата)
        ((TableView<Product>) reportTable).getColumns().setAll(colName, colQty, colPriceIn, colPriceOut, colTotal);

        // 3. Взимаме данните
        List<Product> products = productService.getAllProducts();

        // 4. Слагаме ги в таблицата
        ((TableView<Product>) reportTable).setItems(FXCollections.observableArrayList(products));

        // 5. Обобщение
        calculateInventoryTotals(products);
    }

    private void calculateInventoryTotals(List<Product> products) {
        double totalValue = 0.0;
        int totalItems = 0;

        for (Product p : products) {
            totalItems += p.getQuantity();
            totalValue += (p.getQuantity() * p.getDeliveryPrice());
        }

        totalLabel1.setText(String.format("Общо стоки: %d бр.", totalItems));

        // Ползваме втория етикет за парите
        totalLabel2.setVisible(true);
        totalLabel2.setText(String.format("Стойност на склада: %.2f лв.", totalValue));
        // Оцветяваме го в синьо (информативно), а не зелено (печалба)
        totalLabel2.setStyle("-fx-text-fill: #007bff;");
    }

    @SuppressWarnings("unchecked")
    private void loadFinancialReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (start == null || end == null) {
            // Може да сложиш alert тук
            return;
        }

        // 1. Колона ДАТА
        TableColumn<Sale, String> colDate = new TableColumn<>("Дата");
        colDate.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getSaleDate().toString()));

        // 2. Колона СТОКА
        TableColumn<Sale, String> colProduct = new TableColumn<>("Стока");
        colProduct.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getProduct().getProductName()));

        // 3. Колона КОЛИЧЕСТВО
        TableColumn<Sale, Integer> colQty = new TableColumn<>("К-во");
        colQty.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getQuantity()));

        // 4. Колона ОБОРОТ (Тук беше грешката)
        TableColumn<Sale, Double> colTurnover = new TableColumn<>("Оборот (лв)");
        colTurnover.setCellValueFactory(cell -> {
            Sale s = cell.getValue();
            // ФОРМУЛА: Бройки * Продажна цена от продукта
            double total = s.getQuantity() * s.getProduct().getSalePrice();
            return new SimpleObjectProperty<>(total);
        });

        // 5. Колона ПЕЧАЛБА
        TableColumn<Sale, Double> colProfit = new TableColumn<>("Печалба (лв)");
        colProfit.setCellValueFactory(cell -> {
            Sale s = cell.getValue();
            Product p = s.getProduct();

            // Приход (Оборот)
            double revenue = s.getQuantity() * p.getSalePrice();
            // Разход (Доставна цена * бройки)
            double cost = s.getQuantity() * p.getDeliveryPrice();

            return new SimpleObjectProperty<>(revenue - cost);
        });
        colProfit.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");

        // Слагаме колоните
        ((TableView<Sale>) reportTable).getColumns().setAll(colDate, colProduct, colQty, colTurnover, colProfit);

        // Взимаме данните
        List<Sale> sales = saleService.getSalesByPeriod(start, end);
        ((TableView<Sale>) reportTable).setItems(FXCollections.observableArrayList(sales));

        // Обновяваме сумите долу
        calculateFinancialTotals(sales);
    }

    private void calculateFinancialTotals(List<Sale> sales) {
        double totalTurnover = 0.0;
        double totalProfit = 0.0;

        for (Sale s : sales) {
            Product p = s.getProduct();

            // Ръчно смятане
            double currentTurnover = s.getQuantity() * p.getSalePrice();
            double currentCost = s.getQuantity() * p.getDeliveryPrice();

            totalTurnover += currentTurnover;
            totalProfit += (currentTurnover - currentCost);
        }

        totalLabel1.setText(String.format("Оборот: %.2f лв.", totalTurnover));
        totalLabel2.setVisible(true);
        totalLabel2.setText(String.format("Чиста печалба: %.2f лв.", totalProfit));
        if (totalProfit<0){
            totalLabel2.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
        }
        else {
            totalLabel2.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCashReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (start == null || end == null) return;

        // 1. Колони
        TableColumn<CashEntry, String> colDate = new TableColumn<>("Дата");
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));

        TableColumn<CashEntry, String> colType = new TableColumn<>("Тип");
        colType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType()));

        TableColumn<CashEntry, String> colInfo = new TableColumn<>("Описание");
        colInfo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getInfo()));
        colInfo.setPrefWidth(250);

        TableColumn<CashEntry, Double> colAmount = new TableColumn<>("Сума (лв)");

        colAmount.setCellValueFactory(cell -> {
            Double val = cell.getValue().getAmount();
            // Ако е РАЗХОД, го правим отрицателно число, за да е логично сортирането
            // (Приходите са положителни, Разходите са отрицателни)
            if ("РАЗХОД".equals(cell.getValue().getType())) {
                val = -val;
            }
            return new SimpleObjectProperty<>(val);
        });
        colAmount.setStyle("-fx-font-weight: bold;");

        ((TableView<CashEntry>) reportTable).getColumns().setAll(colDate, colType, colInfo, colAmount);

        // 3. Събиране на данните (СЛИВАНЕ)
        java.util.List<CashEntry> allTransactions = new java.util.ArrayList<>();

        // А) Добавяме Продажбите (Приход)
        List<Sale> sales = saleService.getSalesByPeriod(start, end);
        for (Sale s : sales) {
            double total = s.getQuantity() * s.getProduct().getSalePrice();
            allTransactions.add(new CashEntry(
                    s.getSaleDate().toString(),
                    "ПРИХОД",
                    "Продажба: " + s.getProduct().getProductName(),
                    total,
                    null
            ));
        }

        // Б) Добавяме Доставките (Разход)
        // Трябва да имаш deliveryService горе в класа
        List<Delivery> deliveries = deliveryService.getDeliveriesByPeriod(start, end);
        for (Delivery d : deliveries) {
            double total = d.getQuantity() * d.getProduct().getDeliveryPrice();
            allTransactions.add(new CashEntry(
                    d.getDeliveryDate().toString(),
                    "РАЗХОД",
                    "Доставка: " + d.getProduct().getProductName(),
                    total,
                    null
            ));
        }

        // 4. Сортиране по дата (най-новите отгоре или обратно)
        // Тук ги сортираме хронологично (стари -> нови)
        allTransactions.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        ((TableView<CashEntry>) reportTable).setItems(FXCollections.observableArrayList(allTransactions));

        // 5. Тотал (Баланс за периода)
        double balance = 0;
        for (CashEntry entry : allTransactions) {
            if ("ПРИХОД".equals(entry.getType())) {
                balance += entry.getAmount();
            } else {
                balance -= entry.getAmount();
            }
        }

        totalLabel1.setText(String.format("Баланс за периода: %.2f лв.", balance));
        totalLabel2.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void loadDeliveriesReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start == null || end == null) return;

        // Колони
        TableColumn<Delivery, String> colDate = new TableColumn<>("Дата");
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDeliveryDate().toString()));

        TableColumn<Delivery, String> colProduct = new TableColumn<>("Стока");
        colProduct.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getProductName()));

        TableColumn<Delivery, String> colSupplier = new TableColumn<>("Доставчик");
        colSupplier.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplier().getSupplierName()));

        TableColumn<Delivery, Integer> colQty = new TableColumn<>("Количество");
        colQty.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantity()));

        TableColumn<Delivery, Double> colPrice = new TableColumn<>("Дост. Цена");
        colPrice.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getProduct().getDeliveryPrice()));

        TableColumn<Delivery, Double> colTotal = new TableColumn<>("Общо");
        colTotal.setCellValueFactory(cell -> {
            double total = cell.getValue().getQuantity() * cell.getValue().getProduct().getDeliveryPrice();
            return new SimpleObjectProperty<>(total);
        });

        ((TableView<Delivery>) reportTable).getColumns().setAll(colDate, colProduct,colSupplier, colQty, colPrice, colTotal);

        // Данни
        List<Delivery> list = deliveryService.getDeliveriesByPeriod(start, end);
        ((TableView<Delivery>) reportTable).setItems(FXCollections.observableArrayList(list));

        // Тотал
        double sum = list.stream().mapToDouble(d -> d.getQuantity() * d.getProduct().getDeliveryPrice()).sum();
        totalLabel1.setText(String.format("Общо разходи: %.2f лв.", sum));
        totalLabel2.setVisible(false);
    }

    // 2. ПРОДАЖБИ (ИЗПИСВАНЕ) И КЛИЕНТИ
    @SuppressWarnings("unchecked")
    private void loadSalesReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start == null || end == null) return;

        TableColumn<Sale, String> colDate = new TableColumn<>("Дата");
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSaleDate().toString()));

        TableColumn<Sale, String> colClient = new TableColumn<>("Клиент");
        colClient.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getClient().getClientName()));

        TableColumn<Sale, String> colProduct = new TableColumn<>("Стока");
        colProduct.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getProductName()));

        TableColumn<Sale, Integer> colQty = new TableColumn<>("К-во");
        colQty.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantity()));

        TableColumn<Sale, Double> colPrice = new TableColumn<>("Прод. Цена");
        colPrice.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getProduct().getSalePrice()));

        TableColumn<Sale, Double> colTotal = new TableColumn<>("Оборот");
        colTotal.setCellValueFactory(cell -> {
            double total = cell.getValue().getQuantity() * cell.getValue().getProduct().getSalePrice();
            return new SimpleObjectProperty<>(total);
        });

        ((TableView<Sale>) reportTable).getColumns().setAll(colDate,colClient, colProduct, colQty, colPrice, colTotal);

        List<Sale> list = saleService.getSalesByPeriod(start, end);
        ((TableView<Sale>) reportTable).setItems(FXCollections.observableArrayList(list));

        double sum = list.stream().mapToDouble(s -> s.getQuantity() * s.getProduct().getSalePrice()).sum();
        totalLabel1.setText(String.format("Общ оборот: %.2f лв.", sum));
        totalLabel2.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void loadOperatorsReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start == null || end == null) return;

        // Колони
        TableColumn<CashEntry, String> colDate = new TableColumn<>("Дата");
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));

        TableColumn<CashEntry, String> colOp = new TableColumn<>("Оператор");
        colOp.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOperator()));

        TableColumn<CashEntry, String> colType = new TableColumn<>("Действие");
        colType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType()));

        TableColumn<CashEntry, String> colInfo = new TableColumn<>("Детайли");
        colInfo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getInfo()));
        colInfo.setPrefWidth(300);

        ((TableView<CashEntry>) reportTable).getColumns().setAll(colDate, colOp, colType, colInfo);

        List<CashEntry> activities = new ArrayList<>();

        // 1. Продажби
        List<Sale> sales = saleService.getSalesByPeriod(start, end);
        for (Sale s : sales) {
            String operatorName = (s.getOperator() != null)
                    ? s.getOperator().getUsername()
                    : "Неизвестен";

            activities.add(new CashEntry(
                    s.getSaleDate().toString(),
                    "ПРОДАЖБА",
                    s.getProduct().getProductName() + " (" + s.getQuantity() + " бр.)",
                    0.0,
                    operatorName
            ));
        }

        // 2. Доставки
        List<Delivery> deliveries = deliveryService.getDeliveriesByPeriod(start, end);
        for (Delivery d : deliveries) {
            String operatorName = (d.getOperator() != null)
                    ? d.getOperator().getUsername()
                    : "Неизвестен";

            activities.add(new CashEntry(
                    d.getDeliveryDate().toString(),
                    "ДОСТАВКА",
                    d.getProduct().getProductName() + " (" + d.getQuantity() + " бр.)",
                    0.0,
                    operatorName
            ));

        }

        // Сортиране и показване
        activities.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        ((TableView<CashEntry>) reportTable).setItems(FXCollections.observableArrayList(activities));

        totalLabel1.setText("Общо операции: " + activities.size());
        totalLabel2.setVisible(false);
    }

    public static class CashEntry {
        private String date;
        private String type;
        private String info;
        private Double amount;
        private String operator; // НОВО ПОЛЕ

        public CashEntry(String date, String type, String info, Double amount, String operator) {
            this.date = date;
            this.type = type;
            this.info = info;
            this.amount = amount;
            this.operator = operator;
        }

        // Getters...
        public String getDate() { return date; }
        public String getType() { return type; }
        public String getInfo() { return info; }
        public Double getAmount() { return amount; }
        public String getOperator() { return operator; } // НОВ GETTER
    }
}