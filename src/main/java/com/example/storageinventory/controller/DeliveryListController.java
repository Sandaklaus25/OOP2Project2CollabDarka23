package com.example.storageinventory.controller;

import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.service.DeliveryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryListController {

    @FXML
    private TableView<Delivery> deliveryTable;
    @FXML
    private TableColumn<Delivery, String> colDate;
    @FXML
    private TableColumn<Delivery, String> colInvoice;
    @FXML
    private TableColumn<Delivery, String> colSupplier;
    @FXML
    private TableColumn<Delivery, String> colProduct;
    @FXML
    private TableColumn<Delivery, Integer> colQuantity;

    private static final Logger logger = Logger.getLogger(DeliveryListController.class.getName());

    private final DeliveryService service = new DeliveryService();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        colInvoice.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colSupplier.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSupplier().getSupplierName()));

        colProduct.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));

        loadData();
    }

    private void loadData() {
        deliveryTable.setItems(FXCollections.observableArrayList(service.getAllDeliveries()));
    }

    @FXML
    public void onAddDelivery() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/delivery-add-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Нова доставка");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            DeliveryAddController controller = loader.getController();
            if (controller.isSaveClicked()) {
                loadData();

                if (MainMenuController.instance != null) {
                    MainMenuController.instance.updateBalance();
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при добавяне на доставка!", e);
        }
    }
}