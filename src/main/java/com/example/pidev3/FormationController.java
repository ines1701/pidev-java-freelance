package com.example.pidev3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FormationController {

    @FXML
    private TableView<Formation> tableView;

    @FXML
    private TableColumn<Formation, String> colcategorie;

    @FXML
    private TableColumn<Formation, String> coltitre;

    @FXML
    private TableColumn<Formation, String> coltuteur;

    @FXML
    private TableColumn<Formation, String> colupdated;


    @FXML
    private TextField titref;

    @FXML
    private TextField categorief;

    @FXML
    private TextField tuteur;

    @FXML
    private TextField updated;
    @FXML
    private Button partFront;
    @FXML
    private Button ouvrirId;
    // Variable pour stocker la formation sélectionnée
    private Formation formationSelectionnee;


   /* @FXML
    void ajouterFormation(ActionEvent event) {
        try {
            Formation p = new Formation(titref.getText(), categorief.getText(), tuteur.getText(), updated.getText());
            ServiceFormation sp = new ServiceFormation();
            sp.insertOne(p);
            afficherFormations();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation ajoutée avec succès.");
            // Rafraîchir la TableView après l'ajout d'une nouvelle formation
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de saisie", e.getMessage());
        }
    }*/

    @FXML
    void ajouterFormation(ActionEvent event) {
        try {
            // Vérification des longueurs minimales
            if (titref.getText().length() < 4) {
                throw new Exception("Le titre doit contenir au moins 4 caractères.");
            }
            if (categorief.getText().length() < 5) {
                throw new IllegalArgumentException("La catégorie doit contenir au moins 5 caractères.");
            }
            if (tuteur.getText().length() < 8) {
                throw new IllegalArgumentException("Le tuteur doit contenir au moins 8 caractères.");
            }

            // Vérification que le tuteur ne contient pas de chiffres
            if (tuteur.getText().matches(".*\\d.*")) {
                throw new IllegalArgumentException("Le tuteur ne doit pas contenir de chiffres.");
            }
            // Vérification du format de la date et des plages de validité
            String datePattern = "\\d{2}/\\d{2}/2024"; // Format : jj/mm/2024
            if (!updated.getText().matches(datePattern)) {
                throw new IllegalArgumentException("Le format de la date doit être jj/mm/2024 et être valide.");
            }
            // Extraire les parties de la date
            String[] parts = updated.getText().split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Vérifier les plages de validité
            if (day < 1 || day > 31 || month < 1 || month > 12 || year != 2024) {
                throw new IllegalArgumentException("La date doit être valide : jour (1-31), mois (1-12), année (2024).");
            }
            ServiceFormation sf= new ServiceFormation();
            // Vérifier si un événement avec le même titre existe déjà
            if (sf.existsByTitre(titref.getText())) {
                showErrorAlert("Erreur de saisie", "Une formation avec le même titre existe déjà.");
                return; // Arrêter l'exécution si un événement avec le même titre existe déjà
            }


            Formation p = new Formation(titref.getText(), categorief.getText(), tuteur.getText(), updated.getText(),false);
            ServiceFormation sp = new ServiceFormation();
            sp.insertOne(p);
            afficherFormations();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation ajoutée avec succès.");
            // Rafraîchir la TableView après l'ajout d'une nouvelle formation
        } catch (SQLException e) {
            showErrorAlert("Erreur de saisie", e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de saisie", e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Erreur de saisie", e.getMessage());
        }
    }


    /*@FXML
    void modifierFormation(ActionEvent event) {
        if (formationSelectionnee != null) {
            try {
                // Mettre à jour les valeurs de la formation sélectionnée
                formationSelectionnee.setTitre(titref.getText());
                formationSelectionnee.setCategorie(categorief.getText());
                formationSelectionnee.setTuteur(tuteur.getText());
                formationSelectionnee.setUpdated(updated.getText());

                ServiceFormation sp = new ServiceFormation();
                sp.updateOne(formationSelectionnee); // Mettre à jour la formation dans la base de données

                // Mettre à jour la TableView
                tableView.refresh();

                // Réinitialiser les champs de texte
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource modifiée avec succès.");
            } catch (NumberFormatException | SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            }
        } else {
            showErrorAlert("Aucune formation sélectionnée", "Veuillez sélectionner une formation à modifier.");
        }
    }*/

    @FXML
    void modifierFormation(ActionEvent event) {
        if (formationSelectionnee != null) {
            try {
                // Vérification des longueurs minimales
                if (titref.getText().length() < 4) {
                    throw new IllegalArgumentException("Le titre doit contenir au moins 4 caractères.");
                }
                if (categorief.getText().length() < 5) {
                    throw new IllegalArgumentException("La catégorie doit contenir au moins 5 caractères.");
                }
                if (tuteur.getText().length() < 8) {
                    throw new IllegalArgumentException("Le tuteur doit contenir au moins 8 caractères.");
                }

                // Vérification que le tuteur ne contient pas de chiffres
                if (tuteur.getText().matches(".*\\d.*")) {
                    throw new IllegalArgumentException("Le tuteur ne doit pas contenir de chiffres.");
                }

                // Vérification du format de la date et des plages de validité
                String datePattern = "\\d{2}/\\d{2}/2024"; // Format : jj/mm/2024
                if (!updated.getText().matches(datePattern)) {
                    throw new IllegalArgumentException("Le format de la date doit être jj/mm/2024 et être valide.");
                }
                // Extraire les parties de la date
                String[] parts = updated.getText().split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                // Vérifier les plages de validité
                if (day < 1 || day > 31 || month < 1 || month > 12 || year != 2024) {
                    throw new IllegalArgumentException("La date doit être valide : jour (1-31), mois (1-12), année (2024).");
                }

                // Mettre à jour les valeurs de la formation sélectionnée
                formationSelectionnee.setTitre(titref.getText());
                formationSelectionnee.setCategorie(categorief.getText());
                formationSelectionnee.setTuteur(tuteur.getText());
                formationSelectionnee.setUpdated(updated.getText());

                ServiceFormation sp = new ServiceFormation();
                sp.updateOne(formationSelectionnee); // Mettre à jour la formation dans la base de données

                // Mettre à jour la TableView
                tableView.refresh();

                // Réinitialiser les champs de texte
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource modifiée avec succès.");
            } catch (NumberFormatException | SQLException e) {
                showErrorAlert("Erreur lors de la modification", e.getMessage());
            } catch (IllegalArgumentException e) {
                showErrorAlert("Erreur de saisie", e.getMessage());
            }
        } else {
            showErrorAlert("Aucune formation sélectionnée", "Veuillez sélectionner une formation à modifier.");
        }
    }


    @FXML
    void supprimerFormation(ActionEvent event) {
        if (formationSelectionnee != null) {
            try {
                ServiceFormation sp = new ServiceFormation();
                sp.deleteOne(formationSelectionnee); // Supprimer la formation de la base de données

                tableView.getItems().remove(formationSelectionnee); // Supprimer la formation de la TableView

                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource supprimée avec succès.");
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la suppression", e.getMessage());
            }
        } else {
            showErrorAlert("Aucune formation sélectionnée", "Veuillez sélectionner une formation à supprimer.");
        }
    }


    @FXML
    void initialize() {
        assert titref != null : "fx:id=\"titref\" was not injected: check your FXML file 'FormationFXML.fxml'.";
        assert categorief != null : "fx:id=\"categorief\" was not injected: check your FXML file 'FormationFXML.fxml'.";
        assert tuteur != null : "fx:id=\"tuteur\" was not injected: check your FXML file 'FormationFXML.fxml'.";
        assert updated != null : "fx:id=\"updated\" was not injected: check your FXML file 'FormationFXML.fxml'.";
        // Gestionnaire de clics sur la TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                formationSelectionnee = newSelection;
                afficherFormationSelectionnee();
            }
        });
        initializeTableView();
    }

    @FXML
    private void initializeTableView() {
        coltitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colcategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        coltuteur.setCellValueFactory(new PropertyValueFactory<>("tuteur"));
        colupdated.setCellValueFactory(new PropertyValueFactory<>("updated"));

        afficherFormations(); // Appeler la fonction pour afficher les formations
    }

    @FXML
    private void afficherFormations() {
        try {
            ServiceFormation sp = new ServiceFormation();
            List<Formation> formations = sp.selectAll();

            ObservableList<Formation> formationData = FXCollections.observableArrayList(formations);
            tableView.setItems(formationData);
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de l'affichage des formations", e.getMessage());
        }
    }

    private void afficherFormationSelectionnee() {
        if (formationSelectionnee != null) {
            titref.setText(formationSelectionnee.getTitre());
            categorief.setText(formationSelectionnee.getCategorie());
            tuteur.setText(formationSelectionnee.getTuteur());
            updated.setText(formationSelectionnee.getUpdated());
        }
    }

   /* @FXML
    void ouvrirRessources(ActionEvent event) {
        try {
            // Charger la page Ressource.fxml
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/Ressource.fxml"));
            Scene scene =new Scene(loader.load());
            // Créer une nouvelle scène


            // Obtenir la fenêtre principale
            Stage stage = (Stage) ouvrirId.getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Impossible de charger la page Ressource.fxml");
        }
    }*/

    @FXML
    void ouvrirRessources(ActionEvent event) {
        if (formationSelectionnee != null) {
            try {
                // Récupérer l'identifiant de la formation sélectionnée
               int formation_id = formationSelectionnee.getId();
               // Ressource ressource= new Ressource();
               //ressource.setFormation_id(formationSelectionnee.getId());

                // Charger la page Ressource.fxml avec l'identifiant de formation en paramètre
                FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/Ressource.fxml"));
                Scene scene =new Scene(loader.load());

                // Obtenir le contrôleur de la page de ressources
                RessourceController ressourceController = loader.getController();

                // Appeler une méthode du contrôleur de la page de ressources pour afficher les ressources de la formation spécifiée
                ressourceController.afficherRessourcesPourFormation(formation_id);
               ressourceController.setFormationId(formation_id);
                // Créer une nouvelle scène


                // Obtenir la fenêtre principale
                Stage stage = (Stage) ouvrirId.getScene().getWindow();

                // Définir la nouvelle scène
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("Erreur", "Impossible de charger la page Ressource.fxml");
            }
        } else {
            showErrorAlert("Aucune formation sélectionnée", "Veuillez sélectionner une formation.");
        }
    }





    private void clearFields() {
        titref.clear();
        categorief.clear();
        tuteur.clear();
        updated.clear();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void onPartFrontClicked(ActionEvent event) {
        try {
            // Charger la vue Formation.fxml
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/FormationFront.fxml"));
            Scene scene =new Scene(loader.load());

            // Accéder au contrôleur de la vue Formation.fxml
            FormationFrontController formationFrontController = loader.getController();

            // Passer des données éventuelles au contrôleur de la vue Formation.fxml
            // Exemple : formationController.setSomeData(someData);

            // Accéder à la scène principale
            Stage stage = (Stage) partFront.getScene().getWindow();

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
