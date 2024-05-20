package application.guest;

import java.sql.Connection;
import java.sql.SQLException;

import application.DB_Connection;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class BookingPageData extends DB_Connection{

	private int numberOfRoomType = 4; //there are 4 room types
	 
    public BookingPageData(Connection connection) {
        this.connection = connection;
    }

    // Method to display room page data
    public void displayRoomPageData( Rectangle[] targetNodes, Label[] nameLabel) {
    	
    	connection = connect();
        // Define queries
        String[] imageQueries = {
                "SELECT Image FROM room_type WHERE type_ID = 10",
                "SELECT Image FROM room_type WHERE type_ID = 20",
                "SELECT Image FROM room_type WHERE type_ID = 30",
                "SELECT Image FROM room_type WHERE type_ID = 40"
        };
        
        // Display images
        for (int i = 0; i < numberOfRoomType; i++) {
            book_displayImage(connection, imageQueries[i], targetNodes[i]);
        }

     // Display text
        book_displayName(connection, nameLabel);
     
        close();
    }

	    // BOOKING PAGE - method to display IMAGE
    private void book_displayImage(Connection connection, String query, Rectangle targetNode) {
        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            if (result.next()) {
                String imagePath = result.getString("Image");
                if (imagePath != null) {
                    ImagePattern pattern = new ImagePattern(new javafx.scene.image.Image("file:" + imagePath));
                    targetNode.setFill(pattern);
                    targetNode.setEffect(new DropShadow(2, javafx.scene.paint.Color.BLACK)); // Shadow
                    targetNode.setArcWidth(25.0);   // Corner radius
                    targetNode.setArcHeight(25.0);
                } else {
                    System.out.println("No image file path found for the given ID.");
                }
            } else {
                System.out.println("No data found for the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
    }
	    
	    // BOOKING PAGE - method to display NAME
	    void book_displayName(Connection connection, Label[] nameLabel) {	
	        String[] nameQueries = {
	            "SELECT Name FROM room_type WHERE type_ID = 10",
	            "SELECT Name FROM room_type WHERE type_ID = 20",
	            "SELECT Name FROM room_type WHERE type_ID = 30",
	            "SELECT Name FROM room_type WHERE type_ID = 40"
	        };
	
	        // Display text for labels
	        for (int i = 0; i < numberOfRoomType; i++) {
	            try {
	                prepare = connection.prepareStatement(nameQueries[i]);
	                result = prepare.executeQuery();
	                
	                if (result.next()) {
	                    String name = result.getString("Name");
	                    nameLabel[i].setText(name);
	                } else {
	                    System.out.println("Name: No data found for the given ID.");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            } finally {
	            	// since I'm reusing the connection, it is a good practice to close the resultset and preparedstatement
	                try {
	                    if (result != null) result.close();
	                    if (prepare != null) prepare.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	             }
	        } // end of for loop
	   }
}
