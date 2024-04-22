package com.example.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceEventType implements CRUD<EventType>{


    private Connection cnx;

    // Constructeur par défaut
    public ServiceEventType() {
        // Initialisation de la connexion à la base de données
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

        String req = "DELETE FROM type_event WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(req)) {
            st.setInt(1, eventType.getId());
            st.executeUpdate();
            System.out.println("Le type d'événement a été supprimé avec succès !");
        }

    }

    @Override
    public List<EventType> selectAll() throws SQLException {
        List<EventType> eventTypes = new ArrayList<>();
        String req = "SELECT * FROM type_event";
        try (PreparedStatement st = cnx.prepareStatement(req); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                EventType eventType = new EventType();
                eventType.setId(rs.getInt("id"));
                eventType.setLabel(rs.getString("label"));
                eventTypes.add(eventType);
            }
        }
        return eventTypes;
    }

    public List<String> getAllTypes() throws SQLException {
        List<String> types = new ArrayList<>();
        String query = "SELECT label FROM type_event";

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                types.add(resultSet.getString("label"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return types;
    }






}