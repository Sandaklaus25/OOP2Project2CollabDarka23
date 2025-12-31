package com.example.storageinventory;

import com.example.storageinventory.model.CashRegister;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CashRegisterInit {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // Проверяваме дали вече има каса
            CashRegister existing = session.get(CashRegister.class, 1L);
            if (existing == null) {
                // Ако няма, създаваме с 50 000 лв.
                CashRegister cr = new CashRegister(50000.00);
                session.persist(cr);
                System.out.println("💰 Касата е създадена с 50,000.00 лв.");
            } else {
                System.out.println("ℹ️ Касата вече съществува: " + existing.getBalance() + " лв.");
            }

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