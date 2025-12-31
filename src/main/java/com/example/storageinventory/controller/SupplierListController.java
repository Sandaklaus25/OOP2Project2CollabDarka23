package com.example.storageinventory.controller;

import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.service.SupplierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SupplierListController {

    @FXML private TableView<Supplier> supplierTable;
    @FXML private TableColumn<Supplier, Long> colId;
    @FXML private TableColumn<Supplier, String> colName;
    @FXML private TableColumn<Supplier, String> colVat;
    @FXML private TableColumn<Supplier, String> colAddress;
    @FXML private TableColumn<Supplier, String> colPhone;

    private final SupplierService supplierService = new SupplierService();

    @FXML
    public void initialize() {
        // Настройваме колоните (текстът в скобите е името на полето в Supplier.java)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colVat.setCellValueFactory(new PropertyValueFactory<>("vatNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        loadData();
    }

    @FXML
    public void onDeleteSupplier() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("Няма избран доставчик!");
            return;
        }

        // 1. Трием от базата
        supplierService.deleteSupplier(selected);

        // 2. Трием от екрана
        supplierTable.getItems().remove(selected);
        System.out.println("Доставчикът е изтрит.");
    }

    @FXML
    public void onAddSupplier() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/supplier-add-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Добавяне на доставчик");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            SupplierAddController controller = loader.getController();
            if (controller.isSaveClicked()) {
                loadData(); // Презарежда таблицата
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        ObservableList<Supplier> list = FXCollections.observableArrayList(supplierService.getAllSuppliers());
        supplierTable.setItems(list);
    }
}