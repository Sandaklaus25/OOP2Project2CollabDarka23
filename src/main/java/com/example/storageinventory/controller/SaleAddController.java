package com.example.storageinventory.controller;

import com.example.storageinventory.model.Client;
import com.example.storageinventory.model.Product;
import com.example.storageinventory.model.Sale;
import com.example.storageinventory.service.ClientService;
import com.example.storageinventory.service.ProductService;
import com.example.storageinventory.service.SaleService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SaleAddController {

    @FXML
    private ComboBox<Client> clientCombo;
    @FXML
    private ComboBox<Product> productCombo;
    @FXML
    private TextField docField;
    @FXML
    private TextField quantityField;
    @FXML
    private Label errorLabel;

    private static final Logger logger = Logger.getLogger(SaleAddController.class.getName());

    private final SaleService saleService = new SaleService();
    private final ProductService productService = new ProductService();
    private final ClientService clientService = new ClientService();

    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        clientCombo.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
        productCombo.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
    }

    @FXML
    public void onSave() {
        try {
            if (clientCombo.getValue() == null || productCombo.getValue() == null || docField.getText().isEmpty() || quantityField.getText().isEmpty()) {
                showError("Всички полета са задължителни!");
                return;
            }

            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showError("Количеството трябва да е положително!");
                return;
            }

            Sale sale = new Sale(clientCombo.getValue(), productCombo.getValue(), quantity, docField.getText());

            saleService.createSale(sale);
            saveClicked = true;
            closeDialog();

        } catch (NumberFormatException e) {
            showError("Невалидно число за количество!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при запазване на продажба!", e);

            String realMessage = getRootMessage(e);
            showError(realMessage);
        }
    }

    @FXML
    public void onCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) docField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    // Помощен метод за намиране на истинската грешка
    private String getRootMessage(Throwable t) {
        if (t.getMessage() != null && (t.getMessage().contains("НАЛИЧНОСТ") || t.getMessage().contains("Касата"))) {
            return t.getMessage();
        }
        if (t.getCause() != null) {
            return getRootMessage(t.getCause());
        }
        return t.getMessage() != null ? t.getMessage() : "Грешка";
    }
}