package com.example.storageinventory.controller;

import com.example.storageinventory.model.Product;
import com.example.storageinventory.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductListController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Long> colId;

    @FXML
    private TableColumn<Product, String> colName;

    @FXML
    private TableColumn<Product, Integer> colQuantity;

    @FXML
    private TableColumn<Product, Double> colPriceIn;

    @FXML
    private TableColumn<Product, Double> colPriceOut;

    @FXML
    private TableColumn<Product, Integer> colCritical;

    private final ProductService productService = new ProductService();

    private static final Logger logger = Logger.getLogger(ProductListController.class.getName());

    @FXML
    public void initialize() {
        setupColumns();
        loadData();
    }

    private void setupColumns() {
        // Текстът в скобите трябва да съвпада ТОЧНО с имената на полетата в модела Product
        // Пример: "productName" търси getProductName()
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPriceIn.setCellValueFactory(new PropertyValueFactory<>("deliveryPrice"));
        colPriceOut.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colCritical.setCellValueFactory(new PropertyValueFactory<>("criticalMin"));
    }

    private void loadData() {
        List<Product> productList = productService.getAllProducts();

        // Превръщаме го в специален списък Observable, за да го разбира таблицата
        ObservableList<Product> observableList = FXCollections.observableArrayList(productList);

        // Слагаме го в таблицата
        productTable.setItems(observableList);
    }

    @FXML
    public void onDeleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            System.out.println("Няма избрана стока за триене!");
            return;
        }

        productService.deleteProduct(selectedProduct);
        productTable.getItems().remove(selectedProduct);

        System.out.println("Стоката е изтрита успешно!");
    }

    @FXML
    public void onAddProduct() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/product-add-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Добавяне на стока");
            stage.setScene(new javafx.scene.Scene(root));

            // Правим прозореца "Modal"
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            ProductAddController controller = loader.getController();

            if (controller.isSaveClicked()) {
                loadData();
                System.out.println("Таблицата е обновена!");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при добавяне на стока!", e);
        }
    }

    @FXML
    public void onEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Изберете стока за редакция!");
            return;
        }
        openDialog(selected);
    }

    private void openDialog(Product productToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/storageinventory/product-add-dialog.fxml"));
            Parent root = loader.load();

            ProductAddController controller = loader.getController();

            // АКО Е РЕДАКЦИЯ -> Подаваме данните
            if (productToEdit != null) {
                controller.setProductForEdit(productToEdit);
            }

            Stage stage = new Stage();
            stage.setTitle(productToEdit == null ? "Нова стока" : "Редакция на стока");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isSaveClicked()) {
                loadData();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при зареждане на прозорец", e);
        }
    }
}