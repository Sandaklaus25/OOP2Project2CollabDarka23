package com.example.storageinventory.controller;

import com.example.storageinventory.model.Client;
import com.example.storageinventory.service.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientAddController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField vatField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private Label errorLabel;

    private final ClientService service = new ClientService();
    private boolean saveClicked = false;

    private static final Logger logger = Logger.getLogger(ClientAddController.class.getName());

    private Client clientToEdit;

    public void setClientForEdit(Client client) {
        this.clientToEdit = client;
        nameField.setText(client.getClientName());
        vatField.setText(client.getVatNumber());
        addressField.setText(client.getAddress());
        phoneField.setText(client.getPhoneNumber());
    }

    @FXML
    public void onSave() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Името е задължително!");
            errorLabel.setVisible(true);
            return;
        }

        try {
            // Клиент създаване/редакция
            if (clientToEdit == null) {
                // създаване
                clientToEdit = new Client(nameField.getText(), vatField.getText(), addressField.getText(), phoneField.getText());
            } else {
                // редакция
                clientToEdit.setClientName(nameField.getText());
                clientToEdit.setVatNumber(vatField.getText());
                clientToEdit.setAddress(addressField.getText());
                clientToEdit.setPhoneNumber(phoneField.getText());
            }

            service.saveClient(clientToEdit);
            saveClicked = true;
            closeDialog();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при запазване на клиент!", e);
            errorLabel.setText("Грешка при запис!");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void onCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}