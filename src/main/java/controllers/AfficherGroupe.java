package controllers;

import entities.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import entities.Groupe;
import services.GroupeService;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherGroupe {
    @FXML
    private TextField searchTextField;

    @FXML
    private Button sortButton;
    private boolean isAscendingOrder = true;

    @FXML
    private ListView<Groupe> groupeListView;

    private GroupeService groupeService = new GroupeService();

    @FXML
    public void initialize() {
        afficherTousLesGroupes();

        groupeListView.setCellFactory(new Callback<ListView<Groupe>, ListCell<Groupe>>() {
            @Override
            public ListCell<Groupe> call(ListView<Groupe> param) {
                return new ListCell<Groupe>() {
                    @Override
                    protected void updateItem(Groupe item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.toString());
                            setContextMenu(createContextMenu(item));
                        }
                    }
                };
            }
        });

}
    private ContextMenu createContextMenu(Groupe groupe) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteMenuItem = new MenuItem("Supprimer");
        deleteMenuItem.setOnAction(event -> supprimerGroupe(groupe));
        contextMenu.getItems().add(deleteMenuItem);

        MenuItem updateMenuItem = new MenuItem("Mettre à jour le groupe");
        updateMenuItem.setOnAction(event -> {
            try {
                mettreAJourGroupe(groupe);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().add(updateMenuItem);

        // Ajouter un élément de menu pour ajouter un post



        MenuItem addPostMenuItem = new MenuItem("Ajouter un post");
        addPostMenuItem.setOnAction(event -> {
            try {
                ajouterPost(groupe);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().add(addPostMenuItem);

        return contextMenu;
    }
    @FXML
    private Button addgroupe;


//ajouter groupe



    @FXML
    void addgroupeon(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterGroupe.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) addgroupe.getScene().getWindow();

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// ajouter post avec la boutton droite


    private void ajouterPost(Groupe groupe) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPost.fxml"));
        Parent root = loader.load();
        AjouterPost controller = loader.getController();

        // Passer l'ID du groupe à la page d'ajout de post


        controller.initialize(groupe.getId());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }



    //mise a jour du groupe avec la boutton droite



    private void mettreAJourGroupe(Groupe groupe) throws IOException {
        Long groupeId = groupe.getId();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateGroupe.fxml"));
        Parent root = loader.load();
        UpdateGroupe controller = loader.getController();

        // Pre-fill the fields in the Update page with the values from the selected item
        controller.initialize(groupeId);
        controller.getNomTextField().setText(groupe.getNom());
        controller.getGroupeTextField().setText(groupe.getGroupe());
        controller.getDescriptionTextField().setText(groupe.getDescription());
        controller.getImageTextField().setText(groupe.getImage());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

// supprimer groupe button droite



    private void supprimerGroupe(Groupe groupe) {
        int id = groupe.getId().intValue();
        groupeService.delete(id);

        ObservableList<Groupe> groupesAffiches = groupeListView.getItems();

        groupesAffiches.remove(groupe);

        groupeListView.setItems(groupesAffiches);

        showAlert(Alert.AlertType.INFORMATION, "Groupe supprimé", "Le groupe a été supprimé avec succès.");
    }



// recherche avec condition (recherche avec condition )


    public void rechercher(ActionEvent actionEvent) {
        String keyword = searchTextField.getText().trim();
        List<Groupe> searchResults = groupeService.search(keyword);

        ObservableList<Groupe> observableSearchResults = FXCollections.observableArrayList(searchResults);
        groupeListView.setItems(observableSearchResults);

        // Afficher une notification si la recherche est réussie


        if (!searchResults.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Recherche réussie", "La recherche a abouti à des résultats.");
        }
    }

    // Méthode utilitaire pour afficher une boîte de dialogue


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void actualiser(ActionEvent actionEvent) {
        afficherTousLesGroupes();
    }



    // afficher la liste de groupe



    private void afficherTousLesGroupes() {
        List<Groupe> groupes = groupeService.getAll();
        ObservableList<Groupe> observableGroupes = FXCollections.observableArrayList(groupes);

        groupeListView.setItems(observableGroupes);
        groupeListView.setCellFactory(new Callback<ListView<Groupe>, ListCell<Groupe>>() {
            @Override
            public ListCell<Groupe> call(ListView<Groupe> param) {
                return new ListCell<Groupe>() {
                    @Override
                    protected void updateItem(Groupe item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.toString()); // Set the text to the result of toString()
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }



//  statistique



    @FXML
    private void showCategoryStatistics(ActionEvent event) {
        //  la liste des posts
        List<Groupe> ListView = groupeListView.getItems();

        // Count occurrences of each category
        Map<String, Long> categoryCounts = ListView.stream()
                .collect(Collectors.groupingBy(Groupe::getGroupe, Collectors.counting()));

        // Create a PieChart Data list
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Long> entry : categoryCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Create a PieChart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(" Groupe Statistics");

        // Create a new alert dialog with PieChart as the content
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("groupe Statistics");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(pieChart);
        alert.showAndWait();
    }



// filtrer avec l'ordre alphabetique



   /* @FXML
    private void sortAlphabetically(ActionEvent event) {
        ObservableList<Groupe> items = groupeListView.getItems();

        if (isAscendingOrder) {
            // Sort the data alphabetically in ascending order by the "groupe" attribute
            items.sort(Comparator.comparing(Groupe::getGroupe));
            // Change the button color to indicate ascending order
            sortButton.setStyle("-fx-background-color: #4682B4;");
        } else {
            // Sort the data alphabetically in descending order by the "groupe" attribute
            items.sort(Comparator.comparing(Groupe::getGroupe, String.CASE_INSENSITIVE_ORDER).reversed());
            // Change the button color to indicate descending order
            sortButton.setStyle("-fx-background-color: #FFA07A;");
        }

        // Toggle the sorting order for the next click
        isAscendingOrder = !isAscendingOrder;

        // Refresh the table view with the sorted data
        groupeListView.setItems(items);
    }*/


}
