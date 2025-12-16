package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import com.example.storageinventory.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Моля, попълнете всички полета!");
            errorLabel.setVisible(true);
            return;
        }

        // Викаме сървиса за проверка
        User user = userService.authenticate(username, password);

        if (user != null) {
            errorLabel.setText("Успешен вход! Здравей, " + user.getFullName());
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setVisible(true);

            // ТУК ПО-КЪСНО ЩЕ ОТВАРЯМЕ ГЛАВНОТО МЕНЮ
            System.out.println("User logged in: " + user.getUsername() + " Role: " + user.getRole().getRoleName());

        } else {
            errorLabel.setText("Грешно име или парола!");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
        }
    }
}