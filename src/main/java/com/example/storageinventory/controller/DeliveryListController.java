package com.example.storageinventory.controller;

import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.service.DeliveryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DeliveryListController {

    @FXML private TableView<Delivery> deliveryTable;
    @FXML private TableColumn<Delivery, String> colDate;
    @FXML private TableColumn<Delivery, String> colInvoice;
    @FXML private TableColumn<Delivery, String> colSupplier;
    @FXML private TableColumn<Delivery, String> colProduct;
    @FXML private TableColumn<Delivery, Integer> colQuantity;

    private final DeliveryService service = new DeliveryService();

    @FXML
    public void initialize() {
        // Стандартни колони
        colDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        colInvoice.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // СПЕЦИАЛНИ КОЛОНИ (Вложени обекти)
        // Тук казваме: "Влез в Supplier и вземи името му"
        colSupplier.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSupplier().getSupplierName()));

        // Тук казваме: "Влез в Product и вземи името му"
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
                loadData(); // Презарежда таблицата след успешен запис
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}