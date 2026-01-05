package com.example.storageinventory.repository;

import com.example.storageinventory.model.CashRegister;
import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.User;
import com.example.storageinventory.util.HibernateUtil;
import com.example.storageinventory.util.UserSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DeliveryRepository {

    public void save(Delivery delivery) throws Exception {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Product product = delivery.getProduct();
            Product managedProduct = session.get(Product.class, product.getId());

            if (managedProduct == null) throw new RuntimeException("Стоката не е намерена!");

            double totalCost = managedProduct.getDeliveryPrice() * delivery.getQuantity();

            CashRegister cashRegister = session.get(CashRegister.class, 1L);
            if (cashRegister == null) {
                throw new RuntimeException("Грешка: Касата липсва! Пуснете CashRegisterInit.");
            }

            // Проверка за налични парични средства
            if (cashRegister.getBalance() < totalCost) {
                throw new RuntimeException("НЕДОСТАТЪЧНО СРЕДСТВА! Налични: " + String.format("%.2f", cashRegister.getBalance()) + " лв., Нужни: " + String.format("%.2f", totalCost) + " лв.");
            }

            cashRegister.setBalance(cashRegister.getBalance() - totalCost);
            session.merge(cashRegister);

            managedProduct.setQuantity(managedProduct.getQuantity() + delivery.getQuantity());
            session.merge(managedProduct);

            User currentUser = UserSession.getCurrentUser();
            delivery.setOperator(currentUser);

            session.persist(delivery);

            tx.commit();
            System.out.println("Доставката е успешна!");

        } catch (Exception e) {
            // Ако стане грешка, опитваме Rollback, но го пазим да не гръмне той
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception rbEx) {
                    System.err.println("Rollback се провали: " + rbEx.getMessage());
                }
            }
            // Хвърляме оригиналната грешка (тази за парите), а не грешката от rollback-а
            throw e;
        }
    }

    public List<Delivery> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Delivery d JOIN FETCH d.supplier JOIN FETCH d.product", Delivery.class).list();
        }
    }
}