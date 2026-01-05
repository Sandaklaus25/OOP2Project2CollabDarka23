package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import com.example.storageinventory.service.UserService;
import com.example.storageinventory.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

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

        User user = userService.authenticate(username, password);

        if (user != null) {
            try {

                UserSession.startSession(user);
                UserSession.setCurrentUser(user);

                // Скриваме Login прозореца
                Stage loginStage = (Stage) errorLabel.getScene().getWindow();
                loginStage.close();

                // Зареждаме главното меню
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/main-menu-view.fxml"));
                Scene scene = new Scene(loader.load());
                Stage mainStage = new Stage();
                mainStage.setTitle("Складова система - Главно Меню");
                mainStage.setScene(scene);
                mainStage.setMaximized(true);
                mainStage.show();

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Грешка при зареждане на главното меню!", e);
                errorLabel.setText("Критична грешка при зареждане на менюто!");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Грешно потребителско име или парола!");
            errorLabel.setVisible(true);
        }
    }
}