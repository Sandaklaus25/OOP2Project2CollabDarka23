package com.example.storageinventory.util;

import com.example.storageinventory.model.User;

public class UserSession {

    private static UserSession instance;

    private static User currentUser;

    private UserSession(User user) {
        currentUser = user;
    }

    public static void startSession(User user) {
        instance = new UserSession(user);
        System.out.println("Сесията стартира за: " + user.getUsername() + " [" + user.getRole().getRoleName() + "]");
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void cleanSession() {
        currentUser = null;
        instance = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public boolean isAdmin() {
        if (currentUser != null && currentUser.getRole() != null) {
            String roleName = currentUser.getRole().getRoleName();
            return "ADMIN".equalsIgnoreCase(roleName);
        }
        return false;
    }
}