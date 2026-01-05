package com.example.storageinventory.controller;

import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.service.SupplierService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SupplierAddController {

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

    private static final Logger logger = Logger.getLogger(SupplierAddController.class.getName());

    private final SupplierService service = new SupplierService();
    private boolean saveClicked = false;

    private Supplier supplierToEdit;

    public void setSupplierForEdit(Supplier supplier) {
        this.supplierToEdit = supplier;
        nameField.setText(supplier.getSupplierName());
        vatField.setText(supplier.getVatNumber());
        addressField.setText(supplier.getAddress());
        phoneField.setText(supplier.getPhoneNumber());
    }

    @FXML
    public void onSave() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Името е задължително!");
            errorLabel.setVisible(true);
            return;
        }

        try {
            if (supplierToEdit == null) {
                supplierToEdit = new Supplier(nameField.getText(), vatField.getText(), addressField.getText(), phoneField.getText());
            } else {
                supplierToEdit.setSupplierName(nameField.getText());
                supplierToEdit.setVatNumber(vatField.getText());
                supplierToEdit.setAddress(addressField.getText());
                supplierToEdit.setPhoneNumber(phoneField.getText());
            }

            service.saveSupplier(supplierToEdit);
            saveClicked = true;
            closeDialog();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при запазване на доставчик!", e);
            errorLabel.setText("Грешка при запис на доставчик!");
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