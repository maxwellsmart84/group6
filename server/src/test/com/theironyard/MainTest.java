package com.theironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by DrScott on 11/5/15.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        Main.createTables(conn);
        return conn;
    }

    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE users");
        stmt.execute("DROP TABLE buckets");
        conn.close();
    }
    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "Cooper", "aCooper@gmail.com", "password");
        User user = Main.selectUser(conn, "Alice");
        endConnection(conn);
        assertTrue(user != null);
    }

    @Test
    public void testBucket() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "Cooper", "aCooper@gmail.com", "password");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest.");
        Bucket bucket = Main.selectBucket(conn, 1);
        endConnection(conn);
        assertTrue(bucket != null);
    }

    @Test
    public void testBuckets() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "Cooper", "aCooper@gmail.com", "password");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest.");
        Main.insertBucket(conn, 1, "Something.");
       Main.insertUser(conn, "Bob", "Pearce", "BobbyG@gmail.com", "password");
       Main.insertBucket(conn, 2, "I want to climb Mt. Everest too.");
        ArrayList<Bucket> buckets = Main.selectAllBuckets(conn);
        endConnection(conn);
        assertTrue(buckets!= null);

    }

   @Test
    public void testSelectUserBuckets() throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "Cooper", "aCooper@gmail.com", "password");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest a second time.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Kilimanjaro.");
        Main.insertUser(conn, "bob", "pearce", "bp@gmail", "passwerd");
        Main.insertBucket(conn, 2, "I'm bob");
        ArrayList<Bucket> userBuckets = Main.selectUserBuckets(conn, 1);
        endConnection(conn);
        assertTrue(userBuckets!=null);
    }

    @Test
    public void testRandomBucket() throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "Cooper", "aCooper@gmail.com", "password");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest a second time.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Kilimanjaro.");
        Main.insertUser(conn, "bob", "pearce", "bp@gmail", "passwerd");
        Main.insertBucket(conn, 2, "I'm bob");
        Bucket bucket = Main.selectRandomBucket(conn);
        endConnection(conn);
        assertTrue(bucket != null);
    }


 /*  @Test
   public void testDeleteUser() throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "Cooper", "aCooper@gmail.com", "password");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest a second time.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Kilimanjaro.");
        Main.insertUser(conn, "bob", "pearce", "bp@gmail", "passwerd");
        Main.insertBucket(conn, 2, "I'm bob");
        Main.deleteUser(conn, 1);
        endConnection(conn);
        assertTrue(Main.selectUser(conn, "aCooper@gmail.com")==null);
    }

    @Test
    public void testDeleteBucket() throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "Cooper", "aCooper@gmail.com", "password");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Everest a second time.");
        Main.insertBucket(conn, 1, "I want to climb Mt. Kilimanjaro.");
        Main.insertUser(conn, "bob", "pearce", "bp@gmail", "passwerd");
        Main.insertBucket(conn, 2, "I'm bob");
        Main.removeBucket(conn, 1);
        endConnection(conn);
        assertTrue(Main.selectBucket(conn, 1)==null);
    }
*/


}