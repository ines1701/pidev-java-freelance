module com.example.jessem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.jessem to javafx.fxml;
    exports com.example.jessem;
}