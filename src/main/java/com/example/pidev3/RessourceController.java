package com.example.pidev3;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.security.auth.callback.Callback;
import java.io.File;
import java.util.List;

public class RessourceController {

    @FXML
    private TextField description;

    @FXML
    private Label fileR;

    @FXML
    private TextField titreR;
    @FXML
    private Button btnajoutR;
    @FXML
    private MediaView mediaView;
    @FXML
    private TableView<Ressource> tableView;

    @FXML
    private TableColumn<?, ?> titretR;
    @FXML
    private TableColumn<?, ?> descriptionR;

    @FXML
    private TableColumn<?, ?> fichierR;
    @FXML
    private Button btndeleteR;

    @FXML
    private Button btneditR;
    @FXML
    private Button actualiseId;
    @FXML
    private Button retourId;

    private String selectedFilePath;
    private Ressource ressourceSelectionnee;

    @FXML
    void choisirFichier(MouseEvent event){
        FileChooser chooser =new FileChooser();
        chooser.setTitle("select file");
        File selectedFile =chooser.showOpenDialog(null);
        if (selectedFile !=null){
            selectedFilePath = selectedFile.getAbsolutePath();
            fileR.setText(selectedFilePath);
            String url =selectedFile.toURI().toString();
            Media media = new Media(url) ;
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);


        }

    }
    /*@FXML
    void ajouterRessource(ActionEvent event) {

        try {

            // Créer une instance de la ressource avec les données des champs
            Ressource ressource = new Ressource(titreR.getText(), description.getText(), selectedFilePath); // Vous devez créer le constructeur correspondant dans votre classe Ressource

            // Insérer la ressource dans la base de données
            ServiceRessource serviceRessource = new ServiceRessource(); // Vous devez créer cette classe pour gérer les opérations sur les ressources
            serviceRessource.insertOne(ressource); // Méthode pour insérer une ressource dans la base de données
            tableView.getItems().add(ressource);
            // Rafraîchir l'interface ou effectuer d'autres actions après l'ajout
            // Par exemple, effacer les champs ou afficher un message de succès
            titreR.clear();
            description.clear();
            fileR.setText("");
            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource ajoutée avec succès.");
        } catch (SQLException e) {
            // Gérer les erreurs de base de données
            showErrorAlert("Erreur de base de données", "Une erreur est survenue lors de l'ajout de la ressource : " + e.getMessage());
        } catch (Exception e) {
            // Gérer d'autres exceptions imprévues
            showErrorAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }*/

   /* @FXML
    void ajouterRessource(ActionEvent event) {
        try {
            // Créer une instance de Ressource avec les données des champs
            Ressource ressource = new Ressource(titreR.getText(), description.getText(), selectedFilePath);
            ressource.setFormation_id(formationId); // Utiliser l'identifiant de la formation actuelle

            // Insérer la ressource dans la base de données
            ServiceRessource serviceRessource = new ServiceRessource();
            serviceRessource.insertOne(ressource);
            tableView.getItems().add(ressource);

            // Rafraîchir l'interface ou effectuer d'autres actions après l'ajout
            // Par exemple, effacer les champs ou afficher un message de succès
            titreR.clear();
            description.clear();
            fileR.setText("ajouter un fichier");
            mediaView.setMediaPlayer(null);

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource ajoutée avec succès.");
        } catch (SQLException e) {
            // Gérer les erreurs de base de données
            showErrorAlert("Erreur de base de données", "Une erreur est survenue lors de l'ajout de la ressource : " + e.getMessage());
        } catch (Exception e) {
            // Gérer d'autres exceptions imprévues
            showErrorAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }*/

    @FXML
    void ajouterRessource(ActionEvent event) {
        try {
            // Vérifier que les champs sont remplis correctement
            if (titreR.getText().length() < 5) {
                throw new IllegalArgumentException("Le titre doit contenir au moins 5 caractères.");
            }
            if (description.getText().length() < 10) {
                throw new IllegalArgumentException("La description doit contenir au moins 10 caractères.");
            }
            if (selectedFilePath == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un fichier.");
            }

            // Créer une instance de Ressource avec les données des champs
            Ressource ressource = new Ressource(titreR.getText(), description.getText(), selectedFilePath);
            ressource.setFormation_id(formationId); // Utiliser l'identifiant de la formation actuelle

            // Insérer la ressource dans la base de données
            ServiceRessource serviceRessource = new ServiceRessource();
            serviceRessource.insertOne(ressource);
            tableView.getItems().add(ressource);

            // Rafraîchir l'interface ou effectuer d'autres actions après l'ajout
            // Par exemple, effacer les champs ou afficher un message de succès
            titreR.clear();
            description.clear();
            fileR.setText("ajouter un fichier");
            mediaView.setMediaPlayer(null);

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource ajoutée avec succès.");
        } catch (SQLException e) {
            // Gérer les erreurs de base de données
            showErrorAlert("Erreur de base de données", "Une erreur est survenue lors de l'ajout de la ressource : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Gérer les erreurs de saisie
            showErrorAlert("Erreur de saisie", e.getMessage());
        } catch (Exception e) {
            // Gérer d'autres exceptions imprévues
            showErrorAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }





    // Méthode pour afficher une alerte d'erreur
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher une alerte d'information
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }







    @FXML
    void initialize() {
        assert titretR != null : "fx:id=\"titretR\" was not injected: check your FXML file 'VotreFXML.fxml'.";
        assert descriptionR != null : "fx:id=\"descriptionR\" was not injected: check your FXML file 'VotreFXML.fxml'.";
        assert fichierR != null : "fx:id=\"fichierR\" was not injected: check your FXML file 'VotreFXML.fxml'.";
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ressourceSelectionnee = newSelection;
                afficherRessourceSelectionnee();
            }
        });
        initializeTableView();
    }

    private void afficherRessourceSelectionnee() {


        if (ressourceSelectionnee != null) {
            titreR.setText(ressourceSelectionnee.getTitre());
            description.setText(ressourceSelectionnee.getDescription());
            fileR.setText(ressourceSelectionnee.getFile());




        }// else {
        // Si aucune ressource n'est sélectionnée, effacez les champs de texte
        // titreR.clear();
        //description.clear();
        // Effacez également le champ de fichier si nécessaire
        //fileR.clear();
    }



    @FXML
    private void initializeTableView() {
        titretR.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionR.setCellValueFactory(new PropertyValueFactory<>("description"));
        fichierR.setCellValueFactory(new PropertyValueFactory<>("file"));


        afficherRessources(); // Appeler la fonction pour afficher les formations
    }


    private void afficherRessources() {
        try {
            ServiceRessource serviceRessource = new ServiceRessource();
            List<Ressource> ressources = serviceRessource.selectAll();

            // Convertir la liste de ressources en ObservableList
            tableView.getItems().addAll(ressources);

        } catch (SQLException e) {
            showErrorAlert("Erreur lors de l'affichage des ressources", e.getMessage());
        }
    }


   /* @FXML
    void modifierRessource(ActionEvent event) {

        if (ressourceSelectionnee != null) {
            try {
                // Mettre à jour les valeurs de la ressource sélectionnée
                ressourceSelectionnee.setTitre(titreR.getText());
                ressourceSelectionnee.setDescription(description.getText());
                // Mettre à jour le fichier si nécessaire
                ressourceSelectionnee.setFile(fileR.getText());
                ressourceSelectionnee.setFormation_id(formationId);

                // Appeler le service pour mettre à jour la ressource dans la base de données
                ServiceRessource serviceRessource = new ServiceRessource();
                serviceRessource.updateOne(ressourceSelectionnee);

                // Rafraîchir la TableView pour refléter les modifications
                tableView.refresh();

                // Réinitialiser les champs de texte
                titreR.clear();
                description.clear();
                fileR.setText("");

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource modifiée avec succès.");
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune ressource sélectionnée", "Veuillez sélectionner une ressource à modifier.");
        }

    }*/

  /*  @FXML
    void modifierRessource(ActionEvent event) {

        if (ressourceSelectionnee != null) {
            try {
                // Mettre à jour les valeurs de la ressource sélectionnée
                ressourceSelectionnee.setTitre(titreR.getText());
                ressourceSelectionnee.setDescription(description.getText());
                // Mettre à jour le fichier si nécessaire
                ressourceSelectionnee.setFile(fileR.getText());
                ressourceSelectionnee.setFormation_id(formationId);
                tableView.refresh();
                // Appeler le service pour mettre à jour la ressource dans la base de données
                ServiceRessource serviceRessource = new ServiceRessource();
                serviceRessource.updateOne(ressourceSelectionnee);

                // Rafraîchir la TableView pour refléter les modifications
                tableView.refresh();

                // Réinitialiser les champs de texte
                titreR.clear();
                description.clear();
                fileR.setText("ajouter un fichier ");
                mediaView.setMediaPlayer(null);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource modifiée avec succès.");
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune ressource sélectionnée", "Veuillez sélectionner une ressource à modifier.");
        }
    }*/


    @FXML
    void modifierRessource(ActionEvent event) {
        if (ressourceSelectionnee != null) {
            try {
                // Vérifier que les champs sont remplis correctement
                if (titreR.getText().length() < 5) {
                    throw new IllegalArgumentException("Le titre doit contenir au moins 5 caractères.");
                }
                if (description.getText().length() < 10) {
                    throw new IllegalArgumentException("La description doit contenir au moins 10 caractères.");
                }
                if (selectedFilePath == null) {
                    throw new IllegalArgumentException("Veuillez sélectionner un fichier.");
                }

                // Mettre à jour les valeurs de la ressource sélectionnée
                ressourceSelectionnee.setTitre(titreR.getText());
                ressourceSelectionnee.setDescription(description.getText());
                // Mettre à jour le fichier si nécessaire
                ressourceSelectionnee.setFile(fileR.getText());
                ressourceSelectionnee.setFormation_id(formationId);

                // Appeler le service pour mettre à jour la ressource dans la base de données
                ServiceRessource serviceRessource = new ServiceRessource();
                serviceRessource.updateOne(ressourceSelectionnee);

                // Rafraîchir la TableView pour refléter les modifications
                tableView.refresh();

                // Réinitialiser les champs de texte
                titreR.clear();
                description.clear();
                fileR.setText("ajouter un fichier ");
                mediaView.setMediaPlayer(null);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource modifiée avec succès.");
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            } catch (IllegalArgumentException e) {
                // Gérer les erreurs de saisie
                showErrorAlert("Erreur de saisie", e.getMessage());
            } catch (Exception e) {
                // Gérer d'autres exceptions imprévues
                showErrorAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune ressource sélectionnée", "Veuillez sélectionner une ressource à modifier.");
        }
    }



    @FXML
    void supprimerRessource(ActionEvent event) {

        if (ressourceSelectionnee != null) {
            try {
                // Appeler le service pour supprimer la ressource de la base de données
                ServiceRessource serviceRessource = new ServiceRessource();
                serviceRessource.deleteOne(ressourceSelectionnee);

                // Supprimer la ressource de la TableView
                tableView.getItems().remove(ressourceSelectionnee);

                // Réinitialiser les champs de texte
                titreR.clear();
                description.clear();
                fileR.setText("ajouter un fichier");
                mediaView.setMediaPlayer(null);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource supprimée avec succès.");
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la suppression", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune ressource sélectionnée", "Veuillez sélectionner une ressource à supprimer.");
        }

    }



    private int formationId;

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }
    public void afficherRessourcesPourFormation(int formation_id) {
        try {
            ServiceRessource serviceRessource = new ServiceRessource();
            // Utiliser le ServiceRessource ou votre méthode de récupération de données pour obtenir les ressources spécifiques à la formation
            List<Ressource> ressources = serviceRessource.getRessourcesForFormation(formation_id);

            // Convertir la liste de ressources en ObservableList
            ObservableList<Ressource> ressourceData = FXCollections.observableArrayList(ressources);

            // Ajouter les ressources à votre TableView
            tableView.setItems(ressourceData);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Impossible de récupérer les ressources pour cette formation.");
        }
    }

    @FXML
    void actualiserInterface(ActionEvent event) {
        afficherRessourcesPourFormation(formationId);
    }

    @FXML
    void retourVersFormation(ActionEvent event) {
        try {
            // Charger la vue Formation.fxml
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/Formation.fxml"));
            Scene scene =new Scene(loader.load());

            // Accéder au contrôleur de la vue Formation.fxml
            FormationController formationController = loader.getController();

            // Passer des données éventuelles au contrôleur de la vue Formation.fxml
            // Exemple : formationController.setSomeData(someData);

            // Accéder à la scène principale
            Stage stage = (Stage) retourId.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
            // Afficher la nouvelle scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement de la vue Formation.fxml
        }
    }


}






