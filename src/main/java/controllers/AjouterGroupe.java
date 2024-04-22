package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.time.LocalDate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AjouterGroupe {

    @FXML
    private Label descriptionErrorLabel;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Label groupeErrorLabel;

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

    private GroupeService groupeService = new GroupeService();

    @FXML
    void ajouterGroupe(ActionEvent event) {
        // Effacer les messages d'erreur précédents
        clearErrorLabels();

        // Récupérer les valeurs saisies
        String groupe = groupeTextField.getText();
        String description = descriptionTextField.getText();
        String nom = nomTextField.getText();
        String image = imageTextField.getText();

        // Valider les entrées
        boolean isInputValid = true;
        if (groupe.isEmpty()) {
            groupeErrorLabel.setText("Groupe est requis");
            isInputValid = false;
        }
        if (description.isEmpty()) {
            descriptionErrorLabel.setText("Description est requise");
            isInputValid = false;
        }
        if (nom.isEmpty()) {
            nomErrorLabel.setText("Nom est requis");
            isInputValid = false;
        }

        // Si toutes les saisies sont valides, créer un nouveau groupe
        if (isInputValid) {
            LocalDate dateDeCreation = LocalDate.now(); // Obtenir la date actuelle
            Groupe nouveauGroupe = new Groupe();
            nouveauGroupe.setGroupe(groupe);
            nouveauGroupe.setDescription(description);
            nouveauGroupe.setNom(nom);
            nouveauGroupe.setImage(image);
            nouveauGroupe.setDateDeCreation(dateDeCreation);

            // Appeler le service pour ajouter le nouveau groupe à la base de données
            groupeService.create(nouveauGroupe);

            // Afficher une notification de succès
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
            // Chemin d'accès au dossier où vous souhaitez enregistrer les images
            String imageFolder = "/img";

            // Créer le dossier s'il n'existe pas déjà
            File folder = new File(imageFolder);
            if (!folder.exists()) {
                folder.mkdirs(); // Créez le dossier et tous les dossiers parents nécessaires
            }

            // Copier le fichier sélectionné vers le dossier des images
            String imageName = file.getName();
            File destFile = new File(imageFolder + imageName);
            try {
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                String imagePath = destFile.toURI().toString();
                imageTextField.setText(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer les erreurs de copie de fichier ici
            }
        }
    }


    // Méthode pour effacer les messages d'erreur précédents
    private void clearErrorLabels() {
        groupeErrorLabel.setText("");
        descriptionErrorLabel.setText("");
        nomErrorLabel.setText("");
    }

    // Méthode pour afficher une notification
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private Button gotoaffichage;

    @FXML
    void gotoaffiche(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGroupe.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) gotoaffichage.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
