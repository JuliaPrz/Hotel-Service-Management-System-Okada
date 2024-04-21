package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_Connection {

	   // Database connection details
    public static final String DB_URL = "jdbc:mysql://localhost:3306/hsms_db";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "Password123";
    
    public Connection connection;
    public PreparedStatement prepare;
    public ResultSet result;
    public Statement statement;
     
  /*  // Constructor
    public DB_Connection() {
        // Establish the database connection when the DB_Conection object is created
        connect();
    } */

    // Method to establish the database connection
     protected Connection connect() {
        try {

        	Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        //  statement = connection.createStatement();
            System.out.println("Connected to the database");
            return connection;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database");
            e.printStackTrace();
           return null;
        } catch (ClassNotFoundException e) {
        	return null;
		}
    }
    

    // Method to close the database connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while closing the connection");
            e.printStackTrace();
        }
    }
}
