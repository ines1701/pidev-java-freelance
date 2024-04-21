module com.example.pidev3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;



    opens com.example.pidev3 to javafx.fxml;
    exports com.example.pidev3;
}