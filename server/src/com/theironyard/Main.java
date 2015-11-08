package com.theironyard;

import jodd.json.JsonSerializer;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    // create tables
    public static void createTables (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS buckets (id IDENTITY, userId INT, text VARCHAR, isDone BOOLEAN)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, firstName VARCHAR, lastName VARCHAR, email VARCHAR, " +
                "password VARCHAR)");
    }

    //create new user for /signUp
    public static void insertUser(Connection conn, String firstName, String lastName, String email, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?,?,?,?)");
        stmt.setString(1, firstName );
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.setString(4, password);
        stmt.execute();
    }
    public static void editUser(Connection conn, int id, String firstName, String lastName, String email, String password) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET firstName = ?, lastName = ?, email = ?, password = ?" +
                "  WHERE id = ?");
        stmt.setInt(5, id);
        stmt.setString(1, firstName );
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.setString(4, password);


    }

    //select 1 user info
    public static User selectUser(Connection conn, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
        stmt.setString( 1 , email);
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

    //remove user (just in case)
    public static void deleteUser(Connection conn, int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    //adds row for new bucket in buckets table
    static void insertBucket(Connection conn, int id, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO buckets VALUES (NULL, ? , ?, false)"); //causes identity to auto incremnent
        stmt.setInt(1, id);
        stmt.setString(2, text);
        stmt.execute();
    }

    //pulls a bucket from buckets made by user
    public static Bucket selectBucket (Connection conn, int id) throws SQLException{
        Bucket bucket = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM buckets INNER JOIN users ON " +
                        "buckets.userId = users.id WHERE buckets.userId = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()){
            bucket = new Bucket();
            bucket.id = results.getInt("buckets.id");
            bucket.text = results.getString("buckets.text");
            bucket.isDone= results.getBoolean("buckets.isDone");
        }
        return bucket;
    }

    //select random bucket
    public static Bucket selectRandomBucket (Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        Bucket bucket = null;

       ResultSet results = stmt.executeQuery("SELECT * FROM buckets ORDER BY RAND() LIMIT 1");
        if (results.next()){
            bucket = new Bucket();
            bucket.isDone = false;
            bucket.id = results.getInt("id");
            bucket.text = results.getString("text");
        }
        return bucket;
    }

    //select all buckets for globalBucket
    static ArrayList<Bucket> selectAllBuckets(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM buckets");
        ArrayList<Bucket> buckets = new ArrayList();
        while (results.next()){
            int id = results.getInt("id");
            String text = results.getString("text");
            Boolean isDone = results.getBoolean("isDone");
            Bucket bucket = new Bucket(id, text, isDone);
            buckets.add(bucket);
        }
        return buckets;
    }
    public static void setDone(Connection conn, int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("UPDATE buckets SET isDone = TRUE WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    //pulls all buckets posted by a specific user
    static ArrayList<Bucket> selectUserBuckets(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM buckets WHERE userId = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        //stmt.setInt
        ArrayList<Bucket> buckets = new ArrayList();
        while (results.next()){
            int idNum = results.getInt("id");
            String text = results.getString("text");
            Boolean isDone = results.getBoolean("isDone");
            Bucket bucket = new Bucket(idNum, text, isDone);
            buckets.add(bucket);
        }
        return buckets;
    }

    //removes bucket from table buckets
    public static void removeBucket (Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM buckets WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws SQLException {
        String doug = "doug";
        String test = "test;";
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.externalStaticFileLocation("client");
        Spark.init();
        //testing stuffs

        insertUser(conn, "Doug", "Scott", "dougscott2@gmail.com", "password");
        insertBucket(conn, 1, "I want to see the world-DS");
        insertBucket(conn, 1, "I will climb Mt. Kilimanjaro.-DS");
        insertUser(conn, "Bruce", "Willis", "bruce.willis@gmail.com", "passphrase");
        insertBucket(conn, 2, "I will hang out with Doug one day. -BW ");
        insertUser(conn, "Erik", "Schneider", "eSchnei@gmail.com", "passcode");
        insertBucket(conn, 2, "Get a better car.-ES");
        insertUser(conn, "Pat", "Sajack", "psaj@gmail.com", "patsaj123");
        insertBucket(conn, 3, "Host another TV show.!-PS");




        Spark.post(
                "/login",
                ((request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");
                    if (username.isEmpty() || password.isEmpty()) {
                        Spark.halt(403);
                    }
                    User user = selectUser(conn, username);
                    if (!password.equals(user.password)) {
                        Spark.halt(403);
                    }
                    Session session = request.session();
                    session.attribute("username", username);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

        Spark.get(
                "/getUser",
                ((request, response) -> {
                    String email = request.queryParams("email");
                    try {
                        JsonSerializer serializer = new JsonSerializer();
                        String json = serializer.serialize(selectUser(conn, email));
                        return json;
                    } catch (Exception e) {
                    }
                    return "";
                })
        );
        Spark.post(
                "/editUser",
                ((request2, response2) -> {
                    String email = request2.queryParams("email");
                   String firstName = request2.queryParams("firstName");
                    String lastName = request2.queryParams("lastName");
                    String password = request2.queryParams("password");
                    String id = request2.queryParams("id");
                    try {
                    int idNum = Integer.valueOf(id);
                        editUser(conn, idNum, firstName, lastName, email, password);
                    } catch (Exception e) {
                    }
                    return "";
                })
        );
        Spark.post(
                "/isDone",
                ((request2, response2) -> {
                    String id = request2.queryParams("id");
                    try {
                    int idNum = Integer.valueOf(id);
                        setDone(conn, idNum);
                    } catch (Exception e) {
                    }
                    return "";
                })
        );
         Spark.post(
                 "/insertBucket",
                 ((request, response) -> {
                     String id = request.queryParams("id");
                     String text = request.queryParams("text");
                     try {
                         int idNum = Integer.valueOf(id);
                         insertBucket(conn, idNum, text);
                     } catch (Exception e) {
                     }
                     response.redirect("/");
                     return "";
                 })
         );

        Spark.get(
                "/globalBucket",
                ((request, response) -> {
                        ArrayList<Bucket> buckets = selectAllBuckets(conn);
                      JsonSerializer serializer = new JsonSerializer();
                        String json = serializer.serialize(buckets);
                        return json;
                })
        );

        Spark.get(
                "/userBucket",
                ((request, response) -> {
                    String id = request.queryParams("id");

                    try {
                        int idNum = Integer.valueOf(id);
                        ArrayList<Bucket> buckets = selectUserBuckets(conn, idNum);
                        JsonSerializer serializer = new JsonSerializer();
                        String json = serializer.serialize(buckets);
                        return json;
                    } catch (Exception e) {
                    }
                    return "";
                })
        );

        Spark.get(
                "/removeBucket",
                ((request1, response1) -> {
                    String id = request1.queryParams("id");
                    try {
                    int idNum = Integer.valueOf(id);
                        removeBucket(conn, idNum);
                    } catch (Exception e) {
                    }
                    return "";
                })
        );
        Spark.get(
                "/randomBucket",
                ((request1, response1) -> {
                    JsonSerializer serializer = new JsonSerializer();
                    Bucket bucket = selectRandomBucket(conn);
                        String json = serializer.serialize(bucket);
                        return json;
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
