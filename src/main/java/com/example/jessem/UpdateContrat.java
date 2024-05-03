package com.example.jessem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Contrat;
import services.ServiceContrat;
import utils.InputValidation;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import static com.example.jessem.ListContratController.InputValidation.showAlert;

public class UpdateContrat implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Button cancelButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button selectImage;
    private String imagePathInDatabase;

    @FXML
    private Button confirmerButton;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField montantTextField;

    @FXML
    private TextField nomClientTextField;

    @FXML
    private DatePicker dateDeContratPicker; // DatePicker for the contract's date

    private Contrat selectedContrat;

    private ListContratController listContratController;

    public void setListContratController(ListContratController listContratController) {
        this.listContratController = listContratController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/images/lock-removebg-preview.png").toString());
            brandingImageView.setImage(brandingImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void initData(Contrat contrat) {
        selectedContrat = contrat;
        nomClientTextField.setText(selectedContrat.getNom_client());
        descriptionTextField.setText(selectedContrat.getDescription());
        montantTextField.setText(String.valueOf(selectedContrat.getMontant()));

        // Set the DatePicker value
        if (selectedContrat.getDateDeContrat() != null) {
            java.util.Date date = selectedContrat.getDateDeContrat();
            LocalDate localDate;
            // Check if the date is an instance of java.sql.Date and handle accordingly
            if (date instanceof java.sql.Date) {
                localDate = ((java.sql.Date) date).toLocalDate();
            } else {
                localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            dateDeContratPicker.setValue(localDate);
        }

        imagePathInDatabase = selectedContrat.getImage();
        if (imagePathInDatabase != null && !imagePathInDatabase.isEmpty()) {
            Image image = new Image(new File(imagePathInDatabase).toURI().toString());
            imageView.setImage(image);
        }
    }

    @FXML
    void updateOne(ActionEvent event) {
        try {
            String selectedNomClient = nomClientTextField.getText().trim();
            String selectedDescription = descriptionTextField.getText().trim();

            // Validate and parse the montant
            int selectedMontant;
            try {
                selectedMontant = Integer.parseInt(montantTextField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please enter a valid numeric value for montant.");
                return;
            }

            // Ensure selectedContrat is not null
            if (selectedContrat == null || selectedContrat.getId() == -1) {
                showAlert("Error", "No contract selected.");
                return;
            }

            // Convert LocalDate from DatePicker to java.util.Date
            Date selectedDateDeContrat = null;
            if (dateDeContratPicker.getValue() != null) {
                // Using java.time.LocalDate from DatePicker and converting to java.util.Date
                LocalDate localDate = dateDeContratPicker.getValue();
                selectedDateDeContrat = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            // Creating the Contrat object with the collected information
            Contrat contrat = new Contrat(selectedContrat.getId(), selectedNomClient, selectedDescription, selectedMontant, selectedDateDeContrat,imagePathInDatabase);
            ServiceContrat st = new ServiceContrat();
            st.updateOne(contrat);
            System.out.println("Contrat updated successfully.");

            // Refresh the list in the main controller if it's not null
            if (listContratController != null) {
                listContratController.refreshList();
            }

            // Close the current stage/window
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update contract due to a database error.");
            System.err.println("Failed to update contrat: " + e.getMessage());
        }
    }

    public void browseImageAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePathInDatabase = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }
}
