package com.example.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EventTypeController implements Initializable {

    @FXML
    private TableColumn<EventType, String> CType;

    @FXML
    private TableView<EventType> TableType;

    @FXML
    private Button btnAType;

    @FXML
    private Button btnMType;

    @FXML
    private Button btnSType;

    @FXML
    private TextField tType;

    // Instance de ServiceTypeEvent pour interagir avec la base de données

    private EventType typeSelectionnee;

    private ServiceEventType serviceEventType;

    @FXML
    void AType(ActionEvent event) {
        try {
            EventType eventType = new EventType(tType.getText());
            serviceEventType.insertOne(eventType);
            loadEventTypes(); // Actualiser la TableView après l'ajout
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", "Une erreur est survenue lors de l'ajout de l'événement.");
        }
    }

    @FXML
    void MType(ActionEvent event) {
        if (typeSelectionnee != null) {
            try {
                typeSelectionnee.setLabel(tType.getText());
                serviceEventType.updateOne(typeSelectionnee);
                loadEventTypes(); // Actualiser la TableView après la modification
                clearFields();
            } catch (NumberFormatException | SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            }
        } else {
            showErrorAlert("Aucun EVENT sélectionnée", "Veuillez sélectionner un EVENT à modifier.");
        }
    }

    @FXML
    void SType(ActionEvent event) {
        if (typeSelectionnee != null) {
            try {
                serviceEventType.deleteOne(typeSelectionnee);
                loadEventTypes(); // Actualiser la TableView après la suppression
                clearFields();
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la suppression", e.getMessage());
            }
        } else {
            showErrorAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceEventType = new ServiceEventType(); // Correction : assignez la nouvelle instance à la variable de classe

        // Définition de la valeur de la colonne CType avec le PropertyValueFactory
        CType.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableType.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                typeSelectionnee = newSelection;
                afficherEventSelectionnee();
            }
        });

        loadEventTypes();
    }


    private void loadEventTypes() {
        try {
            if (serviceEventType != null) {
                List<EventType> eventsData = serviceEventType.selectAll();
                if (eventsData != null) {
                    ObservableList<EventType> observableEventTypes = FXCollections.observableArrayList(eventsData);
                    TableType.setItems(observableEventTypes);
                } else {
                    System.err.println("La liste eventsData est null.");
                }
            } else {
                System.err.println("serviceEventType est null.");
            }
        } catch (SQLException e) {
            showErrorAlert("Erreur de chargement", "Une erreur est survenue lors du chargement des types d'événements.");
            e.printStackTrace();
        }
    }



    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void afficherEventSelectionnee() {
        if (typeSelectionnee != null) {
            tType.setText(typeSelectionnee.getLabel());
        }
    }

    private void clearFields() {
        tType.clear();
    }

    @FXML
    void Actype(ActionEvent event) {
        loadEventTypes(); // Actualiser la TableView en rechargeant les types d'événements depuis la base de données
    }

    public List<String> getAllTypes() throws SQLException {
        return serviceEventType.getAllTypes();
    }




}
