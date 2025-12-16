module com.example.storageinventory {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    requires org.apache.logging.log4j;

    opens com.example.storageinventory to javafx.fxml, org.hibernate.orm.core;

    opens com.example.storageinventory.model to org.hibernate.orm.core;

    exports com.example.storageinventory;

    exports com.example.storageinventory.util;
}