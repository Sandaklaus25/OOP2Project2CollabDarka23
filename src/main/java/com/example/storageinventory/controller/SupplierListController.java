package com.example.storageinventory.controller;

import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.service.SupplierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

        supplierService.deleteSupplier(selected);

        supplierTable.getItems().remove(selected);
        System.out.println("Доставчикът е изтрит.");
    }

    @FXML
    public void onAddSupplier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/supplier-add-dialog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Добавяне на доставчик");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Тук също е съкратено
            stage.showAndWait();

            SupplierAddController controller = loader.getController();
            if (controller.isSaveClicked()) {
                loadData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditSupplier() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/supplier-add-dialog.fxml"));
            Parent root = loader.load();

            SupplierAddController controller = loader.getController();
            controller.setSupplierForEdit(selected); // <-- ВАЖНО

            Stage stage = new Stage();
            stage.setTitle("Редакция на доставчик");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isSaveClicked()) {
                loadData();
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