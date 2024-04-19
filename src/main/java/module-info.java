module com.example.main {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.main to javafx.fxml;
    exports com.example.main;
}