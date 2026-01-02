package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import com.example.storageinventory.service.UserService;
import com.example.storageinventory.util.UserSession; // Импортираме сесията
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

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

        // 1. Питаме сървиса: "Има ли такъв човек?"
        User user = userService.authenticate(username, password);

        if (user != null) {
            try {
                // ✅ ВАЖНО: Тук стартираме сесията!
                // Вече цялото приложение ще знае кой е влязъл.
                UserSession.startSession(user);

                // 2. Скриваме Login прозореца
                Stage loginStage = (Stage) errorLabel.getScene().getWindow();
                loginStage.close();

                // 3. Зареждаме Главното меню
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/main-menu-view.fxml"));
                Scene scene = new Scene(loader.load());

                // (Вече НЕ ни трябва: controller.setLoggedInUser(user), защото имаме UserSession)

                // 4. Отваряме новия прозорец
                Stage mainStage = new Stage();
                mainStage.setTitle("Складова система - Главно Меню");
                mainStage.setScene(scene);
                mainStage.setMaximized(true);
                mainStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Критична грешка при зареждане на менюто!");
                errorLabel.setVisible(true);
            }
        } else {
            // Ако user е null, значи грешна парола
            errorLabel.setText("Грешно потребителско име или парола!");
            errorLabel.setVisible(true);
        }
    }
}