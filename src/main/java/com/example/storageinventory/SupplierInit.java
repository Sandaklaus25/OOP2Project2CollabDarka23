package com.example.storageinventory;

import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.repository.SupplierRepository;
import com.example.storageinventory.util.HibernateUtil;

public class SupplierInit {
    public static void main(String[] args) {
        System.out.println("⏳ Проверка на таблица SUPPLIER...");

        SupplierRepository repo = new SupplierRepository();

        try {
            System.out.println("🚛 Добавяне на тестови доставчици...");

            // Име, ЕИК, Адрес, Телефон
            Supplier s1 = new Supplier("TechBG Ltd.", "123456789", "София, бул. България 1", "0888111222");
            Supplier s2 = new Supplier("Office 1 Superstore", "987654321", "Пловдив, ул. Главна 5", "0899333444");
            Supplier s3 = new Supplier("IT Distribution", "555555555", "Варна, Бизнес Парк", "0877555666");

            repo.save(s1);
            repo.save(s2);
            repo.save(s3);

            System.out.println("✅ УСПЕХ! Доставчиците са записани.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}