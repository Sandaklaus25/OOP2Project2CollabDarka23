package com.example.storageinventory;

import com.example.storageinventory.model.*;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class AppInitializer {

    public static void main(String[] args) {
        System.out.println("СТАРТИРАНЕ НА ПЪЛНА ИНИЦИАЛИЗАЦИЯ...");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            try {
                // 1. РОЛИ
                UserRole adminRole = getOrCreateRole(session, "ADMIN");
                UserRole operatorRole = getOrCreateRole(session, "OPERATOR");

                // 2. ПОТРЕБИТЕЛИ
                if (!exists(session, "User", "username", "admin")) {
                    User admin = new User("admin", "admin123", "Main Administrator", "admin@sys.bg", adminRole);
                    session.persist(admin);
                    System.out.println("Admin user създаден.");
                }

                if (!exists(session, "User", "username", "operator")) {
                    User oper = new User("operator", "1234", "Skladov Agent", "agent@sys.bg", operatorRole);
                    session.persist(oper);
                    System.out.println("Operator user създаден.");
                }

                // 3. КАСА (Много важно!)
                CashRegister cashRegister = session.get(CashRegister.class, 1L);
                if (cashRegister == null) {
                    cashRegister = new CashRegister(50000.00); // Начален капитал
                    session.persist(cashRegister);
                    System.out.println("Касата е отворена с 50,000 лв.");
                }

                // 4. ДОСТАВЧИЦИ
                if (!exists(session, "Supplier", "companyName", "TechBG Ltd.")) {
                    session.persist(new Supplier("TechBG Ltd.", "BG123456789", "София", "0888111222"));
                    System.out.println("Доставчик TechBG добавен.");
                }

                // 5. КЛИЕНТИ
                if (!exists(session, "Client", "clientName", "Иван Иванов")) {
                    session.persist(new Client("Иван Иванов", "9090901234", "Пловдив", "0899333444"));
                    System.out.println("Клиент Иван Иванов добавен.");
                }

                // 6. СТОКИ (Примерни)
                if (!exists(session, "Product", "productName", "Лаптоп HP")) {

                    Product p = new Product("Лаптоп HP", 0, 1200.00, 1500.00, 10);

                    session.persist(p);
                    System.out.println("Стока Лаптоп HP добавена.");
                }

                tx.commit();
                System.out.println("ИНИЦИАЛИЗАЦИЯТА ЗАВЪРШИ УСПЕШНО!");

            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    // Помощни методи за по-чист код
    private static UserRole getOrCreateRole(Session session, String roleName) {
        UserRole role = session.createQuery("FROM UserRole WHERE roleName = :name", UserRole.class)
                .setParameter("name", roleName).uniqueResult();
        if (role == null) {
            role = new UserRole(roleName);
            session.persist(role);
        }
        return role;
    }

    private static boolean exists(Session session, String entityName, String field, String value) {
        Long count = session.createQuery("SELECT count(e) FROM " + entityName + " e WHERE e." + field + " = :val", Long.class)
                .setParameter("val", value)
                .uniqueResult();
        return count > 0;
    }
}