package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import com.example.storageinventory.model.UserRole;
import com.example.storageinventory.service.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RegisterUserController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<UserRole> roleCombo;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    private static final Logger logger = Logger.getLogger(RegisterUserController.class.getName());

    @FXML
    public void initialize() {
        // 1. Зареждаме ролите в падащото меню
        try {
            roleCombo.setItems(FXCollections.observableArrayList(userService.getAllRoles()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при зареждане на ролите", e);
        }
    }

    @FXML
    public void onSave() {
        try {
            // Валидация
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || roleCombo.getValue() == null) {
                showError("Полетата User, Password и Роля са задължителни!");
                return;
            }

            User newUser = new User(
                    usernameField.getText(),
                    passwordField.getText(),
                    fullNameField.getText(),
                    emailField.getText(),
                    roleCombo.getValue()
            );

            // Запис в базата
            userService.saveUser(newUser);

            closeDialog();
            System.out.println("Успешно създаден потребител: " + newUser.getUsername());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при запазване на потребител", e);
            showError(e.getMessage() != null ? e.getMessage() : "Грешка при запазване на потребител");
        }
    }

    @FXML
    public void onCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}