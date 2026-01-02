package com.example.storageinventory.controller;

import com.example.storageinventory.model.Client;
import com.example.storageinventory.service.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientAddController {

    @FXML private TextField nameField;
    @FXML private TextField vatField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Label errorLabel;

    private final ClientService service = new ClientService();
    private boolean saveClicked = false;

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
            // ЛОГИКА ЗА РЕДАКЦИЯ VS НОВ
            if (clientToEdit == null) {
                // НОВ КЛИЕНТ
                clientToEdit = new Client(
                        nameField.getText(),
                        vatField.getText(),
                        addressField.getText(),
                        phoneField.getText()
                );
            } else {
                // РЕДАКЦИЯ (Обновяваме стария обект)
                clientToEdit.setClientName(nameField.getText());
                clientToEdit.setVatNumber(vatField.getText());
                clientToEdit.setAddress(addressField.getText());
                clientToEdit.setPhoneNumber(phoneField.getText());
            }

            service.saveClient(clientToEdit);
            saveClicked = true;
            closeDialog();

        } catch (Exception e) {
            e.printStackTrace();
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