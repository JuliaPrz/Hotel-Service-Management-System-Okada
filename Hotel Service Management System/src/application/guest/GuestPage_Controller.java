package application.guest;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.DB_Connection;
import application.encapsulatedData.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class GuestPage_Controller extends DB_Connection implements Initializable{
	
	
		//  ---------     SWITCH PAGE METHOD     --------
	 	@FXML
	    private Button homeBtn, roomBtn , bookingBtn, foodBtn, hkBtn, aboutUsBtn, profileBtn;
	    @FXML
	    private AnchorPane welcomePage, roomPage, bookingPage, foodPage, hkPage, aboutUsPage, profilePage;
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
	    

    @FXML
	    private StackPane rootPane;
	
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
	    
	    @FXML
	    Rectangle rt_bg1, rt_bg2, rt_bg3, rt_bg4;
	    
	    public void roomTypeImage(){
	    	rt_bg1.setArcWidth(25.0);   // Corner radius
	    	rt_bg1.setArcHeight(25.0);
	    	rt_bg2.setArcWidth(25.0);   // Corner radius
	    	rt_bg2.setArcHeight(25.0);
	    	rt_bg3.setArcWidth(25.0);   // Corner radius
	    	rt_bg3.setArcHeight(25.0);
	    	rt_bg4.setArcWidth(25.0);   // Corner radius
	    	rt_bg4.setArcHeight(25.0);
	
	    	    ImagePattern pattern1 = new ImagePattern(
	    	        new Image("/images/room type/Deluxe King.jpg", 280, 180, false, false) // Resizing
	    	    );
	
	    	    rt_bg1.setFill(pattern1);
	    	    rt_bg1.setEffect(new DropShadow(2, Color.BLACK));  // Shadow
	    	    rt_bg2.setFill(pattern1);
	    	    rt_bg2.setEffect(new DropShadow(2, Color.BLACK));  // Shadow
	    	    rt_bg3.setFill(pattern1);
	    	    rt_bg3.setEffect(new DropShadow(2, Color.BLACK));  // Shadow
	    	    rt_bg4.setFill(pattern1);
	    	    rt_bg4.setEffect(new DropShadow(2, Color.BLACK));  // Shadow
	    }

	    
	    
	    void displayData() {
	    	connection = connect();
	        // Define an array of queries and corresponding target nodes
	        String[] queries = {
	            "SELECT Image FROM room_type WHERE type_ID = 10",
	            "SELECT Image FROM room_type WHERE type_ID = 20",
	            "SELECT Image FROM room_type WHERE type_ID = 30",
	            "SELECT Image FROM room_type WHERE type_ID = 40"
	        };
	        
	        Rectangle[] targetNodes = {rt_bg1, rt_bg2, rt_bg3, rt_bg4};

	        // Display images
	        for (int i = 0; i < queries.length; i++) {
	            displayImage(connection,queries[i], targetNodes[i]);
	        }

	        // Display text
	        displayTextFromDatabase();
	        close();
	    }

	    void displayImage(Connection connection, String query, Rectangle targetNode) {
	 

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

	    void displayTextFromDatabase() {
	        // Code to retrieve and display text from the database using labels
	        // ...
	    }
				
    
    
    
	
	
 

    public void initialize(URL location, ResourceBundle resources) {
    	wrapBgImage();
    	
    	//roomTypeImage();
    	
    	// switches page depending on which button has been clicked
    	pageMap.put("homeBtn", welcomePage);
        pageMap.put("roomBtn", roomPage);
        pageMap.put("bookingBtn", bookingPage);
        pageMap.put("foodBtn", foodPage);
        pageMap.put("hkBtn", hkPage);
        pageMap.put("aboutUsBtn", aboutUsPage);
        pageMap.put("profileBtn", profilePage);
    	
        displayData();
    	
    	
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
