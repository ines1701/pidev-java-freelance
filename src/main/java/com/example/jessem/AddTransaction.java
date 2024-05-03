package com.example.jessem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Contrat;
import models.Transaction;
import services.ServiceContrat;
import services.ServiceTransaction;
import utils.InputValidation;

import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import org.controlsfx.control.Notifications;

public class AddTransaction implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmerButton;

    @FXML
    private ComboBox<Contrat> contratCombo;

    @FXML
    private TextField methodeTextField;

    @FXML
    private TextField montantTextField;
    @FXML
    private DatePicker datePicker;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Populate the ComboBox with Tansaction objects
            ServiceContrat contratService = new ServiceContrat();
            List<Contrat> contrats = contratService.selectAll();
            contratCombo.getItems().addAll(contrats);

            // Display only  in the ComboBox
            contratCombo.setCellFactory(param -> new ListCell<Contrat>() {
                @Override
                protected void updateItem(Contrat contrat, boolean empty) {
                    super.updateItem(contrat, empty);

                    if (empty || contrat == null) {
                        setText(null);
                    } else {
                        setText(contrat.getDescription());
                    }
                }
            });

            // Ensure that the email is displayed in the ComboBox when a tansaction is selected
            contratCombo.setButtonCell(new ListCell<Contrat>() {
                @Override
                protected void updateItem(Contrat contrat, boolean empty) {
                    super.updateItem(contrat, empty);

                    if (empty || contrat == null) {
                        setText(null);
                    } else {
                        setText(contrat.getDescription());
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try {
//            Image brandingImage = new Image(getClass().getResource("/images/logo.png").toString());
//            brandingImageView.setImage(brandingImage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }



    @FXML
    void insertOne(ActionEvent event) {
        try {
            String selectedMethode = methodeTextField.getText();
            int selectedMontant = Integer.parseInt(montantTextField.getText());
            Contrat selectedContrat = contratCombo.getValue();
            LocalDate selectedDate = datePicker.getValue(); // Get the selected date from the DatePicker

            if (selectedMethode.isEmpty() || selectedContrat == null || selectedDate == null) {
                InputValidation.showAlert("Input Error", null, "Please fill in all fields.");
                return;
            }

            // Convert LocalDate to Date
            Instant instant = selectedDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date currentDate = Date.from(instant);

            // Create a new Transaction object with retrieved values
            Transaction transaction = new Transaction(selectedMontant, selectedMethode, selectedContrat, currentDate);

            ServiceTransaction transactionService = new ServiceTransaction();
            transactionService.insertOne(transaction);
            System.out.println("Transaction added successfully.");
            showNotification("Success", "transaction added successfully!");

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            InputValidation.showAlert("Input Error", null, "Please enter a valid numeric value for montant.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to add transaction: " + e.getMessage());
        }
    }
    private void showNotification(String title, String content) {
        Notifications notification =Notifications.create()
                .title(title)
                .text(content);

        Platform.runLater(() -> notification.showInformation());
    }















}
