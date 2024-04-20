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



    }

    @FXML
    void SType(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation de l'instance de ServiceEventType
        serviceEventType = new ServiceEventType();
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





}
