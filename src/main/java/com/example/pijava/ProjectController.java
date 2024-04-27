package com.example.pijava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProjectController {

    @FXML
    private Button pAjouter;

    @FXML
    private TextField pBudget;

    @FXML
    private TextField pCategorie;

    @FXML
    private TextField pDescription;

    @FXML
    private TextField pPeriode;

    @FXML
    private TextField pPortee;

    @FXML
    private TextField pTitre;

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


    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmerButton;

    @FXML
    private Button delete;
    private Project selectedProject;
    @FXML
    private ComboBox<String> pBoxPrd;

    @FXML
    private ComboBox<String> pBoxPortee;

    /*@FXML
    void ajouterProjet(ActionEvent event) {
        try {
            Project p = new Project(
                    pTitre.getText(),
                    pCategorie.getText(),
                    pPeriode.getText(),
                    pPortee.getText(),
                    pDescription.getText(),
                    Double.parseDouble(pBudget.getText()));
            ServiceProject sp = new ServiceProject();
            sp.insertOne(p);

            // Load the new FXML file for the new page
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/AddProject.fxml"));
            Scene scene =new Scene(loader.load());

            // Obtenir la fenêtre principale
            Stage stage = (Stage) pAjouter.getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", "Erreur SQL: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de saisie", "Format de nombre invalide.");
        } catch (IOException e) {
            showErrorAlert("Erreur de chargement", "Erreur lors du chargement de la nouvelle page.");
        }
    }*/
    @FXML
    void ajouterProjet() {
        try {
            // Get the values from the text fields
            String titre = pTitre.getText();
            String categorie = pCategorie.getText();
            String periode = pBoxPrd.getSelectionModel().getSelectedItem();
            String portee = pBoxPortee.getSelectionModel().getSelectedItem();
            String description = pDescription.getText();
            String budgetText = pBudget.getText();

            // Check if any of the fields are empty
            if (titre.isEmpty() || categorie.isEmpty() || pBoxPrd.getSelectionModel().getSelectedItem()== null || portee.isEmpty() || description.isEmpty() || budgetText.isEmpty()) {
                showErrorAlert("Erreur de saisie", "Tous les champs doivent être remplis.");
                return;
            }

            // Perform validation checks
            if (titre.length() < 5 || !titre.matches("[a-zA-Z]+")) {
                showErrorAlert("Erreur de saisie", "Le titre doit contenir au moins 5 lettres alphabétiques.");
                return;
            }

            if (categorie.length() < 5 || !categorie.matches("[a-zA-Z]+")) {
                showErrorAlert("Erreur de saisie", "La catégorie doit contenir au moins 5 lettres alphabétiques.");
                return;
            }

            if (pBoxPrd.getSelectionModel().getSelectedItem()== null) {
                showErrorAlert("Erreur de saisie", "Vous devez choisir la période du projet.");
                return;
            }

            if (pBoxPortee.getSelectionModel().getSelectedItem()== null) {
                showErrorAlert("Erreur de saisie", "Vous devez choisir la portée du projet.");
                return;
            }

            if (description.length() < 10) {
                showErrorAlert("Erreur de saisie", "La description doit contenir au moins 10 caractères.");
                return;
            }

            double budget = Double.parseDouble(budgetText);
            if (budget <= 799) {
                showErrorAlert("Erreur de saisie", "Le budget doit être supérieur à 799.");
                return;
            }

            // Check if a project with the same title and category already exists
            ServiceProject serviceProject = new ServiceProject();
            if (serviceProject.existsWithSameTitleAndCategory(titre, categorie)) {
                showErrorAlert("Erreur de saisie", "Un projet avec le même titre et la même catégorie existe déjà.");
                return;
            }

            // If all validation checks pass, create a new Project object and insert it into the database
            Project project = new Project(titre, categorie, periode, portee, description, budget);
            serviceProject.insertOne(project);
            clearFields();

            // Refresh the TableView to display the newly added project
            afficherProjets();
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", "Erreur SQL: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de saisie", "Format de nombre invalide.");
        }
    }




    @FXML
    private void initialize() {
        assert pTitre != null : "fx:id=\"pTitre\" was not injected: check your FXML file 'Project.fxml'.";
        assert pCategorie != null : "fx:id=\"pCategorie\" was not injected: check your FXML file 'Project.fxml'.";
        assert pPeriode != null : "fx:id=\"pPeriode\" was not injected: check your FXML file 'Project.fxml'.";
        assert pPortee != null : "fx:id=\"pPortee\" was not injected: check your FXML file 'Project.fxml'.";
        assert pDescription != null : "fx:id=\"pDescription\" was not injected: check your FXML file 'Project.fxml'.";
        assert pBudget != null : "fx:id=\"pBudget\" was not injected: check your FXML file 'Project.fxml'.";
        // Gestionnaire de clics sur la TableView
        allProjects.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProject = newSelection;
                afficherProjetSelectionne();
            }
        });

        initializeTableView();
        ajouterPortee();
        ajouterPeriode();
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
    void modifierProjet(ActionEvent event) {
        if (selectedProject != null) {
            try {
                // Update the values of the selected project based on the text fields
                selectedProject.setTitre(pTitre.getText());
                selectedProject.setCategorie(pCategorie.getText());
                selectedProject.setPeriode(pBoxPrd.getSelectionModel().getSelectedItem());
                selectedProject.setPortee(pBoxPortee.getSelectionModel().getSelectedItem());
                selectedProject.setDescription(pDescription.getText());
                selectedProject.setBudget(Double.parseDouble(pBudget.getText()));

                // Update the project in the database
                ServiceProject sp = new ServiceProject();
                sp.updateOne(selectedProject);

                // Refresh the TableView
                allProjects.refresh();

                // Clear the text fields
                clearFields();
            } catch (NumberFormatException | SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            }
        } else {
            showErrorAlert("Aucun projet sélectionné", "Veuillez sélectionner un projet à modifier.");
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

                    // Clear the fields (optional)
                    clearFields();
                } catch (SQLException e) {
                    showErrorAlert("Erreur lors de la suppression", e.getMessage());
                }
            }
        } else {
            showErrorAlert("Aucun projet sélectionné", "Veuillez sélectionner un projet à supprimer.");
        }
    }


    private void afficherProjetSelectionne() {
        // Set the text fields with the selected project's data
        if (selectedProject != null) {
            pTitre.setText(selectedProject.getTitre());
            pCategorie.setText(selectedProject.getCategorie());
            pBoxPrd.setValue(selectedProject.getPeriode());
            pBoxPortee.setValue(selectedProject.getPortee());
            pDescription.setText(selectedProject.getDescription());
            pBudget.setText(String.valueOf(selectedProject.getBudget()));
        }
    }
