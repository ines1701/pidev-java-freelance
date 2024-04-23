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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CondidatureController {

    @FXML
    private Button annulerC;

    @FXML
    private Button cvC;

    @FXML
    private TextField emailC;
    @FXML
    private TextField emailCC;

    @FXML
    private Button lettreC;

    @FXML
    private TextField nomC;
    @FXML
    private TextField nomCC;

    @FXML
    private TextField numC;
    @FXML
    private TextField numCC;

    @FXML
    private Button postulerC;

    @FXML
    private TextField prenomC;
    @FXML
    private TextField prenomCC;

    @FXML
    private TextField lettre;

    @FXML
    private TextField cv;
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
    private TableColumn<Condidature, Integer> idCon;
    @FXML
    private Button updateCondidature;
    private Condidature selectedCondidature;
    private Project project;
    @FXML
    private Label projectTitleLabel;

    @FXML
    private void initialize() {
        assert nomC != null : "fx:id=\"nomC\" was not injected: check your FXML file 'AddProject.fxml'.";
        assert prenomC != null : "fx:id=\"prenomC\" was not injected: check your FXML file 'AddProject.fxml'.";
        assert emailC != null : "fx:id=\"emailC\" was not injected: check your FXML file 'AddProject.fxml'.";
        assert numC != null : "fx:id=\"numC\" was not injected: check your FXML file 'AddProject.fxml'.";
        assert lettre != null : "fx:id=\"lettre\" was not injected: check your FXML file 'AddProject.fxml'.";
        assert cv != null : "fx:id=\"cv\" was not injected: check your FXML file 'AddProject.fxml'.";
        //Gestionnaire de clics sur la TableView
        if (condidaturesTable != null) {
            condidaturesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedCondidature = newSelection;

                }
            });
        } else {
            System.err.println("condidaturesTable is null. Check FXML injection.");
        }

        initializeTableView();
    }

    @FXML
    private void openPopup() {
        if (selectedCondidature != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UpdateCondidature.fxml"));
                Parent root = loader.load();

                // Accédez au contrôleur de la page UpdateCondidature
                UpdateCondidatureController updateCondidatureController = loader.getController();

                updateCondidatureController.setCondidatureController(this);
                // Passez les informations de la condidature sélectionnée au contrôleur de la page UpdateCondidature
                updateCondidatureController.initData(selectedCondidature);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Update Condidature");

                popupStage.setScene(new Scene(root));
                popupStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Erreur", "Veuillez sélectionner une condidature à modifier.");
        }
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

    public void chooseLettre(ActionEvent event){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedLettre = fc.showOpenDialog(null);

        if(selectedLettre != null){
            lettre.setText(selectedLettre.getName());
        } else{
            System.out.println("Ce fichier n'est pas valide! ");
        }
    }
    public void chooseCv(ActionEvent event){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedCv = fc.showOpenDialog(null);

        if(selectedCv != null){
            cv.setText(selectedCv.getName());
        } else{
            System.out.println("Ce fichier n'est pas valide! ");
        }
    }

    @FXML
    void ajouterCondidature(ActionEvent event) {
        try {
            // Check if all fields are empty
            if (nomC.getText().isEmpty() || prenomC.getText().isEmpty() || emailC.getText().isEmpty() ||
                    numC.getText().isEmpty() || lettre.getText().isEmpty() || cv.getText().isEmpty()) {
                showErrorAlert("Champs requis", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Check if Nom have at least 5 letters
            if (!nomC.getText().matches("[a-zA-Z]{5,}") ) {
                showErrorAlert("Erreur de saisie", "Le champ Nom doit avoir au moins 5 lettres.");
                return;
            }
            // Check if Prenom have at least 5 letters
            if (!prenomC.getText().matches("[a-zA-Z]{5,}")) {
                showErrorAlert("Erreur de saisie", "Le champs Prénom doit avoir au moins 5 lettres.");
                return;
            }

            // Check if Email has the correct format
            if (!isValidEmail(emailC.getText())) {
                showErrorAlert("Erreur de saisie", "L'adresse Email n'est pas valide.");
                return;
            }

            // Check if NumTel has at least 8 digits
            if (numC.getText().length() < 8) {
                showErrorAlert("Erreur de saisie", "Le champ NumTel doit avoir au moins 8 chiffres.");
                return;
            }

            // Create a new Condidature object with the data from the text fields
            Condidature condidature = new Condidature(
                    nomC.getText(),
                    prenomC.getText(),
                    emailC.getText(),
                    Integer.parseInt(numC.getText()),
                    lettre.getText(),
                    cv.getText(),
                    Integer.parseInt(projectTitleLabel.getText())
            );

            // Insert the new condidature into the database
            ServiceCondidature serviceCondidature = new ServiceCondidature();
            serviceCondidature.insertOne(condidature);

            // Show a success message
            showAlert(Alert.AlertType.INFORMATION, "Confirmation", "La candidature a été ajoutée avec succès!");
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", "Erreur SQL: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de saisie", "Format du numéro est invalide.");
        }
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        // Email regex pattern
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
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
    @FXML
    void annulerCondi(ActionEvent event) {
        Stage stage = (Stage) annulerC.getScene().getWindow();
        stage.close();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void refreshTableView() {
        afficherCondidatures();
    }

    public void setProject(Project project) {
        this.project = project;
        // Now you can use the selected project in your CondidatureController
        if (project != null) {
            projectTitleLabel.setText(String.valueOf(project.getId()));
            // You can similarly update other UI elements with project information
        }
    }

}
