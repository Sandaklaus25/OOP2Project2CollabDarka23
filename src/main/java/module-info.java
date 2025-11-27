module com.example.storageinventory {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.storageinventory to javafx.fxml;
    exports com.example.storageinventory;
}