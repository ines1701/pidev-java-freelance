package com.example.jessem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Contrat;
import services.ServiceContrat;
import utils.InputValidation;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class AddContrat implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmerButton;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField montantTextField;

    @FXML
    private TextField nomClientTextField;

    @FXML
    private DatePicker dateDeContratPicker; // DatePicker for selecting the contract date

    private ListContratController listContratController;

    public void setListContratController(ListContratController listContratController) {
        this.listContratController = listContratController;
    }

    public static final String ACCOUNT_SID = "AC433fe8fab4c198dcddfd6eb075941f90";
    public static final String AUTH_TOKEN = "b5f31261e603e2ad098dbf7f208dccbc";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            Image brandingImage = new Image(getClass().getResource("/images/logo.png").toString());
//            brandingImageView.setImage(brandingImage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insertOne(ActionEvent event) {
        try {
            String selectedNomClient = nomClientTextField.getText();
            String selectedDescription = descriptionTextField.getText();
            String montantText = montantTextField.getText();
            // Get the date from DatePicker and convert it to java.util.Date
            java.util.Date dateDeContrat = null;
            if (dateDeContratPicker.getValue() != null) {
                dateDeContrat = Date.from(dateDeContratPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            if (selectedNomClient.isEmpty() || selectedDescription.isEmpty() || montantText.isEmpty() || dateDeContrat == null) {
                InputValidation.showAlert("Input Error", null, "Please fill in all fields including the date.");
                return;
            }

            Integer selectedMontant = Integer.parseInt(montantText);

            // Pass dateDeContrat to the constructor
            Contrat contrat = new Contrat(selectedNomClient, selectedDescription, selectedMontant, dateDeContrat);

            ServiceContrat st = new ServiceContrat();
            st.insertOne(contrat);
            System.out.println("Contrat added successfully.");
            String recipientPhoneNumber = "+21652724070";
            String messageBody = "hey admin,a new contrat has been created";
            sendSMS(recipientPhoneNumber, messageBody);

            if (listContratController != null) {
                listContratController.refreshList();
            }
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            InputValidation.showAlert("Input Error", null, "Please enter valid numeric values for montant.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to add contrat: " + e.getMessage());
        }
    }

    public static void sendSMS(String recipientPhoneNumber, String messageBody) {
        String twilioPhoneNumber = "++18322205712";

        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber),
                        new PhoneNumber(twilioPhoneNumber),
                        messageBody)
                .create();

        System.out.println("SMS sent successfully. SID: " + message.getSid());
    }

    @FXML
    private void sendSMS(ActionEvent event) {

    }
}
