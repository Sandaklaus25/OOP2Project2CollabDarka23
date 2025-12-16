package com.example.storageinventory.controller;

import com.example.storageinventory.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roleLabel;

    // Този метод се вика от Login екрана, за да прехвърли данните за юзъра
    public void setLoggedInUser(User user) {
        welcomeLabel.setText("Здравей, " + user.getFullName() + "!");
        if (user.getRole() != null) {
            roleLabel.setText("Вие сте влезли като: " + user.getRole().getRoleName());
        }
    }

    @FXML
    public void onLogout() {
        // Взимаме текущия прозорец и го затваряме
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
        System.out.println("Потребителят излезе от системата.");
    }
}