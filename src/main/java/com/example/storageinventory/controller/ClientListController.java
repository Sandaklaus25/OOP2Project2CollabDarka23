package com.example.storageinventory.controller;

import com.example.storageinventory.model.Client;
import com.example.storageinventory.service.ClientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientListController {

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> colName;
    @FXML private TableColumn<Client, String> colVat;
    @FXML private TableColumn<Client, String> colAddress;
    @FXML private TableColumn<Client, String> colPhone;

    private final ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        // Връзваме колоните с полетата от Client.java
        colName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colVat.setCellValueFactory(new PropertyValueFactory<>("vatNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        loadData();
    }

    private void loadData() {
        clientTable.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
    }

    @FXML
    public void onDeleteClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            clientService.deleteClient(selected);
            loadData();
            System.out.println("✅ Клиентът е изтрит.");
        }
    }

    @FXML
    public void onAddClient() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/client-add-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Добавяне на клиент");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            ClientAddController controller = loader.getController();
            if (controller.isSaveClicked()) {
                loadData(); // Презарежда таблицата след успешен запис
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}