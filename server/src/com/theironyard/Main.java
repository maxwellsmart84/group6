package com.theironyard;

import jodd.json.JsonSerializer;
import spark.Session;
import spark.Spark;

import java.beans.*;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

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
        stmt.setString(1, email);
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

    public static ArrayList<User> selectAllUsers(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        ArrayList<User> users = new ArrayList<>();

        ResultSet results = stmt.executeQuery("SELECT * FROM users");
        while (results.next()){
            User user = new User();
            user.firstName = results.getString("firstName");
            user.lastName = results.getString("lastName");
            user.email = results.getString("email");
            user.password = results.getString("password");
            user.id = results.getInt("id");
            users.add(user);
        }
        return users;

    }

    //remove user (just in case)
    public static void deleteUser(Connection conn, int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    //adds row for new bucket in buckets table
    static void insertBucket(Connection conn, int id, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO buckets VALUES (NULL, ? , ?, ?)"); //causes identity to auto incremnent
        Bucket b = new Bucket();
        stmt.setInt(1, id);
        stmt.setString(2, text);
        stmt.setBoolean(3, b.isDone);
        stmt.execute();
    }
    static void insertUserlessBucket (Connection conn, String text) throws SQLException{
        Random random = new Random();
        int randInt = 100000 + (int)(Math.random()* 100000);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO buckets VALUES (NULL, ?, ?, false)");
        stmt.setInt(1, randInt);
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
    public static ArrayList<Bucket> selectRandomBucket(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        Bucket bucket = null;
        ArrayList<Bucket> buckets = new ArrayList();

       ResultSet results = stmt.executeQuery("SELECT * FROM buckets ORDER BY RAND() LIMIT 1");
        if (results.next()){
            bucket = new Bucket();
            bucket.isDone = false;
            bucket.id = results.getInt("id");
            bucket.text = results.getString("text");
            buckets.add(bucket);
        }
        return buckets;
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
        Bucket bucket = selectBucket(conn, id);
        if (!bucket.isDone) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE buckets SET isDone = true WHERE id = ?");
            stmt.setInt(1, id);
            stmt.execute();
        }  PreparedStatement stmt = conn.prepareStatement("UPDATE buckets SET isDone = false WHERE id =?");
        stmt.setInt(1,id);
        stmt.execute();
        //PreparedStatement stmt2 = conn.prepareStatement("UPDATE buckets SET")
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
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.externalStaticFileLocation("client");
        Spark.init();
        //testing stuffs

        if (selectAllBuckets(conn).size() == 0) {
            insertUser(conn, "Doug", "Scott", "dougscott2@gmail.com", "password");
            insertBucket(conn, 1, "I will climb Mt. Kilimanjaro.");
            insertBucket(conn, 1, "I want to hang out with Bruce Willis.");
            insertUser(conn, "Bruce", "Willis", "bruce.willis@gmail.com", "passphrase");
            insertBucket(conn, 2, "I will hang out with Doug one day.");
            insertUser(conn, "Erik", "Schneider", "eSchnei@gmail.com", "passcode");
            insertBucket(conn, 2, "Get a better car.");
            insertUser(conn, "Pat", "Sajack", "psaj@gmail.com", "patsaj123");
            insertBucket(conn, 3, "Host a TV show.");
            insertUserlessBucket(conn, "Meet Harrison Ford");
            insertUserlessBucket(conn, "Spend a week in Fiji");
            insertUserlessBucket(conn, "Go skydiving");
            insertUserlessBucket(conn, "Visit the Vatican");
            insertUserlessBucket(conn, "Eat an entire cow");
            insertUserlessBucket(conn, "Party with Keith Richards");
            insertUserlessBucket(conn, "Learn to ride a horse.");
            insertUserlessBucket(conn, "Punch Mike Meyers");
            insertUserlessBucket(conn, "Pants Donald Trump");
            insertUserlessBucket(conn, "Become a pokemon master");
            insertUserlessBucket(conn, "Eat lunch with Jason Alexander");
            insertUserlessBucket(conn, "Fight Sylvester Stallone");
            insertUserlessBucket(conn, "Learn Java");
            insertUserlessBucket(conn, "Learn HTML");
            insertUserlessBucket(conn, "Learn CSS");
            insertUserlessBucket(conn, "Learn Javascript");
            insertUserlessBucket(conn, "Learn to program");
            insertUserlessBucket(conn, "Go bungee jumping");
            insertUserlessBucket(conn, "Buy Bill Murray a beer");
            insertUserlessBucket(conn, "Learn to fly");
            insertUserlessBucket(conn, "Give Justin Bieber a wedgie");
            insertUserlessBucket(conn, "Take a karate lesson from Steven Segal");
            insertUserlessBucket(conn, "Drink Pappy van Winkle");
            }


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
                    //selectUser(conn, "username");
                    Session session = request.session();
                    session.attribute("username", username);
                    //response.redirect("/userPage.html");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
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

        Spark.get (
                "/getUsers",
                ((request3, response3) -> {
                    JsonSerializer serializer = new JsonSerializer();
                    String json = serializer.serialize(selectAllUsers(conn));
                    return json;
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
                    //Session session = request2.session();
                    //String bucketText = request2.queryParams("bucketText");
                    String id = request2.queryParams("id");
                    try {
                    int idNum = Integer.valueOf(id);
                        setDone(conn, idNum);
                    } catch (Exception e) {

                    }
                    //String id = request2.queryParams("id");
                    //int idNum = selectUser(conn, username).id;
                    //Bucket bucket = selectBucket(conn, idNum);
                    //setDone(conn, bucketText);
                    return "";
                })
        );

        Spark.post(
                "/insertUserlessBucket",
                ((request2, response2) -> {
                    String text = request2.queryParams("newTitle");
                    insertUserlessBucket(conn, text);
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
                    Session session = request.session();
                    String username = session.attribute("username");
                    User user = selectUser(conn, username);
                    try {
                        ArrayList<Bucket> buckets = selectUserBuckets(conn, user.id);
                        JsonSerializer serializer = new JsonSerializer();
                        String json = serializer.serialize(buckets);
                        return json;
                    } catch (Exception e) {
                    }
                    return "";
                })
        );

        Spark.post(
                "/insertBucket",
                ((request, response) ->{
                    Session session = request.session();
                    String username = session.attribute("username");
                    User user = selectUser(conn, username);
                    String text = request.queryParams("newTitle");
                    int idNum = user.id;
                    insertBucket(conn, idNum, text);
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
                    ArrayList<Bucket> buckets = selectRandomBucket(conn);
                        String json = serializer.serialize(buckets);
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
                    return "";
                })
        );

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
