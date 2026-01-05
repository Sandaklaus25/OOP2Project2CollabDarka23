package com.example.storageinventory.util;

public enum ReportType {
    DELIVERIES("Доставки и Доставчици"), SALES("Продажби и Клиенти"), INVENTORY("Наличности в склада"), FINANCIAL("Приходи, Разходи и Печалба"), CASH_FLOW("Движение на касата"), OPERATORS("Дейност на оператори");

    private final String title;

    ReportType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}