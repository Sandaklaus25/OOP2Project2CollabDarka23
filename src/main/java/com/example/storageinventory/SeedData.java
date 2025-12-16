package com.example.storageinventory;

import com.example.storageinventory.model.User;
import com.example.storageinventory.model.UserRole;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SeedData {
    public static void main(String[] args) {
        System.out.println("⏳ Стартиране на инициализация...");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // 1. Създаване на Ролите
            UserRole adminRole = new UserRole("ADMIN");
            UserRole operatorRole = new UserRole("OPERATOR");

            session.persist(adminRole);
            session.persist(operatorRole);
            System.out.println("Ролите са записани.");

            // 2. Създаване на Администратор
            // Паролата тук е в прав текст, по-нататък може да я криптираме
            User adminUser = new User("admin", "admin123", "Main Administrator", "admin@warehouse.bg", adminRole);

            session.persist(adminUser);
            System.out.println("Администраторът е създаден.");

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }
}