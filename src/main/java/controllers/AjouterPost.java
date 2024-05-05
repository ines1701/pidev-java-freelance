package controllers;

import entities.Groupe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import entities.Post;
import javafx.stage.Stage;
import services.PostService;

import java.io.IOException;
import java.time.LocalDate;

public class AjouterPost {

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label nomErrorLabel;

    @FXML
    private TextField nomTextField;

    private Long groupeId;

   /* @FXML
    private Button back;*/

    @FXML
    private Button posts;

    private PostService postService = new PostService();

    public void initialize(Long groupeId) {
        this.groupeId = groupeId;
    }

    @FXML
    void ajouterPost(ActionEvent event) {
        String description = descriptionTextArea.getText().trim();
        String nom = nomTextField.getText().trim();

        // Create a new post
        Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setNom(nom);
        newPost.setDatePublication(LocalDate.now()); // Set publication date to current date
        // Set the group for the new post
        Groupe groupe = new Groupe();
        groupe.setId(groupeId);
        newPost.setGroupe(groupe);

        // Save the new post
        postService.create(newPost);

        // Show a success message
        showAlert(Alert.AlertType.INFORMATION, "Post ajouté", "Le post a été ajouté avec succès.");
    }


    // Méthode utilitaire pour afficher une boîte de dialogue
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*@FXML
    void backon(ActionEvent event) {
        loadView("/AfficherGroupe.fxml");
    }*/

    @FXML
    void postson(ActionEvent event) {
        loadView("/AfficherPost.fxml");
    }

    private void loadView(String cheminFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer le stage (fenêtre) actuel
            Stage currentStage = (Stage) posts.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
