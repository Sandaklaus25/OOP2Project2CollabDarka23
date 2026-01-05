package com.example.storageinventory.controller;

import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.service.DeliveryService;
import com.example.storageinventory.service.ProductService;
import com.example.storageinventory.service.SupplierService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryAddController {

    @FXML private ComboBox<Supplier> supplierCombo;
    @FXML private ComboBox<Product> productCombo;
    @FXML private TextField invoiceField;
    @FXML private TextField quantityField;
    @FXML private Label errorLabel;

    private static final Logger logger = Logger.getLogger(DeliveryAddController.class.getName());

    private final DeliveryService deliveryService = new DeliveryService();
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService = new SupplierService();

    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        supplierCombo.setItems(FXCollections.observableArrayList(supplierService.getAllSuppliers()));
        productCombo.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
    }

    @FXML
    public void onSave() {
        try {
            // Валидация
            if (supplierCombo.getValue() == null || productCombo.getValue() == null ||
                    invoiceField.getText().isEmpty() || quantityField.getText().isEmpty()) {
                showError("Всички полета са задължителни!");
                return;
            }

            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showError("Количеството трябва да е положително!");
                return;
            }

            Delivery delivery = new Delivery(
                    supplierCombo.getValue(),
                    productCombo.getValue(),
                    quantity,
                    invoiceField.getText()
            );

            deliveryService.createDelivery(delivery);

            saveClicked = true;
            closeDialog();

        } catch (NumberFormatException e) {
            showError("Моля въведете цяло число за количество!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при запазване на доставка!", e);

            String realMessage = getRootMessage(e);

            if (realMessage.contains("НЕДОСТАТЪЧНО") || realMessage.contains("Касата")) {
                showError(realMessage);
            } else {
                showError("Грешка: " + realMessage);
            }
        }
    }

    // Помощен метод за намиране на ключовата грешка
    private String getRootMessage(Throwable t) {
        if (t.getMessage() != null && t.getMessage().contains("НЕДОСТАТЪЧНО")) {
            return t.getMessage();
        }
        // Рекурсия
        if (t.getCause() != null) {
            return getRootMessage(t.getCause());
        }
        return t.getMessage() != null ? t.getMessage() : "Неизвестна грешка";
    }

    @FXML
    public void onCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) invoiceField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}