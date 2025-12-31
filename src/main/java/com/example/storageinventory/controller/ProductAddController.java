package com.example.storageinventory.controller;

import com.example.storageinventory.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductAddController {

    @FXML private TextField nameField;
    @FXML private TextField priceInField;
    @FXML private TextField priceOutField;
    @FXML private TextField criticalMinField;
    @FXML private Label errorLabel;

    private final ProductService productService = new ProductService();
    private boolean saveClicked = false; // Маркер дали е натиснат бутона Запис

    @FXML
    public void onSave() {
        if (!validateInput()) {
            return;
        }

        try {
            // Взимаме данните от полетата и ги превръщаме в числа
            String name = nameField.getText();
            Double priceIn = Double.parseDouble(priceInField.getText());
            Double priceOut = Double.parseDouble(priceOutField.getText());
            Integer criticalMin = Integer.parseInt(criticalMinField.getText());

            // Викаме сървиса да запише в базата
            productService.saveProduct(name, priceIn, priceOut, criticalMin);

            saveClicked = true;
            closeDialog();

        } catch (NumberFormatException e) {
            showError("Моля, въведете валидни числа за цените (напр. 10.50)!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Грешка при запис в базата!");
        }
    }

    @FXML
    public void onCancel() {
        closeDialog();
    }

    private void closeDialog() {
        // Взимаме текущия прозорец и го затваряме
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || priceInField.getText().isEmpty() ||
                priceOutField.getText().isEmpty() || criticalMinField.getText().isEmpty()) {
            showError("Всички полета са задължителни!");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    // Този метод ще го ползваме от главния екран, за да разберем дали да обновим таблицата
    public boolean isSaveClicked() {
        return saveClicked;
    }
}