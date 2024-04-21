package com.example.pijava;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProject implements CRUD<Project> {
    private Connection cnx ;

    public ServiceProject() {
        cnx = DBConnection.getInstance().getCnx();
    }
    @Override
    public void insertOne(Project project) throws SQLException {
        String req = "INSERT INTO project(titre, categorie, periode, portee, description, budget) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, project.getTitre());
        ps.setString(2, project.getCategorie());
        ps.setString(3, project.getPeriode());
        ps.setString(4, project.getPortee());
        ps.setString(5, project.getDescription());
        ps.setDouble(6, project.getBudget());


        ps.executeUpdate();
        System.out.println("Le projet est ajouté!");
    }

    @Override
    public void updateOne(Project project) throws SQLException {
        String sql = "UPDATE project SET categorie=?, periode=?, portee=?, description=?, budget=? WHERE titre=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, project.getCategorie());
            statement.setString(2, project.getPeriode());
            statement.setString(3, project.getPortee());
            statement.setString(4, project.getDescription());
            statement.setDouble(5, project.getBudget());
            statement.setString(6, project.getTitre());
            /*statement.setInt(7,project.getId());// Assuming you want to update based on the title*/

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Project not found, update failed.");
            } else {
                System.out.println("Project updated successfully.");
            }
        }
}


    @Override
    public void deleteOne(Project project) throws SQLException {
        String req = "DELETE FROM `project` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, project.getId()); // Assuming getId() returns the ID of the project
        ps.executeUpdate();
    }


    @Override
    public List<Project> selectAll() throws SQLException {
        List<Project> projectList = new ArrayList<>();

        String req = "SELECT * FROM `project`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Project project = new Project();
            project.setId(rs.getInt("id"));
            project.setTitre(rs.getString("titre"));
            project.setCategorie(rs.getString("categorie"));
            project.setPeriode(rs.getString("periode"));
            project.setPortee(rs.getString("portee"));
            project.setDescription(rs.getString("description"));
            project.setBudget(rs.getDouble("budget"));

            projectList.add(project);
        }

        return projectList;
    }

    public boolean existsWithSameTitleAndCategory(String titre, String categorie) throws SQLException {
        String query = "SELECT COUNT(*) FROM project WHERE titre = ? AND categorie = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, titre);
            statement.setString(2, categorie);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    }

