package com.example.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EventController implements Initializable {

    @FXML
    private TableView<Event> TableEvent;

    @FXML
    private Button btnActualiser;

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnModif;

    @FXML
    private Button btnSupp;

    @FXML
    private TableColumn<Event, String> cDescrib;

    @FXML
    private TableColumn<Event, String> cLieu;

    @FXML
    private TableColumn<Event, String> cTitre;

    @FXML
    private TableColumn<Event, Timestamp> cDate;


    @FXML
    private TextArea tDescrib;

    @FXML
    private TextField tLieu;

    @FXML
    private TextField tTitre;

    @FXML
    private DatePicker tDate;

    private ServiceEvent serviceEvent;
    private Event eventSelectionnee;

    @FXML
    void AjoutEvent(ActionEvent event) {
        try {
            Event e = new Event(
                    tTitre.getText(),
                    tDescrib.getText(),
                    tLieu.getText(),
                    // Convertir la LocalDate en Timestamp
                    tDate.getValue() != null ? Timestamp.valueOf(tDate.getValue().atStartOfDay()) : null);

            serviceEvent.insertOne(e);
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", "Une erreur est survenue lors de l'ajout de l'événement.");
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de saisie", "Veuillez vérifier les données saisies.");
        }
    }

    @FXML
    void ModifieEvent(ActionEvent event) {
        if (eventSelectionnee != null) {
            try {
                // Récupérer la date sélectionnée du DatePicker
                LocalDate localDate = tDate.getValue();

                // Convertir la LocalDate en Timestamp
                Timestamp sqlDate = localDate != null ? Timestamp.valueOf(localDate.atStartOfDay()) : null;

                // Mettre à jour les attributs de l'événement sélectionné
                eventSelectionnee.setTitre(tTitre.getText());
                eventSelectionnee.setDescrib(tDescrib.getText());
                eventSelectionnee.setLieu(tLieu.getText());
                eventSelectionnee.setDate(sqlDate);

                // Appeler la méthode de mise à jour de ServiceEvent
                serviceEvent.updateOne(eventSelectionnee);

                // Actualiser la TableView après la mise à jour
                afficherEvent();

                // Effacer les champs de saisie
                clearFields();
            } catch (NumberFormatException | SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            }
        } else {
            showErrorAlert("Aucun EVENT sélectionnée", "Veuillez sélectionner un EVENT à modifier.");
        }
    }

    @FXML
    void SupprimeEvent(ActionEvent event) {
        if (eventSelectionnee != null) {
            try {
                // Appeler la méthode de suppression de ServiceEvent
                serviceEvent.deleteOne(eventSelectionnee);

                // Supprimer l'événement de la TableView
                TableEvent.getItems().remove(eventSelectionnee);

                // Effacer les champs de saisie
                clearFields();
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la suppression", e.getMessage());
            }
        } else {
            showErrorAlert("Aucun event sélectionnée", "Veuillez sélectionner un event à supprimer.");
        }
    }


    private void clearFields() {
        tTitre.clear();
        tDescrib.clear();
        tLieu.clear();
        tDate.setValue(null); // Réinitialiser le DatePicker à null
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        cDescrib.setCellValueFactory(new PropertyValueFactory<>("describ"));
        cLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("date")); // Lier la colonne de date

        TableEvent.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                eventSelectionnee = newSelection;
                afficherEventSelectionnee();
            }
        });

        afficherEvent();
    }





    private void initializeTableView() {
        cTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        cDescrib.setCellValueFactory(new PropertyValueFactory<>("describ"));
        cLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Appeler la méthode pour afficher les détails de l'événement sélectionné lorsque la sélection change
        TableEvent.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                eventSelectionnee = newSelection;
                afficherEventSelectionnee();
            }
        });

        afficherEvent();
    }
    private void afficherEvent() {
        try {
            serviceEvent = new ServiceEvent();
            ObservableList<Event> eventsData = serviceEvent.selectAll();
            TableEvent.setItems(eventsData);
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de l'affichage des événements", e.getMessage());
        }
    }

    private void afficherEventSelectionnee() {
        if (eventSelectionnee != null) {
            tTitre.setText(eventSelectionnee.getTitre());
            tDescrib.setText(eventSelectionnee.getDescrib());
            tLieu.setText(eventSelectionnee.getLieu());
            // Afficher la date sélectionnée dans le DatePicker
            tDate.setValue(eventSelectionnee.getDate().toLocalDateTime().toLocalDate());
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void actualiserTableEvent(ActionEvent event) {
        afficherEvent();
    }

}