package controllers;

import controllers.ModifierPost;
import entities.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.PostService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherPost {


    @FXML
    private Button goback;

    @FXML
    private ListView<Post> postListView;

    @FXML
    private TextField searchTextField;

    private PostService postService = new PostService();

    @FXML
    public void initialize() {
        afficherTousLesPosts();
        configurerMenuContextuel();
    }

    private void afficherTousLesPosts() {
        List<Post> postList = postService.getAll();
        ObservableList<Post> observablePostList = FXCollections.observableArrayList(postList);
        postListView.setItems(observablePostList);
    }

    private void configurerMenuContextuel() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Supprimer");
        deleteItem.setOnAction(event -> supprimerPostSelectionne());
        contextMenu.getItems().add(deleteItem);

        MenuItem editItem = new MenuItem("Modifier");
        editItem.setOnAction(event -> modifierPostSelectionne());
        contextMenu.getItems().add(editItem);

        postListView.setContextMenu(contextMenu);
    }

    private void supprimerPostSelectionne() {
        Post postSelectionne = postListView.getSelectionModel().getSelectedItem();
        if (postSelectionne != null) {
            postService.delete(Math.toIntExact(postSelectionne.getId()));
            postListView.getItems().remove(postSelectionne);
        }
    }

    private void modifierPostSelectionne() {
        Post postSelectionne = postListView.getSelectionModel().getSelectedItem();
        if (postSelectionne != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPost.fxml"));
                Parent root = loader.load();
                ModifierPost controller = loader.getController();
                controller.initData(postSelectionne.getId());
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public void rechercherPosts(ActionEvent actionEvent) {
        String nom = searchTextField.getText().trim();
        if (!nom.isEmpty()) {
            List<Post> searchResult = postService.searchByNom(nom);
            ObservableList<Post> observableSearchResult = FXCollections.observableArrayList(searchResult);
            postListView.setItems(observableSearchResult);
        } else {
            // Si le champ de recherche est vide, afficher tous les posts
            afficherTousLesPosts();
        }
    }

    @FXML
    void goback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPost.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) goback.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* @FXML
    void exportToPDF(ActionEvent event) {
        String filePath = "C:\\Users\\asus\\Desktop\\Projetjava\\src\\main\\resources\\postsData.pdf";

        try {
            ExportPDF exportPDF = new ExportPDF();
            exportPDF.generatePDF(filePath, postListView); // Pass postListView directly
            System.out.println("Data exported to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

}
