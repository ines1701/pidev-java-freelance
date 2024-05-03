package com.example.jessem;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Contrat;
import services.ServiceContrat;
import utils.pdfgenerator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ListContratController implements Initializable {

    @FXML
    private Button ajouterButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Contrat> contratTableView;

    @FXML
    private TableColumn<Contrat, String> descriptionCol;

    @FXML
    private Button logoutButton;
    @FXML
    private Button pdfbut;

    @FXML
    private TableColumn<Contrat, Integer> montantCol;

    @FXML
    private TableColumn<Contrat, String> nomCol;

    @FXML
    private Button supprimerButton;

    @FXML
    private AnchorPane tuteur_AP;

    @FXML
    private TableColumn<Contrat, String> dateDeContratCol;

    @FXML
    private TextField searchField;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTableView() throws SQLException {
        // Retrieve data from the database
        ServiceContrat serviceContrat = new ServiceContrat();
        List<Contrat> contratList = serviceContrat.selectAll();

        // Clear existing items in the TableView
        contratTableView.getItems().clear();

        // Set cell value factories for each column
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_client()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        montantCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMontant()).asObject());

        // Formatting the date
        dateDeContratCol.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getDateDeContrat();
            String dateStr = "N/A";
            if (date != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStr = dateFormat.format(date);
            }
            return new SimpleStringProperty(dateStr);
        });

        // Add the retrieved data to the TableView
        contratTableView.getItems().addAll(contratList);

        contratTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click detected
                Contrat selectedContrat = contratTableView.getSelectionModel().getSelectedItem();
                if (selectedContrat != null) {
                    // Navigate to UpdateContrat.fxml
                    navigateToUpdateContrat(selectedContrat);
                }
            }
        });
    }
