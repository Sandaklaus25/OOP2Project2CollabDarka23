package com.example.storageinventory.service;

import com.example.storageinventory.model.User;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserService {

    public User authenticate(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Търсим потребител по username
            String hql = "FROM User u WHERE u.username = :username";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);

            User user = query.uniqueResult();

            // Проверка:
            // 1. Дали намерихме такъв потребител?
            // 2. Дали паролата съвпада? (Засега е прав текст, по-късно ще е криптирана)
            if (user != null && user.getPassword().equals(password)) {
                return user; // Успешен вход
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Грешен потребител или парола
    }
}