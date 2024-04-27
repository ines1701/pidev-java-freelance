package com.example.pijava;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CardProjectController {

    @FXML
    private Label bCard;

    @FXML
    private Label cCard;

    @FXML
    private Label dCard;

    @FXML
    private Label pCard;

    @FXML
    private Label pnCard;

    @FXML
    private Label prdCard;

    @FXML
    private Button postuler;
    private Project project;

    public void setProject(Project project){
        this.project= project;
        pnCard.setText(project.getTitre());
        cCard.setText(project.getCategorie());
        prdCard.setText(project.getPeriode());
        pCard.setText(project.getPortee());
        bCard.setText(String.valueOf(project.getBudget()));
        dCard.setText(project.getDescription());
    }

   @FXML
    private void openCondidatureH() {
        if (project != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/addCondidature.fxml"));
                Parent root;
                root = loader.load();

                // Accédez au contrôleur de la page AddCondidature
                CondidatureController condidatureController = loader.getController();
                condidatureController.setProject(project);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Ajouter Condidature");

                popupStage.setScene(new Scene(root));
                popupStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void initialize() {
        openCondidatureH();
    }

}
