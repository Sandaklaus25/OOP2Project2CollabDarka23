package com.example.storageinventory.util;

import com.example.storageinventory.model.User;

public class UserSession {

    // 1. Статична променлива - тук ще пазим "кой е влязъл"
    private static UserSession instance;

    // 2. Полето за самия потребител
    private User currentUser;

    // 3. Частен конструктор (за да не може някой да прави new UserSession() безразборно)
    private UserSession(User user) {
        this.currentUser = user;
    }

    // 4. Метод за стартиране на сесията (вика се при Успешен Вход)
    public static void startSession(User user) {
        instance = new UserSession(user);
        System.out.println("🔑 Сесията стартира за: " + user.getUsername() + " [" + user.getRole().getRoleName() + "]");
    }

    // 5. Метод за достъп до сесията отвсякъде
    public static UserSession getInstance() {
        return instance;
    }

    // 6. Метод за изчистване (при Изход/Logout)
    public static void cleanSession() {
        instance = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // 7. Проверка дали е Админ (бъркаме през User -> Role -> RoleName)
    public boolean isAdmin() {
        if (currentUser != null && currentUser.getRole() != null) {
            String roleName = currentUser.getRole().getRoleName();
            return "ADMIN".equalsIgnoreCase(roleName);
        }
        return false;
    }
}