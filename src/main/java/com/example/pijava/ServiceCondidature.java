package com.example.pijava;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCondidature implements CRUD<Condidature> {
    private Connection cnx;

    public ServiceCondidature() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Condidature condidature) throws SQLException {
        String req = "INSERT INTO condidature (name, prenom, email, num_tel, lettremotivation, cv, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, condidature.getName());
        ps.setString(2, condidature.getPrenom());
        ps.setString(3, condidature.getEmail());
        ps.setInt(4, condidature.getNum_tel());
        ps.setString(5, condidature.getLettredemotivation());
        ps.setString(6, condidature.getCv());
        ps.setInt(7,condidature.getProject_id());


        ps.executeUpdate();
        System.out.println("La condidature est ajoutée!");
    }

    @Override
    public void updateOne(Condidature condidature) throws SQLException {
        String sql = "UPDATE condidature SET name=?, prenom=?,  num_tel=?  WHERE email=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, condidature.getName());
            statement.setString(2, condidature.getPrenom());
            statement.setInt(3, condidature.getNum_tel());
            statement.setString(4, condidature.getEmail());
            //statement.setString(5, condidature.getLettredemotivation());
            //statement.setString(6, condidature.getCv());
            //statement.setInt(7,condidature.getId());// Assuming you want to update based on the title*/

            statement.executeUpdate();
        }
    }

    @Override
    public void deleteOne(Condidature condidature) throws SQLException {
        String req = "DELETE FROM `condidature` WHERE `email`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, condidature.getEmail()); // Assuming getId() returns the ID of the project
        ps.executeUpdate();
    }


    @Override
    public List<Condidature> selectAll() throws SQLException {
        List<Condidature> condidatureList = new ArrayList<>();

        String req = "SELECT * FROM `condidature`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Condidature condidature = new Condidature();
            condidature.setName(rs.getString("name"));
            condidature.setPrenom(rs.getString("prenom"));
            condidature.setEmail(rs.getString("email"));
            condidature.setNum_tel(rs.getInt("num_tel"));
            condidature.setLettredemotivation(rs.getString("lettremotivation"));
            condidature.setCv(rs.getString("cv"));
            condidature.setStatus(rs.getString("status"));
            //condidature.setId(rs.getInt("id"));

            condidatureList.add(condidature);
        }

        return condidatureList;
    }
}
