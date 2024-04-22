package com.example.test1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

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


    // Utiliser d'autres attributs comme critères de mise à jour
    public void updateOne(Event event) throws SQLException {
        String req = "UPDATE event SET describ=?, lieu=?, date=? WHERE titre=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, event.getDescrib());
        ps.setString(2, event.getLieu());
        ps.setTimestamp(3, event.getDate());
        ps.setString(4, event.getTitre()); // Utilisation du titre comme critère de recherche

        ps.executeUpdate();
    }





    public void deleteOne(Event event) throws SQLException {
        // Utilisez les attributs de l'événement pour construire la requête de suppression
        String query = "DELETE FROM event WHERE titre = ? AND describ = ? AND lieu = ? AND date = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1, event.getTitre());
        statement.setString(2, event.getDescrib());
        statement.setString(3, event.getLieu());
        statement.setTimestamp(4, event.getDate());
        statement.executeUpdate();
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

    public Event selectById(int eventId) throws SQLException {
        String query = "SELECT * FROM event WHERE id = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setInt(1, eventId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Event event = new Event();
            event.setId(resultSet.getInt("id"));
            event.setTitre(resultSet.getString("titre"));
            event.setDescrib(resultSet.getString("describ"));
            event.setLieu(resultSet.getString("lieu"));
            event.setDate(resultSet.getTimestamp("date"));
            return event;
        } else {
            // Gérer le cas où aucun événement avec l'ID spécifié n'est trouvé
            return null;
        }
    }


    public boolean existsByTitre(String titre) throws SQLException {
        // Vérifier si un événement avec le même titre existe dans la base de données
        // Vous pouvez utiliser une requête SQL pour effectuer cette vérification
        // Par exemple, vous pouvez utiliser une requête SELECT avec une clause WHERE pour vérifier l'existence de l'événement avec le titre donné
        String query = "SELECT COUNT(*) FROM event WHERE titre = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) { // Utilisation de cnx au lieu de connection
            statement.setString(1, titre);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }

}