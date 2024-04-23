package com.example.pidev3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class FormationCardController implements Initializable {

    @FXML
    private AnchorPane formCard;

    @FXML
    private Label formCteg;

    @FXML
    private ImageView formimage;

    @FXML
    private Label formTitre;

    @FXML
    private Label formTuteur;

    @FXML
    private Label formUpdate;
    private Image image;
    private Formation formation ;
    public void setFormation(Formation formation){
        this.formation=formation;
        formTitre.setText(formation.getTitre());
        formCteg.setText(formation.getCategorie());
        formTuteur.setText(formation.getTuteur());
        formUpdate.setText(formation.getUpdated());
      //formimage.setImage();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

