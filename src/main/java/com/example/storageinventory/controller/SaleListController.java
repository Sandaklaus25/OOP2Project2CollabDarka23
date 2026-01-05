package com.example.storageinventory.controller;

import com.example.storageinventory.model.Sale;
import com.example.storageinventory.service.SaleService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SaleListController {

    @FXML
    private TableView<Sale> saleTable;
    @FXML
    private TableColumn<Sale, String> colDate;
    @FXML
    private TableColumn<Sale, String> colDoc;
    @FXML
    private TableColumn<Sale, String> colClient;
    @FXML
    private TableColumn<Sale, String> colProduct;
    @FXML
    private TableColumn<Sale, Integer> colQuantity;

    private static final Logger logger = Logger.getLogger(SaleListController.class.getName());

    private final SaleService service = new SaleService();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        colDoc.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Вадим имената от вложените обекти
        colClient.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getClient().getClientName()));

        colProduct.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getProductName()));

        loadData();
    }

    private void loadData() {
        saleTable.setItems(FXCollections.observableArrayList(service.getAllSales()));
    }

    @FXML
    public void onAddSale() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/sale-add-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Нова продажба");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            SaleAddController controller = loader.getController();
            if (controller.isSaveClicked()) {
                loadData();

                if (MainMenuController.instance != null) {
                    MainMenuController.instance.updateBalance();
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при добавянето на продажба!", e);
        }
    }
}