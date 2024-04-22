package controllers;

import entities.Groupe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.GroupeService;

import java.io.File;

public class UpdateGroupe {
    private Long groupeId;

    @FXML
    private Label descriptionErrorLabel;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Label groupeErrorLabel;

    @FXML
    private TextField groupeTextField;

    @FXML
    private Label idLabel;

    @FXML
    private Label imageErrorLabel;

    @FXML
    private TextField imageTextField;

    @FXML
    private Label nomErrorLabel;

    @FXML
    private TextField nomTextField;

    private GroupeService groupeService = new GroupeService();

    @FXML
    void updateGroupe(ActionEvent event) {
        String groupe = groupeTextField.getText().trim();
        String description = descriptionTextField.getText().trim();
        String nom = nomTextField.getText().trim();
        String image = imageTextField.getText().trim();

        // Retrieve the existing Groupe object from the database
        Groupe existingGroupe = groupeService.getById((int) groupeId.longValue());
        if (existingGroupe != null) {
            // Update the properties of the existing Groupe object
            existingGroupe.setGroupe(groupe);
            existingGroupe.setDescription(description);
            existingGroupe.setNom(nom);
            existingGroupe.setImage(image);

            // Call the update method of the GroupeService
            groupeService.update(existingGroupe);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Groupe mis à jour", "Le groupe a été mis à jour avec succès.");
        } else {
            // Handle case where the Groupe with the specified ID is not found
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le groupe à mettre à jour n'a pas été trouvé.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void initialize(Long groupeId) {
        this.groupeId = groupeId;
    }

    @FXML
    void uploadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        // Set the file chooser to only allow image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show the file chooser dialog
        Stage stage = (Stage) groupeTextField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // If a file is selected, update the imageTextField with its path
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            imageTextField.setText(imagePath);
        }
    }
    public TextField getNomTextField() {
        return nomTextField;
    }

    // Méthode pour obtenir le champ de texte pour le groupe
    public TextField getGroupeTextField() {
        return groupeTextField;
    }

    // Méthode pour obtenir le champ de texte pour la description
    public TextField getDescriptionTextField() {
        return descriptionTextField;
    }

    // Méthode pour obtenir le champ de texte pour l'image
    public TextField getImageTextField() {
        return imageTextField;
    }
}
