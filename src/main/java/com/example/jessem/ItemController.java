package com.example.jessem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Contrat;

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

        Image image = new Image(getClass().getResource("/images/Banque-de-France-–-Particuliers-RIB.jpg").toString());
        img.setImage(image);
    }


}