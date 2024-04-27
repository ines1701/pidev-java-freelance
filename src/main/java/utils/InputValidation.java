package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;

public class InputValidation {

    public static boolean isTextFieldEmpty(String textField) {
        return textField.trim().isEmpty();
    }



    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
