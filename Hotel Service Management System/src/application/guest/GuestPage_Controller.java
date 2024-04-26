package application.guest;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.DB_Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;



public class GuestPage_Controller extends DB_Connection implements Initializable{
	
	
		//  ---------     SWITCH PAGE METHOD     --------
	 	@FXML
	    private Button homeBtn, roomBtn , bookingBtn, foodBtn, hkBtn, aboutUsBtn, profileBtn;
	    @FXML
	    private AnchorPane welcomePage, roomPage, bookingPage, foodPage, hkPage, aboutUsPage, profilePage;
	    @FXML
	    private StackPane rootPane;
	    @FXML
	    private Rectangle rt_bg1, rt_bg2, rt_bg3, rt_bg4;
	    @FXML
	    private Label rt_am1, rt_am2, rt_am3, rt_am4;
	    @FXML
	    private Label rt_betType1, rt_betType2, rt_betType3, rt_betType4;
	    @FXML
	    private Label rt_desc1, rt_desc2, rt_desc3, rt_desc4;
	    @FXML
	    private Label rt_name1, rt_name2, rt_name3, rt_name4;
	    @FXML
	    private Label rt_price1, rt_price2, rt_price3, rt_price4;

	    final int numberOfRoomRype = 4;
	    
	    
	    
		// Declare a HashMap to map button IDs to pages
		private HashMap<String, AnchorPane> pageMap = new HashMap<>();
		// Switches tab
	    @FXML
	    private void handleButtonAction(ActionEvent event) {
	        // Get the ID of the source button
	        String buttonId = ((Button) event.getSource()).getId();

	        // Get the corresponding page from the map
	        AnchorPane currentPage = pageMap.get(buttonId);

	        // Set all pages invisible
	        for (AnchorPane page : pageMap.values()) {
	            page.setVisible(false);
	        }

	        // Set the current page visible
	        if (currentPage != null) {
	            currentPage.setVisible(true);
	        } else {
	        	// for debugging purposes
	            System.out.println("Invalid button ID: " + buttonId);
	        }
	    }
	    

	    // SETS THE BACKGROUND IMAGE
	    public void wrapBgImage() {
	    	  WrappedImageView imageView = new WrappedImageView();
	    	    // Load your image
	    	    Image image = new Image("/images/background/okada night.png");
	    	    // Set the image
	    	    imageView.setImage(image);
	
	    	    rootPane.getChildren().add(imageView);
	    	    imageView.toBack(); // moves it to the back so, we can put more nodes in the front
	    }
	    
	    
	    // ROOM PAGE - display all data
	    void displayRoomPageData() {
	    	connection = connect();
	        // Define an array of queries and corresponding target nodes
	        String[] imageQueries = {
	            "SELECT Image FROM room_type WHERE type_ID = 10",
	            "SELECT Image FROM room_type WHERE type_ID = 20",
	            "SELECT Image FROM room_type WHERE type_ID = 30",
	            "SELECT Image FROM room_type WHERE type_ID = 40"
	        };
	        
	        Rectangle[] targetNodes = {rt_bg1, rt_bg2, rt_bg3, rt_bg4};

	        // Display images
	        for (int i = 0; i < imageQueries.length; i++) {
	            roomType_displayImage(connection,imageQueries[i], targetNodes[i]);
	        }

	        // Display text
	        roomType_displayName(connection);
	        roomType_displayPrice(connection);
	        roomType_displayBedType(connection);
	        roomType_displayDescription(connection);
	        roomType_displayAmenities(connection);
	        close();
	    }

