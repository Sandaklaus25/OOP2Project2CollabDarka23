package com.example.storageinventory.repository;

import com.example.storageinventory.model.CashRegister;
import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.Sale;
import com.example.storageinventory.model.User;
import com.example.storageinventory.util.HibernateUtil;
import com.example.storageinventory.util.UserSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SaleRepository {

    public void save(Sale sale) throws Exception {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // 1. Зареждаме Продукта и Касата
            Product product = session.get(Product.class, sale.getProduct().getId());
            CashRegister cashRegister = session.get(CashRegister.class, 1L);

            if (product == null) throw new RuntimeException("Стоката не е намерена!");
            if (cashRegister == null) throw new RuntimeException("Касата липсва!");

            // 2. ПРОВЕРКА ЗА НАЛИЧНОСТ НА СТОКА
            if (product.getQuantity() < sale.getQuantity()) {
                throw new RuntimeException("НЯМА ДОСТАТЪЧНО НАЛИЧНОСТ! Искате: "
                        + sale.getQuantity() + ", Имаме: " + product.getQuantity());
            }

            // 3. ИЗЧИСЛЕНИЯ
            double totalIncome = product.getSalePrice() * sale.getQuantity();

            // 4. ОБНОВЯВАНЕ (Намаляваме стоката, увеличаваме парите)
            product.setQuantity(product.getQuantity() - sale.getQuantity());
            cashRegister.setBalance(cashRegister.getBalance() + totalIncome);

            // ВАЖНО: Тук взимаме текущия потребител!
            User currentUser = UserSession.getCurrentUser();
            sale.setOperator(currentUser); // <-- Ето тук става магията

            // 5. ЗАПИС
            session.merge(product);
            session.merge(cashRegister);
            session.persist(sale);

            tx.commit();
            System.out.println("✅ Продажбата е успешна! Приход: " + totalIncome);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception rb) {}
            }
            throw e; // Хвърляме грешката нагоре
        }
    }

    public List<Sale> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Sale s JOIN FETCH s.client JOIN FETCH s.product",
                    Sale.class).list();
        }
    }
}