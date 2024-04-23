package com.example.pijava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableView<Condidature> condidatureP;
    private int selectedProjectId;
    @FXML
    private Label idProject;

    @FXML
    private void initialize() {
        initializeTableView();
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


}
