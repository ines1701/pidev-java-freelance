package com.example.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EventController implements Initializable {

    @FXML
    private TableView<Event> TableEvent;

    @FXML
    private Button btnActualiser;

    @FXML
    private Button btnEventType;

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
    private TableColumn<Event, String> cEtat;

    @FXML
    private TextArea tDescrib;

    @FXML
    private TextField tLieu;

    @FXML
    private TextField tTitre;

    @FXML
    private DatePicker tDate;

    @FXML
    private ChoiceBox<String> choixType;

    private ServiceEvent serviceEvent;
    private Event eventSelectionnee;

    @FXML
    void AjoutEvent(ActionEvent event) {
        // Vérifier la validité des champs
        if (!validateFields()) {
            return; // Arrêter l'exécution si les champs ne sont pas valides
        }

        // Récupérer le titre de l'événement à ajouter
        String titre = tTitre.getText();

        try {
            // Vérifier si un événement avec le même titre existe déjà
            if (serviceEvent.existsByTitre(titre)) {
                showErrorAlert("Erreur de saisie", "Un événement avec le même titre existe déjà.");
                return; // Arrêter l'exécution si un événement avec le même titre existe déjà
            }

            // Créer un nouvel événement
            Event e = new Event(
                    titre,
                    tDescrib.getText(),
                    tLieu.getText(),
                    // Convertir la LocalDate en Timestamp
                    tDate.getValue() != null ? Timestamp.valueOf(tDate.getValue().atStartOfDay()) : null);

            // Insérer l'événement dans la base de données
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
            if (validateFields()) {
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


        // Initialiser le service de gestion des types d'événements
        ServiceEventType serviceEventType = new ServiceEventType();
        loadEventTypes();

        // Charger les types d'événements dans la ChoiceBox

        // Configuration des colonnes de la TableView
        cTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        cDescrib.setCellValueFactory(new PropertyValueFactory<>("describ"));
        cLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        cEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Configuration du listener pour la sélection dans la TableView
        TableEvent.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                eventSelectionnee = newSelection;
                afficherEventSelectionnee();
            }
        });

        // Affichage des événements dans la TableView
        afficherEvent();
    }




    private void initializeTableView() {
        cTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        cDescrib.setCellValueFactory(new PropertyValueFactory<>("describ"));
        cLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        cEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

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

            // Déterminez l'état de chaque événement
            for (Event event : eventsData) {
                if (event.getDate() != null) {
                    if (event.getDate().toLocalDateTime().toLocalDate().isBefore(LocalDate.now())) {
                        event.setEtat("Terminé");
                    } else {
                        event.setEtat("A venir");
                    }
                }
            }

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
        loadEventTypes(); // Actualiser la liste des types d'événements
    }

    private boolean validateFields() {


        String titre = tTitre.getText();
        String describ = tDescrib.getText();
        String lieu = tLieu.getText();
        LocalDate date = tDate.getValue();
        // Vérifier que la description a au moins 15 caractères

        if (titre.isEmpty() || describ.isEmpty() || lieu.isEmpty() || date == null) {
            showErrorAlert("Champs obligatoires non remplis", "Veuillez remplir tous les champs obligatoires.");
            return false;
        }


        // Vérifier que le titre a un maximum de 15 caractères
        if (titre.length() > 15) {
            showErrorAlert("Titre trop long", "Le titre ne peut pas dépasser 15 caractères.");
            return false;
        }

        if (describ.length() < 15) {
            showErrorAlert("Description trop courte", "La description doit avoir au moins 15 caractères.");
            return false;
        }

        // Vérifier que le lieu n'est pas vide
        if (lieu.isEmpty()) {
            showErrorAlert("Lieu manquant", "Veuillez spécifier un lieu pour l'événement.");
            return false;
        }

        // Vérifiez que la date n'est pas vide et qu'elle est postérieure à la date actuelle
        if (date == null || date.isBefore(LocalDate.now())) {
            showErrorAlert("Erreur de saisie", "Veuillez sélectionner une date valide (postérieure à aujourd'hui).");
            return false;
        }


        return true;
    }

    @FXML
    void ouvrirEventType(ActionEvent event) {

    }

    @FXML
    void openType(ActionEvent event) {
        try {
            // Charger EventType.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/EventType.fxml"));
            Scene scene = new Scene(loader.load());

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setScene(scene);

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur d'ouverture", "Une erreur est survenue lors de l'ouverture de l'interface EventType.");
        }
    }



    private void loadEventTypes() {
        try {
            // Créer une instance de ServiceEventType
            ServiceEventType serviceEventType = new ServiceEventType();

            // Récupérer les types d'événements depuis la base de données en utilisant le service ServiceEventType
            List<String> eventTypeLabels = serviceEventType.getAllTypes();

            // Convertir la liste en ObservableList pour la liaison avec la ChoiceBox
            ObservableList<String> observableEventTypes = FXCollections.observableArrayList(eventTypeLabels);

            // Lier les données à la ChoiceBox
            choixType.setItems(observableEventTypes);
        } catch (SQLException e) {
            // Gérer les exceptions si une erreur survient lors du chargement des types d'événements depuis la base de données
            showErrorAlert("Erreur de chargement", "Une erreur est survenue lors du chargement des types d'événements.");
        }
    }


}