package com.example.storageinventory.service;

import com.example.storageinventory.model.User;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.example.storageinventory.model.UserRole; // Импортирай модела
import java.util.List;

public class UserService {

    public User authenticate(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // ВАЖНО: Ползваме "JOIN FETCH u.role", за да заредим ролята веднага!
            Query<User> query = session.createQuery(
                    "FROM User u JOIN FETCH u.role WHERE u.username = :user",
                    User.class
            );
            query.setParameter("user", username);

            User user = query.uniqueResult();

            // Проверка на парола
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Връща null, ако няма такъв user или паролата е грешна
    }

    public void saveUser(User user) throws Exception {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Проверка дали username е свободен
            Long count = session.createQuery("SELECT count(u) FROM User u WHERE u.username = :name", Long.class)
                    .setParameter("name", user.getUsername())
                    .uniqueResult();

            if (count > 0) {
                throw new Exception("Потребителското име вече е заето!");
            }

            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
    public List<UserRole> getAllRoles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserRole", UserRole.class).list();
        }
    }
}