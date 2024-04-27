package com.example.pijava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CondidaturesParProjet {
    private Project project;

    @FXML
    private Button RefuseC;

    @FXML
    private Button acceptC;

    @FXML
    private TableColumn<Condidature, String> cc;

    @FXML
    private TableColumn<Condidature, String> ec;

    @FXML
    private TableColumn<Condidature, String> lc;

    @FXML
    private TableColumn<Condidature, String> nc;

    @FXML
    private TableColumn<Condidature, Integer> nuc;

    @FXML
    private TableColumn<Condidature, String> pc;

    @FXML
    private TableColumn<Condidature, String> sc;

    @FXML
    private TableView<Condidature> condidatureP;
    private int selectedProjectId;
    @FXML
    private Label idProject;
    private Condidature selectedCondidature;
    private Connection cnx;

    @FXML
    private void initialize() {

        initializeTableView();
        condidatureP.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCondidature = newSelection;
            }
        });

    }
    @FXML
    private void initializeTableView() {
        try {
            nc.setCellValueFactory(new PropertyValueFactory<>("name"));
            pc.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            ec.setCellValueFactory(new PropertyValueFactory<>("email"));
            nuc.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
            lc.setCellValueFactory(new PropertyValueFactory<>("lettredemotivation"));
            cc.setCellValueFactory(new PropertyValueFactory<>("cv"));
            sc.setCellValueFactory(new PropertyValueFactory<>("status"));

            afficherCondidatures();// Call the method to populate the TableView with projects
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherCondidatures() {
        try {
            ServiceCondidature sc = new ServiceCondidature();

            // Récupérer uniquement les candidatures liées au projet sélectionné
            List<Condidature> condidatures = sc.selectByProjectId(selectedProjectId);

            ObservableList<Condidature> projetData = FXCollections.observableArrayList(condidatures);
            condidatureP.setItems(projetData);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'affichage des candidatures", e.getMessage());
        }
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    public void setSelectedProject(Project project) {
        this.project = project;
        if (project != null) {
            // Obtenez l'ID du projet à partir de l'objet Project
            int selectedProjectId = project.getId();
            try {
                // Utilisez l'ID du projet pour récupérer les candidatures liées
                ServiceCondidature sc = new ServiceCondidature();
                List<Condidature> condidatures = sc.selectByProjectId(selectedProjectId);

                // Mettez à jour l'interface utilisateur avec les données des candidatures
                ObservableList<Condidature> projetData = FXCollections.observableArrayList(condidatures);
                condidatureP.setItems(projetData);
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Erreur lors de l'affichage des candidatures", e.getMessage());
            }
        }
    }

    @FXML
    private void accepterCondidature() {
        if (selectedCondidature != null) {
            System.out.println("Selected Condidature Status: " + selectedCondidature.getStatus()); // Debug statement
            if (selectedCondidature.getStatus().equals("Acceptée")) {
                // Show alert if the status is already "Acceptée"
                showAlert("La condidature a été déjà acceptée!");
            } else {
                try {
                    selectedCondidature.setStatus("Acceptée");
                    ServiceCondidature sc = new ServiceCondidature();
                    sc.updateCondidatureStatus(selectedCondidature.getEmail(), "Acceptée");
                    condidatureP.refresh(); // Refresh the TableView after updating
                    // Show success message
                    showAlert("Condidature est acceptée!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    showErrorAlert("Erreur lors de l'acceptation de la condidature", e.getMessage());
                }
            }
        } else {
            showErrorAlert("Aucune condidature sélectionnée", "Veuillez sélectionner une condidature à accepter.");
        }
    }


    @FXML
    private void refuserCondidature() {
        if (selectedCondidature != null) {
            try {
                ServiceCondidature sc = new ServiceCondidature();
                sc.deleteOne(selectedCondidature); // Pass the selected condidature object
                //supprimer de tableView
                condidatureP.getItems().remove(selectedCondidature);
                // dialog message
                showAlert("La condidature est refusé!");
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Erreur lors du refus de la condidature", e.getMessage());
            }
        } else {
            showErrorAlert("Aucune condidature sélectionnée", "Veuillez sélectionner une condidature à refuser.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
