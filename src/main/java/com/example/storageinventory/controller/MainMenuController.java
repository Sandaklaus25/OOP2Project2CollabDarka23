package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import com.example.storageinventory.service.CashRegisterService;
import com.example.storageinventory.service.NotificationService;
import com.example.storageinventory.util.ReportType;
import com.example.storageinventory.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController {

    // Връзка с главната рамка от FXML файла.

    @FXML
    private Label balanceLabel;

    // Admin-only менюта
    @FXML
    private Menu adminMenu;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox dashboardView;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private ListView<String> notificationList;

    private static final Logger logger = Logger.getLogger(MainMenuController.class.getName());


    private final CashRegisterService cashService = new CashRegisterService();
    private final NotificationService notificationService = new NotificationService();

    public static MainMenuController instance;

    @FXML
    public void initialize() {
        instance = this;
        updateBalance();

        UserSession session = UserSession.getInstance();

        if (session != null && UserSession.getCurrentUser() != null) {
            User user = UserSession.getCurrentUser();

            // Ако FullName е празно, показваме username
            String displayName = (user.getFullName() != null && !user.getFullName().isEmpty())
                    ? user.getFullName()
                    : user.getUsername();

            welcomeLabel.setText("Добре дошли, " + displayName + "!");

            String roleName = user.getRole().getRoleName();
            roleLabel.setText("Роля: " + roleName);

            // Червено за Админ, зелено за Оператор
            if ("ADMIN".equals(roleName)) {
                roleLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;"); // Червено
            } else {
                roleLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;"); // Зелено
            }

            // Сигурност/Скриването на Админ бутони
            applySecurity(session);

            refreshNotifications();
        }
    }

    @FXML
    public void refreshNotifications() {
        if (notificationList != null) {
            List<String> alerts = notificationService.getAlerts();
            notificationList.getItems().setAll(alerts);
        }
    }

    public void updateBalance() {
        Double balance = cashService.getCurrentBalance();
        balanceLabel.setText(String.format("%.2f лв.", balance));
    }

    private void applySecurity(UserSession session) {
        if (!session.isAdmin()) {
            // Ако НЕ Е Админ, скрива менюто за "Администрация"
            if (adminMenu != null) {
                adminMenu.setVisible(false);
            }
            System.out.println("Ограничен режим: Оператор");
        } else {
            System.out.println("Пълен достъп: Администратор");
        }
    }

    @FXML
    public void onShowHome() {
        if (mainBorderPane != null && dashboardView != null) {
            mainBorderPane.setCenter(dashboardView);

            refreshNotifications();
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

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при регистрация на потребител!", e);
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
    public void onReportDeliveries() {
        openReportWindow(ReportType.DELIVERIES);
    }

    @FXML
    public void onReportSales() {
        openReportWindow(ReportType.SALES);
    }

    @FXML
    public void onReportInventory() {
        openReportWindow(ReportType.INVENTORY);
    }

    @FXML
    public void onReportFinancial() {
        openReportWindow(ReportType.FINANCIAL);
    }

    @FXML
    public void onReportCash() {
        openReportWindow(ReportType.CASH_FLOW);
    }

    @FXML
    public void onReportOperators() {
        openReportWindow(ReportType.OPERATORS);
    }

    private void openReportWindow(ReportType type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/report-view.fxml"));
            Parent root = loader.load();

            // Взимаме контролера на справката и му казваме каква справка да покаже
            ReportController controller = loader.getController();
            controller.initReport(type);

            Stage stage = new Stage();
            stage.setTitle("Справка: " + type.getTitle());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при извеждането на справка!", e);
        }
    }

    @FXML
    public void onLogout() {
        try {
            UserSession.cleanSession();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/login-view.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Вход в системата");
            loginStage.setScene(new Scene(loader.load()));
            loginStage.show();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при изход на потребителя!", e);
        }
    }

    //Помощен метод за зареждане на FXML файлове.
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Слагаме заредения изглед в центъра на BorderPane
            mainBorderPane.setCenter(view);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Грешка при зареждането на файл: " + fxmlPath, e);
        }
    }
}