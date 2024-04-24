package com.example.pidev3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FormationCardController implements Initializable {

    @FXML
    private AnchorPane formCard;

    @FXML
    private Label formCteg;
    @FXML
    private Button butFavoris;

    @FXML
    private ImageView formimage;

    @FXML
    private Label formTitre;

    @FXML
    private Label formTuteur;

    @FXML
    private Label formUpdate;
    private Image image;
    private Formation formation ;
    public void setFormation(Formation formation){
        this.formation=formation;
        formTitre.setText(formation.getTitre());
        formCteg.setText(formation.getCategorie());
        formTuteur.setText(formation.getTuteur());
        formUpdate.setText(formation.getUpdated());
      //formimage.setImage();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        butFavoris.setOnAction(event -> toggleFavoris());
    }
   /* private void toggleFavoris() {
        if (formation != null) {
            // Inverser le statut "favoris"
            boolean nouveauStatut = !formation.isFavoris();
            formation.setFavoris(nouveauStatut);

            // Mettre à jour le statut "favoris" dans la base de données
            updateFavorisInDatabase(formation.getId(), nouveauStatut);
            afficherNotification();
        }
    }*/


    private void toggleFavoris() {
        if (formation != null) {
            try {
                // Inverser le statut "favoris"
                boolean nouveauStatut = !formation.isFavoris();
                formation.setFavoris(nouveauStatut);

                // Mettre à jour le statut "favoris" dans la base de données
                ServiceFormation serviceFormation = new ServiceFormation();
                serviceFormation.updateFavoris(formation.getId(), nouveauStatut);

                // Afficher la notification appropriée en fonction du changement de statut
                if (nouveauStatut) {
                    showAlert(Alert.AlertType.INFORMATION, "Formation ajoutée aux favoris",
                            "La formation \"" + formation.getTitre() + "\" a été ajoutée à vos favoris.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Formation retirée des favoris",
                            "La formation \"" + formation.getTitre() + "\" a été retirée de vos favoris.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs de mise à jour dans la base de données
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour",
                        "Une erreur s'est produite lors de la mise à jour du statut de favoris.");
            }
        }
    }


    private void updateFavorisInDatabase(int formationId, boolean nouveauStatut) {
        try {
            // Mettre à jour le statut "favoris" dans la base de données
            ServiceFormation serviceFormation = new ServiceFormation();
            serviceFormation.updateFavoris(formationId, nouveauStatut);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de mise à jour dans la base de données
        }
    }
    private void afficherNotification() {
        if (formation.isFavoris()) {
            showAlert(Alert.AlertType.INFORMATION, "Formation ajoutée aux favoris",
                    "La formation \"" + formation.getTitre() + "\" a été ajoutée à vos favoris.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

