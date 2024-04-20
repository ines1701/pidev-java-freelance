package com.example.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceEventType implements CRUD<EventType>{

    private Connection cnx ;

    public ServiceEventType() {
        cnx = DBConnection.getInstance().getCnx();
    }



    @Override
    public void insertOne(EventType eventType) throws SQLException {


        String req = "INSERT INTO type_event (label) VALUES (?)";
        PreparedStatement st = cnx.prepareStatement(req);

        st.setString(1, eventType.getLabel());
        // Assurez-vous que la date n'est pas nulle avant de l'ajouter à la requête

        st.executeUpdate();
        System.out.println("Le type de L'événement est ajouté!");

    }

    @Override
    public void updateOne(EventType eventType) throws SQLException {



    }

    @Override
    public void deleteOne(EventType eventType) throws SQLException {

    }

    @Override
    public List<EventType> selectAll() throws SQLException {
        return null;
    }


}
