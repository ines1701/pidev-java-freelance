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
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EventTypeController implements Initializable {

    @FXML
    private TableColumn<?, ?> CType;

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
    private ServiceEventType serviceEventType;
    private EventType typeSelectionnee;



    @FXML
    void AType(ActionEvent event) {
        try {
            EventType eventType = new EventType(tType.getText());
            serviceEventType.insertOne(eventType);
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", "Une erreur est survenue lors de l'ajout de l'événement.");
        }
    }



    @FXML
    void MType(ActionEvent event) {
        if (typeSelectionnee != null) {
            try {
                typeSelectionnee.setLabel(tType.getText());

                // Créez une instance de ServiceEventType
                ServiceEventType serviceEventType = new ServiceEventType();

                // Appeler la méthode de mise à jour sur cette instance
                serviceEventType.updateOne(typeSelectionnee);

                // Actualiser la TableView après la mise à jour
                afficherType();

                // Effacer les champs de saisie
                clearFields();
            } catch (NumberFormatException | SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            }
        } else {
            showErrorAlert("Aucun EVENT sélectionnée", "Veuillez sélectionner un EVENT à modifier.");
        }
    }




    private void afficherType() {
        try {
            ServiceEventType ServiceEventType = new ServiceEventType();
            List<EventType> eventsData = ServiceEventType.selectAll();
            TableType.setItems((ObservableList<EventType>) eventsData);
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de l'affichage des événements", e.getMessage());
        }
    }
    @FXML
    void SType(ActionEvent event) {

        // Vérifiez si un type d'événement est sélectionné
        if (typeSelectionnee != null) {
            try {
                // Supprimez le type d'événement sélectionné
                serviceEventType.deleteOne(typeSelectionnee);

                // Actualisez la TableView après la suppression
                loadEventTypes();

                // Effacez les champs de saisie
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
        // Initialisation de l'instance de ServiceEventType
        serviceEventType = new ServiceEventType();
        // Configuration de la colonne de la TableView pour afficher les types d'événements
        CType.setCellValueFactory(new PropertyValueFactory<>("label"));

        // Appeler la méthode pour afficher les détails de l'événement sélectionné lorsque la sélection change
        TableType.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                typeSelectionnee = newSelection;
                afficherEventSelectionnee();
            }
        });


        // Chargement des types d'événements depuis la base de données
        loadEventTypes();
    }

    private void loadEventTypes() {
        try {
            // Récupérer les types d'événements depuis la base de données
            List<EventType> eventTypes = serviceEventType.selectAll();

            // Convertir la liste en une ObservableList pour la liaison avec la TableView
            ObservableList<EventType> observableEventTypes = FXCollections.observableArrayList(eventTypes);

            // Lier les données à la TableView
            TableType.setItems(observableEventTypes);
        } catch (SQLException e) {
            showErrorAlert("Erreur de chargement", "Une erreur est survenue lors du chargement des types d'événements.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    public void Atype(ActionEvent actionEvent) {
    }

    // Méthode pour charger les types d'événements depuis la base de données et les afficher dans la TableView




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
        // Actualisez la TableView en rechargeant les types d'événements depuis la base de données
        loadEventTypes();
    }



}
