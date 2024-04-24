package com.example.pidev3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class FormationFrontController {

    @FXML
    private AnchorPane menuForm;

    @FXML
    private GridPane menuGrid;
    @FXML
    private Button consultFavoris;

    @FXML
    private ScrollPane menuScroll;
    @FXML
    private ComboBox<String> comboFiltre;
    private ServiceFormation serviceFormation;
    private Connection cnx;
    private ObservableList<Formation> cardListFormation = FXCollections.observableArrayList();


    public FormationFrontController() {

        serviceFormation = new ServiceFormation();
        cnx = DBConnection.getInstance().getCnx();
    }

    public ObservableList<Formation> menuGetData(){
        String sql = "SELECT * FROM formation";

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
                cardListFormation.add(f);

            }
        }catch (Exception e){e.printStackTrace();}


        return cardListFormation;
    }

   /* public void  afficheCard(){
        cardListFormation.clear();
        cardListFormation.addAll(menuGetData());
        int row =0;
        int column=0;
        menuGrid.getRowConstraints().clear();
        menuGrid.getColumnConstraints().clear();
        for(int q = 0 ; q < cardListFormation.size();q++){
            try{
                FXMLLoader load =new FXMLLoader();
                load.getClass().getResource("FormationCard.fxml");
                AnchorPane formationCard = load.load();
                FormationCardController formCard = load.getController();
                formCard.setFormation(cardListFormation.get(q));
                if (column == 3){
                    column = 0;
                    row+=1 ;
                }
                menuGrid.add(formationCard,column++,row);
            }catch (Exception e){e.printStackTrace();}
        }
    }*/
   public void afficheCard() {
       // Récupérer les données de formation
       ObservableList<Formation> formations = menuGetData();

       // Nettoyer le contenu actuel du menuGrid
       menuGrid.getChildren().clear();

       int row = 0;
       int column = 1;

       for (Formation formation : formations) {
           try {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/FormationCard.fxml"));
               AnchorPane formationCard = loader.load();
               FormationCardController formCard = loader.getController();
               formCard.setFormation(formation);

               // Ajouter la carte de formation à menuGrid
               menuGrid.add(formationCard, column, row);

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
        comboFiltre.setItems(getAllCategories());
       afficheCard();
    }


    @FXML
    void categorieSelectionnee(ActionEvent event) {
        String categorieSelectionnee = comboFiltre.getSelectionModel().getSelectedItem();
        if (categorieSelectionnee != null) {
            // Filtrer les formations en fonction de la catégorie sélectionnée
            ObservableList<Formation> formationsFiltrees = getFormationsByCategorie(categorieSelectionnee);
            // Afficher les formations filtrées dans la grille
            afficherFormations(formationsFiltrees);
        } else {
            // Si aucune catégorie n'est sélectionnée, afficher toutes les formations
            afficheCard();
        }
    }
    public ObservableList<String> getAllCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT categorie FROM formation";

        try {
            PreparedStatement prepare = cnx.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();
            while (result.next()) {
                categories.add(result.getString("categorie"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
    public ObservableList<Formation> getFormationsByCategorie(String categorie) {
        ObservableList<Formation> formationsFiltrees = FXCollections.observableArrayList();
        // Récupérer toutes les formations correspondant à la catégorie spécifiée
        String sql = "SELECT * FROM formation WHERE categorie = ?";
        try {
            PreparedStatement prepare = cnx.prepareStatement(sql);
            prepare.setString(1, categorie);
            ResultSet result = prepare.executeQuery();
            while (result.next()) {
                Formation f = new Formation(
                        result.getInt("id"),
                        result.getString("titre"),
                        result.getString("categorie"),
                        result.getString("tuteur"),
                        result.getString("updated"));
                formationsFiltrees.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formationsFiltrees;
    }
    public void afficherFormations(ObservableList<Formation> formations) {
        // Nettoyer le contenu actuel du menuGrid
        menuGrid.getChildren().clear();
        // Afficher les formations dans la grille
        int row = 0;
        int column = 1;
        for (Formation formation : formations) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/FormationCard.fxml"));
                AnchorPane formationCard = loader.load();
                FormationCardController formCard = loader.getController();
                formCard.setFormation(formation);
                // Ajouter la carte de formation à menuGrid
                menuGrid.add(formationCard, column, row);
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
    @FXML
    void ConsulterFavoris(ActionEvent event) {
        try {
            // Charger la vue Formation.fxml
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/Favoris.fxml"));
            Scene scene =new Scene(loader.load());

            // Accéder au contrôleur de la vue Formation.fxml
            FavorisController favorisController = loader.getController();

            // Passer des données éventuelles au contrôleur de la vue Formation.fxml
            // Exemple : formationController.setSomeData(someData);

            // Accéder à la scène principale
            Stage stage = (Stage) consultFavoris.getScene().getWindow();

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