//pBoxPrd.getSelectionModel().getSelectedItem()


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    /*@FXML
    private void handleSelection() {
        // Get the selected project from the table view
        Project selectedProject = allProjects.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            // Open the pop-up window and initialize its fields
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UpdateProject.fxml"));
                Parent root = loader.load();

                // Initialize the text fields directly from the loaded FXML file
                TextField pTitre = (TextField) loader.getNamespace().get("pTitre");
                TextField pCategorie = (TextField) loader.getNamespace().get("pCategorie");
                TextField pPeriode = (TextField) loader.getNamespace().get("pPeriode");
                TextField pPortee = (TextField) loader.getNamespace().get("pPortee");
                TextField pDescription = (TextField) loader.getNamespace().get("pDescription");
                TextField pBudget = (TextField) loader.getNamespace().get("pBudget");


                // Set the text fields with the selected project's data
                pTitre.setText(selectedProject.getTitre());
                pCategorie.setText(selectedProject.getCategorie());
                pPeriode.setText(selectedProject.getPeriode());
                pPortee.setText(selectedProject.getPortee());
                pDescription.setText(selectedProject.getDescription());
                pBudget.setText(String.valueOf(selectedProject.getBudget()));

                // Show the pop-up window
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert if no project is selected
            showErrorAlert("Aucun projet sélectionné", "Veuillez sélectionner un projet à modifier.");
        }
    }
*/

    @FXML
    private void clearFields() {
        pTitre.clear();
        pCategorie.clear();
        pBoxPrd.setValue(null);
        pBoxPortee.setValue(null);
        pDescription.clear();
        pBudget.clear();
    }


    /*/Open the Add Form
    public void Open(ActionEvent event) throws IOException{
        try {
            // Load the AddProject.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AddProject.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions (e.g., file not found)
        }
    }*/
    private final String[] periodeList = {"1 à 3 mois", "3 à 6 mois", "plus que 6 mois"};

    public void ajouterPeriode() {
        List<String> listP = new ArrayList<>();
        Collections.addAll(listP, periodeList);

        ObservableList<String> listDataPrd = FXCollections.observableArrayList(listP);
        pBoxPrd.setItems(listDataPrd);
    }
    private final String[] porteeList = {"Petit", "Moyen", "Large"};

    public void ajouterPortee() {
        List<String> listPt = new ArrayList<>();
        Collections.addAll(listPt, porteeList);

        ObservableList<String> listData = FXCollections.observableArrayList(listPt);
        pBoxPortee.setItems(listData);
    }

    //POP UP for all condidatures
    @FXML
    private void openCondidatures() {
        if (selectedProject != null) {
            try {
                // Load the FXML file for the candidate window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AllCondidatures.fxml"));
                Parent root = loader.load();

                // Get the controller of the candidate window
                CondidaturesParProjet controller = loader.getController();

                // Pass the selected project ID to the candidate controller
                controller.setSelectedProject(selectedProject);

                // Show the candidate window
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Les candidatures liées à ce projet!");
                popupStage.setScene(new Scene(root));
                popupStage.showAndWait();
                clearFields();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Erreur", "Veuillez sélectionner un projet.");
        }
    }

}
