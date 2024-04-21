package com.example.jessem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Contrat;
import services.ServiceContrat;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class FrontController implements Initializable {
    @FXML
    private VBox chosenFruitCard;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView img;
    @FXML
    private Label desriptionLable;

    @FXML
    private Label montantLable;

    @FXML
    private Label nomClientLabel;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Button cancelButton;

    private Contrat selectedContrat;
    private List<Contrat> contratList = new ArrayList<>();
    private Image image;
    private MyListener myListener;

    private List<Contrat> getData() throws SQLException {
        ServiceContrat serviceContrat = new ServiceContrat();
        List<Contrat> contratList = serviceContrat.selectAll();

        return contratList;
    }

    private void setChosenFruit(Contrat contrat) {
        nomClientLabel.setText(contrat.getNom_client());
        desriptionLable.setText(contrat.getDescription());
        montantLable.setText(String.valueOf(contrat.getMontant()));

        Image image = new Image(getClass().getResource("/images/Banque-de-France-–-Particuliers-RIB.jpg").toString());
        img.setImage(image);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            contratList = getData(); // Populate the existing contratList
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        myListener = new MyListener() {
            @Override
            public void onClickListener(Contrat contrat) {
                setChosenFruit(contrat);
                selectedContrat = contrat;
                System.out.println(selectedContrat);
            }
        };

        if (contratList.size() > 0) {
            setChosenFruit(contratList.get(0));
            System.out.println(contratList.size());
        }

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < contratList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(contratList.get(i), myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAddContrat() {
        try {
            // Load the AddContrat.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddContrat.fxml"));
            Parent root = loader.load();

            // Show the scene containing the AddContrat.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Set a listener to execute code after the AddContrat stage is closed
            stage.setOnHiding(event -> {
                // Call the refreshList method to update the grid with the latest data
                refreshList();
            });
            stage.showAndWait();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void navigateToAddTransaction() {
        try {
            // Load the AddContrat.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTransaction.fxml"));
            Parent root = loader.load();

            // Show the scene containing the AddContrat.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Set a listener to execute code after the AddContrat stage is closed
            stage.setOnHiding(event -> {
                // Call the refreshList method to update the grid with the latest data
                refreshList();
            });
            stage.showAndWait();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterButtonOnAction(ActionEvent event) {
        navigateToAddContrat();
    }
    @FXML
    void ajouterButtonOnActionTrasaction(ActionEvent event) {
        navigateToAddTransaction();
    }
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void refreshList() {
        try {
            // Clear the existing list of Contrat objects
            contratList.clear();

            // Fetch the latest data from the database
            contratList = getData();

            // Clear the existing items from the grid
            grid.getChildren().clear();

            // Reset row and column indices
            int column = 0;
            int row = 1;

            // Iterate through the updated contratList
            for (int i = 0; i < contratList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(contratList.get(i), myListener);

                // Check if the column exceeds the limit
                if (column == 3) {
                    column = 0;
                    row++;
                }

                // Add the anchorPane to the grid at the specified column and row
                grid.add(anchorPane, column++, row);

                // Set margins for the anchorPane
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateButtonClicked(ActionEvent event) {
        try {
            if (selectedContrat == null) {
                // Alert the user to select a vehicle first
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Contrat Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a contrat to update.");
                alert.showAndWait();
                return; // Exit the method if no vehicle is selected
            }

            // Load the UpdateContrat.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Updatecontrat.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            UpdateContrat updateContratController = loader.getController();

            // Pass the selected vehicle to the controller
            updateContratController.initData(selectedContrat);

            // Create a new stage for the UpdateContrat scene
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Contrat");
            stage.setScene(new Scene(root));

            // Show the UpdateContrat stage
            stage.showAndWait();

            // Optionally, refresh the list after updating
            refreshList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        // Check if a contrat is selected
        if (selectedContrat != null) {
            try {
                // Call the delete method from your ServiceContrat class to delete the selected contrat
                ServiceContrat serviceContrat = new ServiceContrat();
                serviceContrat.deleteOne(selectedContrat);

                // Optionally, refresh the list after deletion
                refreshList();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle any potential exceptions here
            }
        } else {
            // Alert the user to select a vehicle first
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Vehicle Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a vehicle to delete.");
            alert.showAndWait();
        }
    }


}