package com.example.pidev3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavorisController {
    @FXML
    private AnchorPane menuFormF;

    @FXML
    private GridPane menuGridF;

    @FXML
    private ScrollPane menuScrollF;
    @FXML
    private Button retourF;
    private ServiceFormation serviceFormation;
    private Connection cnx;
    private ObservableList<Formation> cardListFormationFavoris = FXCollections.observableArrayList();
    public FavorisController() {

        serviceFormation = new ServiceFormation();
        cnx = DBConnection.getInstance().getCnx();
    }
    public ObservableList<Formation> menuGetDataF(){
        String sql = "SELECT * FROM formation WHERE favoris=1";

        try{
            PreparedStatement prepare =cnx.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();
            Formation f;
            while (result.next()){
                f =new Formation(
                        result.getInt("id"),
                        result.getString("titre"),
                        result.getString("categorie"),
                        result.getString("tuteur"),
                        result.getString("updated"));
                cardListFormationFavoris.add(f);

            }
        }catch (Exception e){e.printStackTrace();}


        return cardListFormationFavoris;
    }


    public void afficheCardF() {
        // Récupérer les données de formation
        ObservableList<Formation> formations = menuGetDataF();

        // Nettoyer le contenu actuel du menuGrid
        menuGridF.getChildren().clear();

        int row = 0;
        int column = 1;

        for (Formation formation : formations) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/FormationCard.fxml"));
                AnchorPane formationCardF = loader.load();
                FormationCardController formCard = loader.getController();
                formCard.setFormation(formation);

                // Ajouter la carte de formation à menuGrid
                menuGridF.add(formationCardF, column, row);

                // Incrémenter les indices de colonne et de ligne
                column++;
                if (column == 4) {
                    column = 1;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize(){
        afficheCardF();
    }


    @FXML
    void RetourFront(ActionEvent event) {
        try {
            // Charger la vue Formation.fxml
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/FormationFront.fxml"));
            Scene scene =new Scene(loader.load());

            // Accéder au contrôleur de la vue Formation.fxml
            FormationFrontController formationFrontController = loader.getController();

            // Passer des données éventuelles au contrôleur de la vue Formation.fxml
            // Exemple : formationController.setSomeData(someData);

            // Accéder à la scène principale
            Stage stage = (Stage) retourF.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
            // Afficher la nouvelle scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement de la vue Formation.fxml
        }
    }


   /* @FXML
    private GridPane favorisGridPane;

    @FXML
    private AnchorPane menuForm;



    @FXML
    private ScrollPane menuScroll;
    private Label lblNoFavoris;
    private ServiceFormation serviceFormation;
    private Connection cnx;
    private ObservableList<Formation> cardListFormation = FXCollections.observableArrayList();


    public FavorisController() {

        serviceFormation = new ServiceFormation();
        cnx = DBConnection.getInstance().getCnx();
    }

    @FXML
    public void initialize() {
        afficherFormationsFavoris();
    }

    private void afficherFormationsFavoris() {
        try {
            // Récupérer les formations favoris depuis la base de données
            FavorisController favorisController=new FavorisController();
            List<Formation> formationsFavoris = favorisController.getFormationsFavoris();

            // Filtrer les formations qui ont le favoris à true
            List<Formation> formationsFavorisTrue = formationsFavoris.stream()
                    .filter(Formation::isFavoris)
                    .collect(Collectors.toList());

            // Afficher les formations favoris dans la grille
            int column = 0;
            int row = 0;
            for (Formation formation : formationsFavorisTrue) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/FormationCard.fxml"));
                AnchorPane formationCard = loader.load();
                FormationCardController formCard = loader.getController();
                formCard.setFormation(formation);

                favorisGridPane.add(formationCard, column++, row);
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

            // Afficher un message si aucune formation favoris n'est trouvée
            if (formationsFavorisTrue.isEmpty()) {
                lblNoFavoris.setVisible(true);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la récupération des formations favoris.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }





    public List<Formation> getFormationsFavoris() throws SQLException {
        List<Formation> formationsFavoris = new ArrayList<>();
        String sql = "SELECT * FROM formation WHERE favoris = 1";

        try{
            PreparedStatement prepare =cnx.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();
            Formation f;
            while (result.next()){
                f =new Formation(
                        result.getInt("id"),
                        result.getString("titre"),
                        result.getString("categorie"),
                        result.getString("tuteur"),
                        result.getString("updated"),
                result.getBoolean("favoris"));
                formationsFavoris.add(f);

            }
        }catch (Exception e){e.printStackTrace();}


        return formationsFavoris;
    }*/
}
