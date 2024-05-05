package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import entities.Groupe;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.GroupeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class AjouterGroupe {

    @FXML
    private Label descriptionErrorLabel;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField groupeTextField;

    @FXML
    private Label imageErrorLabel;

    @FXML
    private TextField imageTextField;

    @FXML
    private Label nomErrorLabel;

    @FXML
    private TextField nomTextField;

    @FXML
    private Label groupeErrorLabel; // Added this line

    private GroupeService groupeService = new GroupeService();

    @FXML
    void ajouterGroupe(ActionEvent event) {
        String groupe = groupeTextField.getText();
        String description = descriptionTextField.getText();
        String nom = nomTextField.getText();
        String image = imageTextField.getText();

        boolean isInputValid = true;

        if (nom.isEmpty()) {
            groupeErrorLabel.setText("Nom est requis"); // Updated this line
            isInputValid = false;
        }

        if (isInputValid) {
            LocalDate dateDeCreation = LocalDate.now();
            Groupe nouveauGroupe = new Groupe();
            nouveauGroupe.setGroupe(groupe);
            nouveauGroupe.setDescription(description);
            nouveauGroupe.setNom(nom);
            nouveauGroupe.setImage(image);
            nouveauGroupe.setDateDeCreation(dateDeCreation);

            groupeService.create(nouveauGroupe);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Groupe ajouté avec succès");
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        Stage stage = (Stage) groupeTextField.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String imageFolder = "/img";
            File folder = new File(imageFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String imageName = file.getName();
            File destFile = new File(imageFolder + File.separator + imageName); // Use File.separator for platform independence
            try {
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                String imagePath = destFile.toURI().toString();
                imageTextField.setText(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de sauvegarder l'image. Vérifiez les permissions.");
            }
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private Button goback;

    @FXML
    void gotoaffiche(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGroupe.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) goback.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
