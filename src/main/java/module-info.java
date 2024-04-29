module com.example.jessem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires com.google.zxing;
    requires itextpdf;
    requires twilio;


    opens com.example.jessem to javafx.fxml;
    exports com.example.jessem;
}