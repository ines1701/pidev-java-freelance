package com.example.test1;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EventsViewController implements Initializable {

    @FXML
    private GridPane gridEvent;

    @FXML
    private TextField searchField;

    private ServiceEvent serviceEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Créer une instance du service ServiceEvent
        serviceEvent = new ServiceEvent();

        // Afficher tous les événements au démarrage
        showAllEvents();
    }

    @FXML
    private void searchEventsByTitle() {
        String title = searchField.getText();
        if (title.isEmpty()) {
            // Si le champ de recherche est vide, afficher tous les événements
            showAllEvents();
        } else {
            // Sinon, effectuer une recherche par titre
            try {
                ObservableList<Event> events = serviceEvent.searchByTitle(title);
                displayEvents(events);
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs lors de la recherche dans la base de données
            }
        }
    }

    private void showAllEvents() {
        try {
            ObservableList<Event> events = serviceEvent.selectAll();
            displayEvents(events);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs lors du chargement des événements depuis la base de données
        }
    }

    // Méthode pour créer un élément visuel pour chaque événement
    private void displayEvents(ObservableList<Event> events) {
        gridEvent.getChildren().clear(); // Effacer les événements précédents

        // Parcourir la liste des événements
        for (Event event : events) {
            // Créer un élément visuel pour chaque événement
            Pane eventPane = createEventPane(event);
            // Ajouter l'élément visuel au GridPane
            gridEvent.add(eventPane, 0, gridEvent.getChildren().size());
        }
    }

    // Méthode pour créer un élément visuel pour chaque événement
    private Pane createEventPane(Event event) {
        // Créer un Pane pour représenter l'événement
        Pane eventPane = new Pane();
        eventPane.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 1px;");

        // Ajouter des contrôles pour afficher les détails de l'événement
        Label titleLabel = new Label(event.getTitre());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label descriptionLabel = new Label(event.getDescrib());

        // Vérifier si la date de l'événement est null avant de l'afficher
        String dateString = event.getDate() != null ? event.getDate().toString() : "Date non spécifiée";
        Label dateLabel = new Label(dateString);

        // Définir la disposition des contrôles dans le Pane
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(10);
        descriptionLabel.setLayoutX(10);
        descriptionLabel.setLayoutY(30);
        dateLabel.setLayoutX(10);
        dateLabel.setLayoutY(50);

        // Ajouter les contrôles à l'élément visuel
        eventPane.getChildren().addAll(titleLabel, descriptionLabel, dateLabel);

        return eventPane;
    }
}
