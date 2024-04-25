package application.guest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.DB_Connection;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;


public class GuestPage_Controller extends DB_Connection implements Initializable{

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
    
    
    
	
	/*
	//          ---------     SWITCH PAGE     --------
	@FXML
	private AnchorPane dashboard_page, room_page, booking_page, food_page, hk_page, transact_page, profile_page;
	@FXML
	private Button dashboard_btn, room_btn, booking_btn, food_btn, hk_btn, transact_btn, profile_btn;
	
	// Switches tab
	@FXML
	private void handleButtonAction(ActionEvent event) {
		// Get the ID of the source button
	    String buttonId = ((Button) event.getSource()).getId();

	    // Switch on the button ID
	    switch (buttonId) {
	        case "dashboard_btn":
	            dashboard_page.toFront();
	            break;
	        case "room_btn":
	        	room_page.toFront();
	            break;
	        case "booking_btn":
	        	booking_page.toFront();
	            break;
	        case "food_btn":
	        	food_page.toFront();
	            break;
	        case "hk_btn":
	        	hk_page.toFront();
	            break;
	        case "transact_btn":
	        	transact_page.toFront();
	            break;
	        case "profile_btn":
	        	profile_page.toFront();
				break;
	         
	        default:
	        	System.out.println("Invalid");
	        
	            break;
	    }
	}
*/
    
 

    public void initialize(URL location, ResourceBundle resources) {
    	wrapBgImage();
    	
    	
    	
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
