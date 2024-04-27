package com.example.jessem;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Contrat;
import models.Transaction;
import services.ServiceContrat;
import services.ServiceTransaction;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ListTransactionController implements Initializable {

    @FXML
    private Button ajouterButton;

    @FXML
    private Button cancelButton1;

    @FXML
    private TableColumn<Transaction, String> descriptionCol;

    @FXML
    private Button logoutButton;

    @FXML
    private TableColumn<Transaction, Integer> montantCol;

    @FXML
    private TableColumn<Transaction, String> methodeCol;

    @FXML
    private Button supprimerButton;
    @FXML
    private TableView<Transaction> transactionTableView;

    @FXML
    private AnchorPane tuteur_AP;
    @FXML
    private Pagination pagination;
    private List<Transaction> allTransaction; // List of all posts

    private ObservableList<Transaction> currentPagePosts; // Posts for the current page

    private static final int ROWS_PER_PAGE = 6; // Number of rows per page

    @FXML
    private TableColumn<Transaction, String> dateTransaction; // Ensure this matches the type in FXML, now it's explicitly typed.

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pagination.setPageFactory(this::createPage);
        try {
            ServiceTransaction serviceTransaction = new ServiceTransaction();
            allTransaction = serviceTransaction.selectAll();
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton1.getScene().getWindow();
        stage.close();
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allTransaction.size());
        currentPagePosts = FXCollections.observableArrayList(allTransaction.subList(fromIndex, toIndex));

        // Populate your table with currentPagePosts here
        // For example, if you have a TableView called table:
        transactionTableView.setItems(currentPagePosts);

        return new AnchorPane(); // Return a placeholder node for now
    }


    private void populateTableView() throws SQLException {
        ServiceTransaction serviceTransaction = new ServiceTransaction();
        List<Transaction> transactionList = serviceTransaction.selectAll();
        allTransaction = serviceTransaction.selectAll();
        transactionTableView.getItems().clear();
        int pageCount = (int) Math.ceil((double) allTransaction.size() / ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        createPage(0);

        System.out.println("Transactions fetched: " + transactionList.size()); // Debug print

        transactionTableView.getItems().clear();

        methodeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMethode_paiement()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getContrat() != null ? cellData.getValue().getContrat().getDescription() : "No Description"));
        montantCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMontant()).asObject());
        dateTransaction.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getDate_transaction();
            String formattedDate = (date != null) ? new SimpleDateFormat("yyyy-MM-dd").format(date) : "No Date";
            return new SimpleStringProperty(formattedDate);
        });

        transactionTableView.getItems().addAll(transactionList);

        transactionTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
                if (selectedTransaction != null) {
                    navigateToUpdateTransaction(selectedTransaction);
                }
            }
        });
    }

    private void navigateToUpdateTransaction(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateTransaction.fxml"));
            javafx.scene.Parent root = loader.load();

            UpdateTransaction controller = loader.getController();
            controller.initData(transaction);
            controller.setListTransactionController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteOne(ActionEvent event) {
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation Dialog");
            confirmAlert.setHeaderText("Supprimer Contrat");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet transaction ?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        ServiceTransaction serviceTransaction = new ServiceTransaction();
                        serviceTransaction.deleteOne(selectedTransaction);
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Delete Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Transaction deleted successfully.");
                        successAlert.showAndWait();
                        populateTableView();
                    } catch (SQLException ex) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Delete Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Error deleting transaction: " + ex.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Non Selection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une transaction à supprimer.");
            alert.showAndWait();
        }
    }

    private void navigateToAddTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTransaction.fxml"));
            javafx.scene.Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            populateTableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterButtonOnAction(ActionEvent event) {
        navigateToAddTransaction();
    }

    public void refreshList() {
        try {
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
