package com.example.pijava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateCondidatureController {

    @FXML
    private Button annulerC;

    @FXML
    private TextField emailC;

    @FXML
    private TextField nomC;

    @FXML
    private TextField numC;

    @FXML
    private TextField prenomC;

    @FXML
    private Button update;
    private Condidature condidature;
    private CondidatureController condidatureController;
    public void setCondidatureController(CondidatureController condidatureController) {
        this.condidatureController = condidatureController;
    }

    @FXML
    void annulerCondi(ActionEvent event) {
        Stage stage = (Stage) annulerC.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void initData(Condidature condidature) {
        // Remplissez les champs de texte avec les données de la condidature sélectionnée
        this.condidature = condidature;
        nomC.setText(condidature.getName());
        prenomC.setText(condidature.getPrenom());
        emailC.setText(condidature.getEmail());
        numC.setText(String.valueOf(condidature.getNum_tel()));
    }
    @FXML
    private void modifierCondidature(ActionEvent event) throws SQLException {
        try {
            // Update the fields that can be modified
            condidature.setName(nomC.getText());
            condidature.setPrenom(prenomC.getText());
            condidature.setEmail((emailC.getText()));
            condidature.setNum_tel(Integer.parseInt(numC.getText()));


            ServiceCondidature serviceCondidature = new ServiceCondidature();
            serviceCondidature.updateOne(condidature);

            // Close the update window after modification
            Stage stage = (Stage) nomC.getScene().getWindow();
            stage.close();

            // Refresh the TableView in the CondidatureController
            condidatureController.refreshTableView();
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur", "Veuillez saisir un numéro de téléphone valide.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }



}
