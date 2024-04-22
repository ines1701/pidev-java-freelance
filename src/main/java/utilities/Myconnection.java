/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Myconnection {
    private String url = "jdbc:mysql://localhost:3306/pidevsymfonyy";
    private String login = "root";
    private String password ="";

    Connection cnx;
    private static Myconnection instance;

    private Myconnection(){
        try {
            cnx = DriverManager.getConnection(url, login, password);
            System.out.println("Connected to DB ! ");
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Connection getCnx(){
        return cnx ;
    }
    public static Myconnection getInstance() {

        if (instance == null){
            instance = new Myconnection();
        }
        return instance;
    }




}

