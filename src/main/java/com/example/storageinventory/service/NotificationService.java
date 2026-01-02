package com.example.storageinventory.service;

import com.example.storageinventory.model.Product;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final ProductService productService = new ProductService();
    private final CashRegisterService cashService = new CashRegisterService();

    // Праг за парите (можеш да го промениш)
    private static final double CASH_THRESHOLD = 500.00;

    public List<String> getAlerts() {
        List<String> alerts = new ArrayList<>();

        // 1. Проверка на КАСАТА
        double currentBalance = cashService.getCurrentBalance();
        if (currentBalance <= 0) {
            alerts.add("КАСА: Липса на парична наличност! (0.00 лв.)");
        } else if (currentBalance < CASH_THRESHOLD) {
            alerts.add(String.format("КАСА: Критичен минимум! (%.2f лв.)", currentBalance));
        }

        // 2. Проверка на СТОКИТЕ
        List<Product> products = productService.getAllProducts();

        for (Product p : products) {
            int qty = p.getQuantity();
            // Ако критичният минимум не е зададен, приемаме че е 5 (или 0)
            int min = (p.getCriticalMin() != null) ? p.getCriticalMin() : 0;

            if (qty == 0) {
                alerts.add("СТОКА: \"" + p.getProductName() + "\" е изчерпана!");
            } else if (qty <= min) {
                alerts.add("СТОКА: \"" + p.getProductName() + "\" е под минимума (" + qty + " бр.)");
            }
        }

        if (alerts.isEmpty()) {
            alerts.add("Няма критични известия. Системата работи нормално.");
        }

        return alerts;
    }
}