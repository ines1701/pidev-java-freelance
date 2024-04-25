package com.example.pijava;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardProjectController {

    @FXML
    private Label bCard;

    @FXML
    private Label cCard;

    @FXML
    private Label dCard;

    @FXML
    private Label pCard;

    @FXML
    private Label pnCard;

    @FXML
    private Label prdCard;
    private Project project;

    public void setProject(Project project){
        this.project= project;
        pnCard.setText(project.getTitre());
        cCard.setText(project.getCategorie());
        prdCard.setText(project.getPeriode());
        pCard.setText(project.getPortee());
        bCard.setText(String.valueOf(project.getBudget()));
        dCard.setText(project.getDescription());
    }

}
