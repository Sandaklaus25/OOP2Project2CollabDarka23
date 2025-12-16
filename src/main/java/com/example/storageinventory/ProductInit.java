package com.example.storageinventory;

import com.example.storageinventory.model.Product;
import com.example.storageinventory.repository.ProductRepository;
import com.example.storageinventory.util.HibernateUtil;

public class ProductInit {
    public static void main(String[] args) {
        System.out.println("⏳ Проверка на таблица PRODUCT...");

        // Това изречение събужда Hibernate
        ProductRepository repo = new ProductRepository();

        // Пробваме да запишем тестова стока
        try {
            System.out.println("📦 Добавяне на тестови стоки...");

            // Име, Наличност, Доставна, Продажна, Критичен минимум
            // Забележка: При създаване на обект, ID е null, а quantity е каквото подадем
            Product p1 = new Product("Лаптоп Dell", 15, 1200.00, 1600.00, 5);
            Product p2 = new Product("Мишка Logitech", 50, 25.00, 45.00, 10);
            Product p3 = new Product("Монитор Samsung", 8, 300.00, 450.00, 3);

            repo.save(p1);
            repo.save(p2);
            repo.save(p3);

            System.out.println("✅ УСПЕХ! Таблицата е създадена и стоките са вътре.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}