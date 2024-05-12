package com.example.pijava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class condidatureBackController {

    @FXML
    private Button cID;

    @FXML
    private TableView<Condidature> condidaturesTable;

    @FXML
    private TableColumn<Condidature, String> cvCon;

    @FXML
    private TableColumn<Condidature, String> emailCon;

    @FXML
    private TableColumn<Condidature, String> lettreCon;

    @FXML
    private TableColumn<Condidature, String> nomCon;

    @FXML
    private TableColumn<Condidature, Integer> numCon;

    @FXML
    private TableColumn<Condidature, String> prenomCon;

    @FXML
    private TableColumn<Condidature, String> statutsCon;

    @FXML
    private Button pID;



    private Condidature selectedCondidature;




    @FXML
    private void initialize() {
        // Gestionnaire de clics sur la TableView

        condidaturesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCondidature = newSelection;

            }
        });

        initializeTableView();
    }

    @FXML
    private void initializeTableView() {
        try {
            nomCon.setCellValueFactory(new PropertyValueFactory<>("name"));
            prenomCon.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            emailCon.setCellValueFactory(new PropertyValueFactory<>("email"));
            numCon.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
            lettreCon.setCellValueFactory(new PropertyValueFactory<>("lettredemotivation"));
            cvCon.setCellValueFactory(new PropertyValueFactory<>("cv"));
            statutsCon.setCellValueFactory(new PropertyValueFactory<>("status"));
            //idCon.setCellValueFactory(new PropertyValueFactory<>("id"));

            afficherCondidatures(); // Call the method to populate the TableView with projects
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void afficherCondidatures() {
        try {
            ServiceCondidature sc = new ServiceCondidature();
            List<Condidature> condidatures = sc.selectAll();

            ObservableList<Condidature> projetData = FXCollections.observableArrayList(condidatures);
            condidaturesTable.setItems(projetData);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'affichage des projets", e.getMessage());
        }
    }

    @FXML
    void supprimerCondidature(ActionEvent event) {
        if (selectedCondidature != null) {
            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette candidature ?");
            //alert.setContentText("Cette action est irréversible.");

            ButtonType buttonTypeOui = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNon = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);

            // Attendre la réponse de l'utilisateur
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeOui) {
                try {
                    ServiceCondidature sc = new ServiceCondidature();
                    sc.deleteOne(selectedCondidature);

                    condidaturesTable.getItems().remove(selectedCondidature);
                } catch (SQLException e) {
                    showErrorAlert("Erreur lors de la suppression", e.getMessage());
                }
            }
        } else {
            showErrorAlert("Aucune candidature sélectionnée", "Veuillez sélectionner une candidature à supprimer.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

}