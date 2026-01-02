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

    // Този метод се стартира автоматично, когато се отвори прозореца
    @FXML
    public void initialize() {
        setupColumns();
        loadData();
    }

    private void setupColumns() {
        // ВАЖНО: Текстът в скобите трябва да съвпада ТОЧНО с имената на полетата в Product.java!
        // Например: "productName" търси getProductName()
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPriceIn.setCellValueFactory(new PropertyValueFactory<>("deliveryPrice"));
        colPriceOut.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colCritical.setCellValueFactory(new PropertyValueFactory<>("criticalMin"));
    }

    private void loadData() {
        // 1. Взимаме списъка от базата
        List<Product> productList = productService.getAllProducts();

        // 2. Превръщаме го в специален JavaFX списък (Observable), за да го разбира таблицата
        ObservableList<Product> observableList = FXCollections.observableArrayList(productList);

        // 3. Слагаме го в таблицата
        productTable.setItems(observableList);
    }

    @FXML
    public void onDeleteProduct() {
        // 1. Взимаме избрания ред от таблицата
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            System.out.println("Няма избрана стока за триене!");
            // Тук по-късно може да сложим Alert прозорец за грешка
            return;
        }

        // 2. Питаме сървиса да я изтрие от базата
        // (Трябва да си сигурен, че в ProductService имаш deleteProduct!)
        productService.deleteProduct(selectedProduct);

        // 3. Махаме я и от екрана, за да не трябва да рестартираш програмата
        productTable.getItems().remove(selectedProduct);

        System.out.println("Стоката е изтрита успешно!");
    }

    @FXML
    public void onAddProduct() {
        try {
            // 1. Зареждаме файла за добавяне (диалога)
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/storageinventory/product-add-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            // 2. Създаваме нов прозорец (Stage)
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Добавяне на стока");
            stage.setScene(new javafx.scene.Scene(root));

            // 3. Правим го "Modal" - това означава, че не можеш да цъкаш отзад, докато не затвориш този
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // 4. Показваме го и чакаме да се затвори (Wait)
            stage.showAndWait();

            // 5. След като се затвори, проверяваме дали потребителят е натиснал "Запази"
            // Взимаме контролера на диалога, за да го питаме
            ProductAddController controller = loader.getController();

            if (controller.isSaveClicked()) {
                // Ако е натиснал Save, презареждаме таблицата, за да видим новата стока
                loadData();
                System.out.println("✅ Таблицата е обновена!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("⚠️ Изберете стока за редакция!");
            return;
        }
        openDialog(selected); // Подаваме избрания за редакция
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

            // Ако е натиснат Save -> презареждаме таблицата
            if (controller.isSaveClicked()) {
                loadData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}