module com.example.pijava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.pijava to javafx.fxml;
    exports com.example.pijava;
}