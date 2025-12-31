package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    // Връзка с главната рамка от FXML файла.
    // Това ни позволява да сменяме съдържанието в средата (Center).
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roleLabel;

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

    /**
     * Действие при кликване на меню "Стоки".
     * Зарежда таблицата със стоките и я слага в центъра на екрана.
     */
    @FXML
    public void onShowProducts() {
        loadView("/com/example/storageinventory/product-list-view.fxml");
    }

    /**
     * Действие при кликване на меню "Изход".
     * Затваря прозореца.
     */

    @FXML
    public void onShowSuppliers() {
        loadView("/com/example/storageinventory/supplier-list-view.fxml");
    }

    @FXML
    public void onLogout() {
        // Взимаме текущия прозорец чрез някой от елементите (напр. welcomeLabel)
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
        System.out.println("Потребителят излезе от системата.");

        // Тук може да се добави логика за отваряне на Login екрана отново, ако желаеш.
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