package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;



public class AdminPage_Controller {
	
	// text area in Add Room Type
	@FXML
	private TextArea rType_description;
	
	@FXML
	private TextArea rType_amenities;


	@FXML
	private AnchorPane dashboard_page, room_page, food_page, hk_page, acc_page, transact_page, profile_page;
	
	@FXML
	private Button dashboard_btn, room_btn, food_btn, hk_btn, acc_btn, transact_btn, profile_btn;
	
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
	        case "food_btn":
	        	food_page.toFront();
	            break;
	        case "hk_btn":
	        	hk_page.toFront();
	            break;
	        case "acc_btn":
	        	acc_page.toFront();
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
	
	
    public void initialize() {

    	rType_description.setWrapText(true);
    	rType_amenities.setWrapText(true);
    	
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
