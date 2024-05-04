package application.guest;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;

import application.DB_Connection;

// ROOM PAGE - DISPLAY DATA
public class RoomTypeData extends DB_Connection{

	    private int numberOfRoomType = 4; //there are 4 room types
	 

	    public RoomTypeData(Connection connection) {
	        this.connection = connection;
	    }

	    // Method to display room page data
	    public void displayRoomPageData( Rectangle[] targetNodes, Label[] nameLabel, Label[] priceLabel, Label[] bedLabel,
	                                     Label[] descriptionLabel, Label[] amenLabel) {
	    	
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
	            roomType_displayImage(connection, imageQueries[i], targetNodes[i]);
	        }

	     // Display text
	        roomType_displayName(connection, nameLabel);
	        roomType_displayPrice(connection, priceLabel);
	        roomType_displayBedType(connection, bedLabel);
	        roomType_displayDescription(connection, descriptionLabel);
	        roomType_displayAmenities(connection, amenLabel);
	        close();
	    }

	    // Method to display image for room type
	    
	    private void roomType_displayImage(Connection connection, String query, Rectangle targetNode) {
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
	        } finally {
	            // Close resources
	           // close();
	        }
	    }

	 // ROOM PAGE - method to display NAME
	    void roomType_displayName(Connection connection, Label[] nameLabel) {	
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
	  
	    // ROOM PAGE - method to display PRICE
	    void roomType_displayPrice(Connection connection, Label[] priceLabel) {
	    	 String [] priceQueries = {
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 10",
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 20",
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 30",
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 40"
			        };
		            
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomType; i++) {
		            try {
		              
		                prepare = connection.prepareStatement(priceQueries[i]);
			            result = prepare.executeQuery();

		                if (result.next()) {	                 
		                	// Retrieve the price from the result set
		                    double price = result.getDouble("Price_per_night");

		                    // Format the price with commas and no decimals
		                    DecimalFormat formatter = new DecimalFormat("#,###");
		                    String formattedPrice = formatter.format(price);

		                    // Set the formatted price text to the label
		                    priceLabel[i].setText(formattedPrice + " PHP");
   
		                } else {
		                    System.out.println("No data found for the given ID.");
		                }
		            } catch (SQLException e) {
		                e.printStackTrace();
		            } finally {
		                try {
		                    if (result != null) result.close();
		                    if (prepare != null) prepare.close();
		                } catch (SQLException e) {
		                    e.printStackTrace();
		                }
		             }
		        } // end of for loop
		   }

	 // Method to retrieve and display the BED TYPE from the database using labels
	    void roomType_displayBedType(Connection connection, Label[] bedLabel) {

		    	 String [] bedTypeQueries = {
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 10",
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 20",
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 30",
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 40"
		 	        };
		            
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomType; i++) {
		            try {
		              
		                prepare = connection.prepareStatement(bedTypeQueries[i]);
			            result = prepare.executeQuery();

		                if (result.next()) {	                 
		                    String bedType = result.getString("Bed_Type");
		                    bedLabel[i].setText(bedType);
   
		                } else {
		                    System.out.println("Bed Type: No data found for the given ID.");
		                }
		            } catch (SQLException e) {
		                e.printStackTrace();
		            } finally {
		                try {
		                    if (result != null) result.close();
		                    if (prepare != null) prepare.close();
		                } catch (SQLException e) {
		                    e.printStackTrace();
		                }
		             }
		        } // end of for loop
		   }
    
	  
	    // ROOM PAGE - method to display DESCRIPTION
	    void roomType_displayDescription(Connection connection, Label[] descriptionLabel) {	

	    	 String [] descQueries = {
			        	"SELECT Description FROM room_type WHERE type_ID = 10",
			        	"SELECT Description FROM room_type WHERE type_ID = 20",
			        	"SELECT Description FROM room_type WHERE type_ID = 30",
			        	"SELECT Description FROM room_type WHERE type_ID = 40"
			        };
		        
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomType; i++) {
		            try {
		                prepare = connection.prepareStatement(descQueries[i]);
		                result = prepare.executeQuery();
		                
		                if (result.next()) {
		                    String desc = result.getString("Description");
		                    descriptionLabel[i].setText(desc);
		                    descriptionLabel[i].setWrapText(true);
		                    
		                } else {
		                    System.out.println("Description: No data found for the given ID.");
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
	   
	    // ROOM PAGE - method to display AMENITIES
	    void roomType_displayAmenities(Connection connection, Label[] amenLabel) {	
	    	 
	    
	    	 String [] amenQueries = {
			        	"SELECT Amenities FROM room_type WHERE type_ID = 10",
			        	"SELECT Amenities FROM room_type WHERE type_ID = 20",
			        	"SELECT Amenities FROM room_type WHERE type_ID = 30",
			        	"SELECT Amenities FROM room_type WHERE type_ID = 40"
			        };
		        
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomType; i++) {
		            try {
		                prepare = connection.prepareStatement(amenQueries[i]);
		                result = prepare.executeQuery();
		                
		                if (result.next()) {
		                    String desc = result.getString("Amenities");
		                    amenLabel[i].setText(desc);
		                    amenLabel[i].setWrapText(true);
		                } else {
		                    System.out.println("Amenities: No data found for the given ID.");
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
	    



