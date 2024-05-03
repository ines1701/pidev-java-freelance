package com.example.jessem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Contrat;

import java.io.InputStream;

public class ItemController {
    @FXML
    private Label desriptionLable;

    @FXML
    private ImageView img;

    @FXML
    private Label montantLable;

    @FXML
    private Label nomClientLabel;

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(contrat);
    }

    private Contrat contrat;
    private MyListener myListener;

    public void setData(Contrat contrat, MyListener myListener) {
        this.contrat = contrat;
        this.myListener = myListener;
        nomClientLabel.setText(contrat.getNom_client());
        desriptionLable.setText(contrat.getDescription());
        montantLable.setText(String.valueOf(contrat.getMontant()));

        String imagePath = "/images/" + contrat.getImage();
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            img.setImage(image);
        } else {
            System.out.println("L'image n'a pas pu être chargée : " + imagePath);
        }
    }


}