package com.example.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvent implements CRUD<Event> {

    private Connection cnx ;

    public ServiceEvent() {
        cnx = DBConnection.getInstance().getCnx();
    }


    @Override
    public void insertOne(Event event) throws SQLException {
        String req = "INSERT INTO event (titre, describ, lieu, date) VALUES (?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(req);
        st.setString(1, event.getTitre());
        st.setString(2, event.getDescrib());
        st.setString(3, event.getLieu());
        // Assurez-vous que la date n'est pas nulle avant de l'ajouter à la requête
        if (event.getDate() != null) {
            st.setTimestamp(4, event.getDate());
        } else {
            st.setNull(4, Types.TIMESTAMP);
        }
        st.executeUpdate();
        System.out.println("L'événement est ajouté!");
    }


    @Override
    public void updateOne(Event event) throws SQLException {
        String req = "UPDATE `event` SET `titre`=?, `describ`=?, `lieu`=?, `date`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, event.getTitre());
        ps.setString(2, event.getDescrib());
        ps.setString(3, event.getLieu());
        ps.setTimestamp(4, event.getDate()); // Utilisez setTimestamp pour mettre à jour la date
        ps.setInt(5, event.getId()); // Utilisation de l'ID pour identifier l'événement à mettre à jour

        ps.executeUpdate();
    }


    @Override
    public void deleteOne(Event event) throws SQLException {
        String req = "DELETE FROM `event` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, event.getId());
        ps.executeUpdate();
    }

    @Override
    public ObservableList<Event> selectAll() throws SQLException {
        ObservableList<Event> eventsList = FXCollections.observableArrayList();

        String req = "SELECT titre, describ, lieu, date FROM event"; // Sélectionnez également la colonne de date
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Event e = new Event();
            e.setTitre(rs.getString("titre"));
            e.setDescrib(rs.getString("describ"));
            e.setLieu(rs.getString("lieu"));
            // Récupérez la date du résultat de la requête et l'ajoutez à l'objet Event
            e.setDate(rs.getTimestamp("date"));
            eventsList.add(e);
        }

        return eventsList;
    }
}
