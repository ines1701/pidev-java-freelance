module com.example.jessem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires com.google.zxing;
    requires itextpdf;
    requires twilio;
    requires org.controlsfx.controls;


    opens com.example.jessem to javafx.fxml;
    exports com.example.jessem;
    exports utils;
    opens utils to javafx.fxml;
}