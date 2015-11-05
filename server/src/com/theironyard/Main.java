package com.theironyard;

import spark.Spark;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void createTables (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
       // stmt.execute("CREATE TABLE IF NOT EXISTS buckets (id IDENTITY, text VARCHAR, is_done BOOLEAN)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, firstName VARCHAR, lastName VARCHAR, email VARCHAR, " +
                "password VARCHAR)");
    }

    public static void insertUser(Connection conn, String firstName, String lastName, String email, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?,?,?,?)");
        stmt.setString(1, firstName );
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.setString(4, password);
        stmt.execute();
    }

    public static User selectUser(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
        stmt.setInt( 1 , id);
        User user = new User();
        ResultSet results = stmt.executeQuery();
        if (results.next()){
            user.firstName = results.getString("firstName");
            user.lastName = results.getString("lastName");
            user.email = results.getString("email");
            user.password = results.getString("password");
            user.id = results.getInt("id");
        }
        return user;
    }
  /*  public static User selectUser(Connection conn) throws SQLException {
        return selectUser(conn, 0);
    }
    public static ArrayList<User> selectUsers (Connection conn) {
        ArrayList<User> users = new ArrayList();

        return  users;
    } */






/////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();

        //test to fill table
       /* insertUser(conn, "doug", "scott", "douscott2", "1234");
        insertUser(conn, "frank", "frank's last name", "franke email", "12345");
        insertUser(conn, "max", "to the", "email", "123455");*/


           Spark.post(
                         "/signUp",
                         ((request, response) -> {
                             String firstName = request.queryParams("firstName");
                             String lastName = request.queryParams("lastName");
                             String email = request.queryParams("email");
                             String password = request.queryParams("password");
                             String id = request.queryParams("id");
                             try {
                              int idNum = Integer.valueOf(id);
                                 //User user = new User(idNum, firstName, lastName, email, password);
                                 insertUser(conn, firstName, lastName, email, password);
                                 //selectUser(conn, idNum);
                             } catch (Exception e) {
                             }
                              //response.redirect("/");
                             return "";
                         })
                 );














    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
