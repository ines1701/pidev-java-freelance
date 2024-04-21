package com.example.jessem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
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

public class UpdateTransaction implements Initializable {

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
    private DatePicker datePicker; // New DatePicker field

    private Transaction selectedTransaction;
    private ListTransactionController listTransactionController;

    public void setListTransactionController(ListTransactionController listTransactionController) {
        this.listTransactionController = listTransactionController;
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/images/logo.png").toString());
            brandingImageView.setImage(brandingImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initData(Transaction transaction) throws SQLException {
        ServiceContrat serviceContrat = new ServiceContrat();
        List<Contrat> contratList = serviceContrat.selectAll();
        selectedTransaction = transaction;

        methodeTextField.setText(selectedTransaction.getMethode_paiement());
        montantTextField.setText(String.valueOf(selectedTransaction.getMontant()));

        contratCombo.getItems().clear();
        contratCombo.getItems().add(selectedTransaction.getContrat());
        contratCombo.getItems().addAll(contratList);

        // Your existing CellFactory and Converter setup for contratCombo...

        contratCombo.getSelectionModel().select(selectedTransaction.getContrat());

        // Safe conversion from java.util.Date to java.time.LocalDate
        if (selectedTransaction.getDate_transaction() != null) {
            Date date = selectedTransaction.getDate_transaction();
            LocalDate localDate;
            // Check if the date is an instance of java.sql.Date
            if (date instanceof java.sql.Date) {
                localDate = ((java.sql.Date) date).toLocalDate();
            } else {
                // It's a java.util.Date
                Instant instant = date.toInstant();
                localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            }
            datePicker.setValue(localDate);
        }
    }

    public void updateOne(ActionEvent event) {
        String selectedMethode = methodeTextField.getText().trim();
        String montantText = montantTextField.getText().trim();
        Contrat selectedContrat = contratCombo.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedMethode.isEmpty() || montantText.isEmpty() || selectedContrat == null || selectedDate == null) {
            InputValidation.showAlert("Input Error", "Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            int selectedMontant = Integer.parseInt(montantText);
            java.util.Date date = java.util.Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Transaction updatedTransaction = new Transaction(selectedMontant, selectedMethode, selectedContrat, date);
            updatedTransaction.setId(selectedTransaction.getId());

            ServiceTransaction serviceTransaction = new ServiceTransaction();
            serviceTransaction.updateOne(updatedTransaction);
            System.out.println("Transaction updated successfully.");
            InputValidation.showAlert("Success", "Update Complete", "Transaction updated successfully.");

            if (listTransactionController != null) {
                listTransactionController.refreshList();
            }

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            InputValidation.showAlert("Input Error", "Validation Error", "Please enter valid numeric values for montant.");
        } catch (SQLException e) {
            InputValidation.showAlert("Database Error", "Update Failure", "Failed to update transaction due to a database error.");
            System.err.println("Failed to update transaction: " + e.getMessage());
        }
    }
}
