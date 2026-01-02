package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import com.example.storageinventory.service.CashRegisterService;
import com.example.storageinventory.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.paint.Color;

import java.io.IOException;

public class MainMenuController {

    // Връзка с главната рамка от FXML файла.
    // Това ни позволява да сменяме съдържанието в средата (Center).

    @FXML
    private Label balanceLabel;

    // Тук ще сложим и менютата, които искаме да крием (подготовка за следващата стъпка)
    @FXML private Menu adminMenu; // Меню за администрация (ще го добавим в FXML)

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roleLabel;

    private final CashRegisterService cashService = new CashRegisterService();

    public static MainMenuController instance;

    // Този метод се вика автоматично при старт
    @FXML
    public void initialize() {
        instance = this;
        updateBalance();

        UserSession session = UserSession.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            User user = session.getCurrentUser();

            // 3. Показваме името (ако FullName е празно, показваме username)
            String displayName = (user.getFullName() != null && !user.getFullName().isEmpty())
                    ? user.getFullName()
                    : user.getUsername();

            welcomeLabel.setText("Добре дошли, " + displayName + "!");

            // 4. Показваме ролята и я оцветяваме
            String roleName = user.getRole().getRoleName();
            roleLabel.setText("Роля: " + roleName);

            // Малък визуален бонус: Червено за Админ, Зелено за Оператор
            if ("ADMIN".equals(roleName)) {
                roleLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;"); // Червено
            } else {
                roleLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;"); // Зелено
            }

            // 5. ТУК ЩЕ БЪДЕ СИГУРНОСТТА (скриването на бутони)
            applySecurity(session);
        }
    }

    public void updateBalance() {
        Double balance = cashService.getCurrentBalance();
        balanceLabel.setText(String.format("%.2f лв.", balance));
    }

    private void applySecurity(UserSession session) {
        if (!session.isAdmin()) {
            // Ако НЕ Е админ, скрий менюто за "Администрация"
            if (adminMenu != null) {
                adminMenu.setVisible(false);
            }
            System.out.println("🔒 Ограничен режим: Оператор");
        } else {
            System.out.println("🔓 Пълен достъп: Администратор");
        }
    }

    /**
     * Този метод се извиква от LoginController след успешен вход.
     * Той попълва екрана с данните на влезлия потребител.
     */
    public void setLoggedInUser(User user) {
        if (user != null) {
            welcomeLabel.setText("Здравей, " + user.getFullName() + "!");
            if (user.getRole() != null) {
                roleLabel.setText("Вие сте влезли като: " + user.getRole().getRoleName());
            }
        }
    }

    @FXML
    public void onRegisterUser() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/register-user-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Регистрация на служител");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Тук няма нужда да обновяваме таблица, просто затваряме прозореца

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onShowProducts() {
        loadView("/com/example/storageinventory/product-list-view.fxml");
        updateBalance();
    }

    @FXML
    public void onShowSuppliers() {
        loadView("/com/example/storageinventory/supplier-list-view.fxml");
        updateBalance();
    }

    @FXML
    public void onShowDeliveries() {
        loadView("/com/example/storageinventory/delivery-list-view.fxml");
        updateBalance();
    }

    @FXML
    public void onShowClients() {
        loadView("/com/example/storageinventory/client-list-view.fxml");
    }

    @FXML
    public void onShowSales() {
        loadView("/com/example/storageinventory/sale-list-view.fxml");
        updateBalance(); // Обновяваме парите след продажба
    }

    @FXML
    public void onLogout() {
        try {
            UserSession.cleanSession(); // Чистим сесията

            // 2. Взимаме сцената чрез mainBorderPane - той никога не е null!
            // Преди тук гърмеше, защото ползваше елемент, който е бил изтрит от екрана.
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.close();

            // Отваряме Логин екрана
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/login-view.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Вход в системата");
            loginStage.setScene(new Scene(loader.load()));
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Помощен метод за зареждане на FXML файлове.
     * Използваме го, за да не пишем try-catch всеки път за всяко меню.
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Слагаме заредения изглед в центъра на BorderPane
            mainBorderPane.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Грешка при зареждане на файл: " + fxmlPath);
        }
    }


}