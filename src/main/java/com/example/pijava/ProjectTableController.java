package com.example.pijava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectTableController {

    @FXML
    private TableColumn<Project, Integer> idFA;
    @FXML
    private TableColumn<Project, Integer> budgetFA;

    @FXML
    private TableColumn<Project, String> categorieFA;

    @FXML
    private Button condi;

    @FXML
    private TableColumn<Project, String> descriptionFA;

    @FXML
    private Button formAdd;

    @FXML
    private TableColumn<Project, String> periodeFA;

    @FXML
    private TableColumn<Project, String> porteeFA;

    @FXML
    private TableView<Project> projectsForAll;

    @FXML
    private TableColumn<Project, String> titreFA;
    private Project selectedProject;
    @FXML
    private GridPane menuGrid;

    @FXML
    private ScrollPane menuScroll;
    private ServiceProject serviceProject;
    private Connection cnx;
    private ObservableList<Project> cardListProject = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Gestionnaire de clics sur la TableView
        projectsForAll.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProject = newSelection;
            }
        });
        initializeTableViewForAll();
        afficheCard();
    }
    @FXML
    private void initializeTableViewForAll() {
        try {
            //idFA.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreFA.setCellValueFactory(new PropertyValueFactory<>("titre"));
            categorieFA.setCellValueFactory(new PropertyValueFactory<>("categorie"));
            periodeFA.setCellValueFactory(new PropertyValueFactory<>("periode"));
            porteeFA.setCellValueFactory(new PropertyValueFactory<>("portee"));
            descriptionFA.setCellValueFactory(new PropertyValueFactory<>("description"));
            budgetFA.setCellValueFactory(new PropertyValueFactory<>("budget"));

            afficherProjectsForAll();// Call the method to populate the TableView with projects
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void afficherProjectsForAll() {
        try {
            ServiceProject sp = new ServiceProject();
            List<Project> projects = sp.selectAll();

            ObservableList<Project> projetData = FXCollections.observableArrayList(projects);
            projectsForAll.setItems(projetData);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'affichage des projets", e.getMessage());
        }
    }
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void openCondidature() {
        if (selectedProject != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/addCondidature.fxml"));
                Parent root = loader.load();

                // Accédez au contrôleur de la page AddCondidature
                CondidatureController condidatureController = loader.getController();
                condidatureController.setProject(selectedProject);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Ajouter Condidature");

                popupStage.setScene(new Scene(root));
                popupStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Erreur", "Veuillez sélectionner un projet pour postuler.");
        }
    }


    public ProjectTableController(){
        serviceProject = new ServiceProject();
        cnx = DBConnection.getInstance().getCnx();
    }
    //afficher

    public ObservableList<Project> menuGetData() {
        String sql = "SELECT * FROM project";

        try{
            PreparedStatement prepare =cnx.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();
            Project p;
            while (result.next()){
                p =new Project(
                        result.getInt("id"),
                        result.getString("titre"),
                        result.getString("categorie"),
                        result.getString("periode"),
                        result.getString("portee"),
                        result.getString("description"),
                        result.getDouble("budget"));
                cardListProject.add(p);

            }
        }catch (Exception e){e.printStackTrace();}
        return cardListProject;
    }
    public void afficheCard() {
        // Récupérer les données de formation
        ObservableList<Project> projects = menuGetData();

        // Nettoyer le contenu actuel du menuGrid
        menuGrid.getChildren().clear();

        int row = 0;
        int column = 1;

        for (Project project : projects) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/CardProject.fxml"));
                AnchorPane projectCard = loader.load();
                CardProjectController formCard = loader.getController();
                formCard.setProject(project);

                // Ajouter la carte de formation à menuGrid
                menuGrid.add(projectCard, column, row);

                // Incrémenter les indices de colonne et de ligne
                column++;
                if (column == 4) {
                    column = 1;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
