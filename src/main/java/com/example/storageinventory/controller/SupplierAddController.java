package com.example.storageinventory.controller;

import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.service.SupplierService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SupplierAddController {

    @FXML private TextField nameField;
    @FXML private TextField vatField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Label errorLabel;

    private final SupplierService service = new SupplierService();
    private boolean saveClicked = false;

    @FXML
    public void onSave() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Името на фирмата е задължително!");
            errorLabel.setVisible(true);
            return;
        }

        try {
            Supplier supplier = new Supplier(
                    nameField.getText(),
                    vatField.getText(),
                    addressField.getText(),
                    phoneField.getText()
            );

            service.saveSupplier(supplier);
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