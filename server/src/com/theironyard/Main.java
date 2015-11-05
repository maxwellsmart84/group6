package com.theironyard;

import jodd.json.JsonSerializer;
import spark.Spark;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void createTables (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS buckets (id IDENTITY, text VARCHAR, is_done BOOLEAN)");
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
    public static User selectUser(Connection conn) throws SQLException {
        return selectUser(conn, 0);
    }

    /*
    public static Game selectGame (Connection conn, int id) throws SQLException{
        Game game = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM games INNER JOIN users ON  games.user_id = " +
                "users.id WHERE games.id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()){
            game = new Game();
            game.id = results.getInt("games.id");
            game.title = results.getString("games.title");
            game.username = results.getString("users.name");
            game.system = results.getString("games.system");
        }
        return game;
    }
     */

    static void insertBucket(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO buckets VALUES (NULL, ?, false)"); //causes identity to auto incremnent
        stmt.setString(1, text);
        stmt.execute();
    }


    static ArrayList<Bucket> selectBuckets(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM buckets");
        ArrayList<Bucket> buckets = new ArrayList();
        while (results.next()){
            int id = results.getInt("id");
            String text = results.getString("text");
            Boolean isDone = results.getBoolean("is_done");
            Bucket listItem = new Bucket(id, text, isDone);
            buckets.add(listItem);
        }
        return buckets;
    }











/////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();




        Spark.get(
                "/getUser",
                ((request, response) -> {
                    String id = request.queryParams("id");
                    try {
                        int idNum = Integer.valueOf(id);
                        JsonSerializer serializer = new JsonSerializer();
                        String json = serializer.serialize(selectUser(conn, idNum));
                        return json;
                    } catch (Exception e) {
                    }
                    return "";
                })
        );

        Spark.get(
                "/globalBucket",
                ((request, response) -> {
                    try {
                      JsonSerializer serializer = new JsonSerializer();
                        String json = serializer.serialize(selectBuckets(conn));
                        return json;
                    } catch (Exception e) {
                    }
                    return "";
                })
        );


        Spark.post(
                "/signUp",
                ((request, response) -> {
                    String firstName = request.queryParams("firstName");
                    String lastName = request.queryParams("lastName");
                    String email = request.queryParams("email");
                    String password = request.queryParams("password");
                    //String id = request.queryParams("id");
                    try {
                       // int idNum = Integer.valueOf(id);
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