	    // ROOM PAGE - method to display IMAGE
	    void roomType_displayImage(Connection connection, String query, Rectangle targetNode) {
	        try {
	            prepare = connection.prepareStatement(query);
	            result = prepare.executeQuery();

	            if (result.next()) {
	                // Retrieve the file path from the result set
	                String imagePath = result.getString("Image");

	                if (imagePath != null) {
	                    // Load the image from the file path
	                    Image image = new Image("file:" + imagePath);

	                    if (image.isError()) {
	                        System.out.println("Error loading image: " + image.getException());
	                    } else {
	                        // Create an ImagePattern from the Image object
	                        ImagePattern pattern = new ImagePattern(image);

	                        // Set the ImagePattern as the fill for the target node
	                        targetNode.setFill(pattern);
	                        targetNode.setEffect(new DropShadow(2, Color.BLACK)); // Shadow
	                        targetNode.setArcWidth(25.0);   // Corner radius
	                        targetNode.setArcHeight(25.0);
	                    }
	                } else {
	                    System.out.println("No image file path found for the given ID.");
	                }
	            } else {
	                System.out.println("No data found for the given ID.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            // Close resources (result set, prepared statement, connection)
	            try {
	                if (result != null) result.close();
	                if (prepare != null) prepare.close();
	           //     if (connection != null) connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    // ROOM PAGE - method to display NAME
	    void roomType_displayName(Connection connection) {	
	    	 Label[] nameLabel = {rt_name1, rt_name2, rt_name3, rt_name4};
	    
		        String[] nameQueries = {
		            "SELECT Name FROM room_type WHERE type_ID = 10",
		            "SELECT Name FROM room_type WHERE type_ID = 20",
		            "SELECT Name FROM room_type WHERE type_ID = 30",
		            "SELECT Name FROM room_type WHERE type_ID = 40"
		        };

		        // Display text for labels
		        for (int i = 0; i < numberOfRoomRype; i++) {
		            try {
		                prepare = connection.prepareStatement(nameQueries[i]);
		                result = prepare.executeQuery();
		                
		                if (result.next()) {
		                    String name = result.getString("Name");
		                    nameLabel[i].setText(name);
		                } else {
		                    System.out.println("Price per Night: No data found for the given ID.");
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
	    void roomType_displayPrice(Connection connection) {
	    	 Label[] priceLabel = {rt_price1, rt_price2, rt_price3, rt_price4};

	    	 String [] priceQueries = {
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 10",
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 20",
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 30",
			        	"SELECT Price_per_night FROM room_type WHERE type_ID = 40"
			        };
		            
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomRype; i++) {
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
	    void roomType_displayBedType(Connection connection) {
	    	 Label[] bedLabel = {rt_betType1, rt_betType2, rt_betType3, rt_betType4};

		    	 String [] bedTypeQueries = {
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 10",
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 20",
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 30",
		 	        	"SELECT Bed_Type FROM room_type WHERE type_ID = 40"
		 	        };
		            
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomRype; i++) {
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
	    void roomType_displayDescription(Connection connection) {	
	    	 Label[] descriptionLabel = {rt_desc1, rt_desc2, rt_desc3, rt_desc4};
	    	 
	    
	    	 String [] descQueries = {
			        	"SELECT Description FROM room_type WHERE type_ID = 10",
			        	"SELECT Description FROM room_type WHERE type_ID = 20",
			        	"SELECT Description FROM room_type WHERE type_ID = 30",
			        	"SELECT Description FROM room_type WHERE type_ID = 40"
			        };
		        
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomRype; i++) {
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
	    void roomType_displayAmenities(Connection connection) {	
	    	 Label[] amenLabel = {rt_am1, rt_am2, rt_am3, rt_am4};
	    
	    	 String [] amenQueries = {
			        	"SELECT Amenities FROM room_type WHERE type_ID = 10",
			        	"SELECT Amenities FROM room_type WHERE type_ID = 20",
			        	"SELECT Amenities FROM room_type WHERE type_ID = 30",
			        	"SELECT Amenities FROM room_type WHERE type_ID = 40"
			        };
		        
		        // Display text for labels
		        for (int i = 0; i < numberOfRoomRype; i++) {
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
    
	
	
 

    public void initialize(URL location, ResourceBundle resources) {
    	wrapBgImage();
    	
    	// switches page depending on which button has been clicked
    	pageMap.put("homeBtn", welcomePage);
        pageMap.put("roomBtn", roomPage);
        pageMap.put("bookingBtn", bookingPage);
        pageMap.put("foodBtn", foodPage);
        pageMap.put("hkBtn", hkPage);
        pageMap.put("aboutUsBtn", aboutUsPage);
        pageMap.put("profileBtn", profilePage);
    	
        displayRoomPageData();
    	
    	
        // Set the desired width and height of the ImageView
     //   img.setFitWidth(400); // Set the width to 400 pixels
      //  img.setFitHeight(300); // Set the height to 300 pixels
    	
      //  img.fitWidthProperty().bind(rootPane.widthProperty()); 
        
  
       
        
     
/*
    	// Create an instance of WrappedImageView
        WrappedImageView imageView = new WrappedImageView();

        // Load your image
        Image image = new Image("/images/background/okada night.png");

        // Set the image
        imageView.setImage(image);

        // Add the WrappedImageView to the StackPane
        rootPane.getChildren().add(imageView);

    	*/

    	
    	
    	
    	
    	
    	
    }
    
}
