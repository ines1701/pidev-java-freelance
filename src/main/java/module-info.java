module com.example.test1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.test1 to javafx.fxml;
    exports com.example.test1;
}