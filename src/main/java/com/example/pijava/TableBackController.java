package com.example.pijava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TableBackController {


    @FXML
    private Button delete;

    @FXML
    private TableColumn<Project, String> budgetColumn;

    @FXML
    private TableColumn<Project, String> categorieColumn;

    @FXML
    private TableColumn<Project, String> descriptionColumn;

    @FXML
    private TableColumn<Project, String> periodeColumn;

    @FXML
    private TableColumn<Project, String> porteeColumn;

    @FXML
    private TableColumn<Project, Double> titreColumn;




    @FXML
    private TableView<Project> allProjects;
    private Project selectedProject;


    @FXML
    private void initialize() {
        // Gestionnaire de clics sur la TableView
        allProjects.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProject = newSelection;
            }
        });

        initializeTableView();
    }

    @FXML
    private void initializeTableView() {
        try {
            titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
            periodeColumn.setCellValueFactory(new PropertyValueFactory<>("periode"));
            porteeColumn.setCellValueFactory(new PropertyValueFactory<>("portee"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            budgetColumn.setCellValueFactory(new PropertyValueFactory<>("budget"));

            afficherProjets();// Call the method to populate the TableView with projects
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherProjets() {
        try {
            ServiceProject sp = new ServiceProject();
            List<Project> projects = sp.selectAll();

            ObservableList<Project> projetData = FXCollections.observableArrayList(projects);
            allProjects.setItems(projetData);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'affichage des projets", e.getMessage());
        }
    }



    @FXML
    void supprimerProjet(ActionEvent event) {
        if (selectedProject != null) {
            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce projet ?");
            alert.setContentText("Cette action est irréversible.");

            ButtonType buttonTypeOui = new ButtonType("Suuprimer", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNon = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeOui) {
                // Si l'utilisateur a cliqué sur "Oui", supprimer le projet
                try {
                    ServiceProject sp = new ServiceProject();
                    sp.deleteOne(selectedProject);

                    // Remove the project from the TableView
                    allProjects.getItems().remove(selectedProject);

                } catch (SQLException e) {
                    showErrorAlert("Erreur lors de la suppression", e.getMessage());
                }
            }
        } else {
            showErrorAlert("Aucun projet sélectionné", "Veuillez sélectionner un projet à supprimer.");
        }
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


}

