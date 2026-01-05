package com.example.storageinventory.controller;

import com.example.storageinventory.model.Product;
import com.example.storageinventory.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductAddController {

    @FXML private TextField nameField;
    @FXML private TextField priceInField;
    @FXML private TextField priceOutField;
    @FXML private TextField criticalMinField;
    @FXML private Label errorLabel;

    private static final Logger logger = Logger.getLogger(ProductAddController.class.getName());

    private final ProductService productService = new ProductService();
    private boolean saveClicked = false; // Маркер дали е натиснат бутона Запис

    private Product productToEdit;

    private int hiddenQuantity = 0;

    public void setProductForEdit(Product product) {
        this.productToEdit = product;

        nameField.setText(product.getProductName());
        priceInField.setText(String.valueOf(product.getDeliveryPrice()));
        priceOutField.setText(String.valueOf(product.getSalePrice()));

        if (product.getCriticalMin() != null) {
            criticalMinField.setText(String.valueOf(product.getCriticalMin()));
        }

        // Запомняме текущата наличност в променливата, а не в поле!
        this.hiddenQuantity = product.getQuantity();
    }

    @FXML
    public void onSave() {
        errorLabel.setVisible(false);

        if (nameField.getText().isEmpty() ||
                priceInField.getText().isEmpty() ||
                priceOutField.getText().isEmpty()) {

            errorLabel.setText("Моля, попълнете име и цени!");
            errorLabel.setVisible(true);
            return;
        }

        try {
            String name = nameField.getText();
            double dPrice = Double.parseDouble(priceInField.getText());
            double sPrice = Double.parseDouble(priceOutField.getText());

            int critMin = 0;
            if (!criticalMinField.getText().isEmpty()) {
                critMin = Integer.parseInt(criticalMinField.getText());
            }

            // Тук ползваме скритата променлива hiddenQuantity
            // Ако е нов продукт, тя си е 0.
            // Ако е редакция, тя пази старото число (напр. 10).

            if (productToEdit == null) {
                // Нов продукт (hiddenQuantity е 0)
                productToEdit = new Product(name, hiddenQuantity, dPrice, sPrice, critMin);
            } else {
                // Редакция
                productToEdit.setProductName(name);
                productToEdit.setQuantity(hiddenQuantity);
                productToEdit.setDeliveryPrice(dPrice);
                productToEdit.setSalePrice(sPrice);
                productToEdit.setCriticalMin(critMin);
            }

            productService.saveProduct(productToEdit);

            saveClicked = true;
            closeDialog();

        } catch (NumberFormatException e) {
            errorLabel.setText("Грешка: Въведете валидни числа!");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при запазване на стока!", e);
            errorLabel.setText("Техническа грешка!");
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

    @SuppressWarnings("unused")
    private boolean validateInput() {
        if (nameField.getText().isEmpty() || priceInField.getText().isEmpty() ||
                priceOutField.getText().isEmpty() || criticalMinField.getText().isEmpty()) {
            showError("Всички полета са задължителни!");
            return false;
        }
        return true;
    }

    @SuppressWarnings("SameParameterValue")
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    // Ползва се при главният екран
    public boolean isSaveClicked() {
        return saveClicked;
    }
}