//    @FXML
//    void printLogPdf(ActionEvent event) {
//        try {
//            Contrat selectedLogement = contratTableView.getSelectionModel().getSelectedItem();
//            if (selectedLogement == null) {
//                showAlert("No logement Selected", "Please select a logement to print.");
//                return;
//            }
//// Appeler la méthode generatePdf de la classe pdfgenerator pour générer le PDF avec les détails du logement sélectionné
//            com.example.jessem.PDFGenerator.generatePdf(selectedLogement.getNom_client(), selectedLogement.getDescription(),selectedLogement.getMontant());
//
//
//            showAlert("PDF Created", "Logement details printed to PDF successfully.");
//
//
//
//        } catch (Exception e) {
//            showAlert("Error", "An error occurred while printing the logement to PDF.");
//            e.printStackTrace();
//        }
//    }


    private void navigateToUpdateContrat(Contrat contrat) {
        try {
            // Load the UpdateContrat.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateContrat.fxml"));
            javafx.scene.Parent root = loader.load();

            // Access the controller and pass the selected contrat to it
            UpdateContrat controller = loader.getController();
            controller.initData(contrat);
            controller.setListContratController(this);

            // Show the scene containing the UpdateContrat.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void printLogPdf(ActionEvent event) {
        try {
            Contrat selectedContrat = contratTableView.getSelectionModel().getSelectedItem();
            if (selectedContrat == null) {
                showAlert("No contrat Selected", "Please select a contrat to print.", Alert.AlertType.INFORMATION);
                return;
            }
// Appeler la méthode generatePdf de la classe pdfgenerator pour générer le PDF avec les détails du logement sélectionné
            pdfgenerator.generatePdf(selectedContrat.getNom_client(),selectedContrat.getDescription(),selectedContrat.getMontant());


            showAlert("PDF Created", "Contrat details printed to PDF successfully.", Alert.AlertType.INFORMATION);



            // Open the PDF file
            File file = new File("C:\\Users\\user\\pidev-java-freelance\\Contrat.pdf");
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void deleteOne(ActionEvent event) {
        // Get the selected contrat from the table view
        Contrat selectedContrat = contratTableView.getSelectionModel().getSelectedItem();
        if (selectedContrat != null) {
            // Show a confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation Dialog");
            confirmAlert.setHeaderText("Supprimer contrat");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet contrat ?");

            // Use lambda expression for handling contrat's choice
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Contrat confirmed, proceed with deletion
                    try {
                        // Call the deleteOne method with the selected contrat
                        ServiceContrat serviceContrat = new ServiceContrat();
                        serviceContrat.deleteOne(selectedContrat);
                        // Show a success message
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Delete Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Contrat deleted successfully.");
                        successAlert.showAndWait();
                        // Refresh the table view after deletion
                        populateTableView();
                    } catch (SQLException ex) {
                        // Show an error message if deletion fails
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Delete Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Error deleting contrat: " + ex.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            // Show an alert if no contrat is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a contrat to delete.");
            alert.showAndWait();
        }
    }

    public void refreshList() {
        try {
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAddContrat() {
        try {
            // Load the AddContrat.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddContrat.fxml"));
            javafx.scene.Parent root = loader.load();

            // Access the controller of AddContrat
            AddContrat controller = loader.getController();
            controller.setListContratController(this);

            // Show the scene containing the AddContrat.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.showAndWait(); // Use showAndWait() to wait for the contrat input

            // Refresh the table view after adding a new contrat
            populateTableView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterButtonOnAction(ActionEvent event) {

        navigateToAddContrat();

    }

    public class InputValidation {
        public static void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null); // Remove the header text
            alert.setContentText(message);
            alert.showAndWait();
        }
    }


    @FXML
    void searchUser(ActionEvent event) {
        // Get the search criteria entered by the user from the TextField
        String searchTerm = searchField.getText().trim().toLowerCase();

        if (!searchTerm.isEmpty()) {
            // Filter the list of contracts based on the search criteria
            List<Contrat> filteredContrats = contratTableView.getItems().stream()
                    .filter(contrat -> contrat.getNom_client().toLowerCase().contains(searchTerm) ||
                            contrat.getDescription().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());

            // Clear the TableView and add the filtered contracts
            contratTableView.getItems().clear();
            contratTableView.getItems().addAll(filteredContrats);
        } else {
            // If the search term is empty, repopulate the TableView with all contracts
            try {
                populateTableView(); // Call method to repopulate TableView
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to populate table view: " + e.getMessage(), Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void initialize() {
        // Add a listener to the searchField to detect changes in text
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Get the search term entered by the user
            String searchTerm = newValue.trim().toLowerCase();

            if (searchTerm.isEmpty()) {
                // If the search term is empty, repopulate the TableView with all contracts
                try {
                    populateTableView(); // Call method to repopulate TableView
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to populate table view: " + e.getMessage(), Alert.AlertType.INFORMATION);
                }
            } else {
                // Filter the list of contracts based on the search criteria
                List<Contrat> filteredContrats = contratTableView.getItems().stream()
                        .filter(contrat -> contrat.getNom_client().toLowerCase().contains(searchTerm) ||
                                contrat.getDescription().toLowerCase().contains(searchTerm))
                        .collect(Collectors.toList());

                // Clear the TableView and add the filtered contracts
                contratTableView.getItems().clear();
                contratTableView.getItems().addAll(filteredContrats);
            }
        });
    }
    @FXML
    private void showCategoryStatistics(ActionEvent event) {
        // Get the list of all posts from the table
        List<Contrat> allPosts = contratTableView.getItems();

        // Count occurrences of each category
        Map<String, Long> categoryCounts = allPosts.stream()
                .collect(Collectors.groupingBy(Contrat::getDescription

                        , Collectors.counting()));

        // Create a PieChart Data list
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Long> entry : categoryCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Create a PieChart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Category Statistics");

        // Create a new alert dialog with PieChart as the content
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category Statistics");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(pieChart);
        alert.showAndWait();
    }













}
