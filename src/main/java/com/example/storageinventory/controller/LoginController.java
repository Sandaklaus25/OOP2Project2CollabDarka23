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
            try {
                // 1. Скриваме (затваряме) Login прозореца
                // Взимаме прозореца чрез някой елемент от сцената (напр. бутона или полето)
                javafx.stage.Stage loginStage = (javafx.stage.Stage) errorLabel.getScene().getWindow();
                loginStage.close();

                // 2. Зареждаме новия FXML файл (Главното меню)
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/main-menu-view.fxml"));
                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

                // 3. Взимаме контролера на новия прозорец и му подаваме юзъра
                MainMenuController controller = loader.getController();
                controller.setLoggedInUser(user);

                // 4. Създаваме и показваме новия прозорец
                javafx.stage.Stage mainStage = new javafx.stage.Stage();
                mainStage.setTitle("Складова система - Главно Меню");
                mainStage.setScene(scene);
                mainStage.setMaximized(true); // Да се отвори на цял екран
                mainStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Критична грешка при зареждане на менюто!");
                errorLabel.setVisible(true);
            }
        }
    }
}