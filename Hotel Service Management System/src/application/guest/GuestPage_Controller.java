package application.guest;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.AlertMessage;
import application.DB_Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	    // ROOM PAGE variable declaration
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

	    final int numberOfRoomType = 4;
	   
	    // BOOKING PAGE
	    @FXML
	    private Button book1, book2, book3, book4;
	    @FXML
	    private TextField bookCardNum, bookCCV, bookExp, bookNameCard;
	    @FXML
	    private Button book_avail;
	    @FXML
	    private DatePicker book_checkInPicker, book_checkOutPicker;
	    @FXML
	    private Rectangle book_image1, book_image2, book_image3, book_image4;
	    @FXML
	    private Label bookName1, bookName2, bookName3, bookName4;

	 
	    @FXML
	    private Label book_stayCount;
	    
	    
	    
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
	        if (buttonId.equals(roomBtn.getId())) {
  	            Rectangle[] targetNodes = {rt_bg1, rt_bg2, rt_bg3, rt_bg4};
	            Label[] nameLabel = {rt_name1, rt_name2, rt_name3, rt_name4}; 
	            Label[] priceLabel = {rt_price1, rt_price2, rt_price3, rt_price4}; 
	            Label[] bedLabel = {rt_betType1, rt_betType2, rt_betType3, rt_betType4}; 
	            Label[] descriptionLabel = {rt_desc1, rt_desc2, rt_desc3, rt_desc4};
	            Label[] amenLabel = {rt_am1, rt_am2, rt_am3, rt_am4}; 
	            
	            // display the data stored in database
	            RoomTypeData roomTypeData = new RoomTypeData(connection);
	            roomTypeData.displayRoomPageData(targetNodes, nameLabel, priceLabel, bedLabel, descriptionLabel, amenLabel);
	        }
	        else if (buttonId.equals(bookingBtn.getId())) {
	        	
	        	 Rectangle[] targetNodes = {book_image1, book_image2, book_image3, book_image4};
	        	 Label[] nameLabel = {bookName1, bookName2, bookName3, bookName4};
	        	 
	        	 BookingPageData bookingData = new BookingPageData(connection);
	        	 bookingData.displayRoomPageData(targetNodes, nameLabel);
	        	
	        	
	        	
	        	
	        	connection = connect();
	        	// Restricts the available date to check in to the current date and 5 years from now
	    		LocalDate minDate = LocalDate.now();
	    		LocalDate maxDate = LocalDate.now().plusYears(5);
	    		book_checkInPicker.setDayCellFactory(d -> new DateCell() {
	    			@Override 
	    		 	public void updateItem(LocalDate item, boolean empty) {
	    						super.updateItem(item, empty);
	    		                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
	    		               }});
	    		book_checkOutPicker.setDayCellFactory(d -> new DateCell() {
	    			@Override 
	    		 	public void updateItem(LocalDate item, boolean empty) {
	    						super.updateItem(item, empty);
	    		                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
	    		               }});
	    		
	    		// Disable all book buttons
	    		Button[] bookButton = {book1, book2, book3, book4};
	    	    for (Button button : bookButton) {
	    	        button.setDisable(true);
	    	    }
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
	    
	
	/////////////////////////////       BOOKING PAGE           ///////////////////////////////////
	    
	   
	    
	    
	    @FXML
	    private void checkButtonAction(ActionEvent event) {
	    	 Button[] bookButtons = {book1, book2, book3, book4};
	    	    AlertMessage alert = new AlertMessage();

	    	    // Disable all book buttons
	    	    for (Button button : bookButtons) {
	    	        button.setDisable(true);
	    	    }
	    	    
	    	    LocalDate checkInDate = book_checkInPicker.getValue();
	    	    LocalDate checkOutDate = book_checkOutPicker.getValue();

	    	    if (checkInDate == null || checkOutDate == null) {
	    	        alert.errorMessage("Choose check-in and check-out date.");
	    	    } else if (checkOutDate.isBefore(checkInDate)) {
	    	    	alert.errorMessage("Check-out date cannot be before the check-in date.");
	    	    }
	    	    
	    	    else {
	    	        try {
	    	            // Prepare the query to check room availability
	    	        	// this query show the number of available room grouped by its type_id
	    	        	String query = "SELECT COUNT(r.Room_no) AS numOfAvailRooms, Type_ID FROM room r " +
	                            " LEFT JOIN booking_transaction bt ON r.Room_no = bt.Room_no " +
	                            " WHERE r.Status = 'available' " +
	                            " AND NOT EXISTS (SELECT * FROM booking_transaction " +
	                            " WHERE Room_no = r.Room_no " +
	                            " AND check_in_date <= ? AND check_out_date >= ?) " +
	                            "GROUP BY Type_ID";
	    	        	
	             prepare = connection.prepareStatement(query);
	             prepare.setDate(1, Date.valueOf(checkInDate));
	             prepare.setDate(2, Date.valueOf(checkOutDate));

	             // Execute the query
	             result = prepare.executeQuery();

	          // Store the type IDs in an array or a collection
	             int[] typeIDs = {10, 20, 30, 40};

	             // Enable book buttons for available room types
	             while (result.next()) {
	                 int numOfAvailRooms = result.getInt("numOfAvailRooms");
	                 int typeID = result.getInt("Type_ID");
	                 
	                 // Check if the type ID is in the array of type IDs
	                 for (int i = 0; i < typeIDs.length; i++) {
	                     if (typeID == typeIDs[i] && numOfAvailRooms > 0) {
	                         bookButtons[i].setDisable(false); // Enable the corresponding button
	                         break; // Exit the loop once the button is enabled
	                     }
	                 }
	             }
	    	            

	    	            // Close resources
	    	            result.close();
	    	            prepare.close();
	    	        } catch (SQLException e) {
	    	            e.printStackTrace();
	    	        }
	        }
	    }
	        
	    
	    @FXML
	    private void bookButtonAction(ActionEvent event) {
	    	int[] typeIDs = {10, 20, 30, 40};
	        Button clickedButton = (Button) event.getSource();
	        int buttonIndex = Integer.parseInt(clickedButton.getId().substring(4)) - 1; 
	        int roomTypeID = typeIDs[buttonIndex]; // Assuming typeIDs array contains the corresponding type IDs
	        
	        LocalDate checkInDate = book_checkInPicker.getValue();
    	    LocalDate checkOutDate = book_checkOutPicker.getValue();
	        try {
	            // Establish connection
	        	connection = connect();

	        	// Find an available room of the selected type that is not occupied
	            String findRoomQuery = "SELECT Room_no FROM room WHERE Status = 'Available' AND Type_ID = ? AND Room_no NOT IN (" +
	                                   "SELECT Room_no FROM booking_transaction WHERE Room_no = room.Room_no AND (? < check_out_date AND ? > check_in_date)" +
	                                   ")";
	            prepare = connection.prepareStatement(findRoomQuery);
	            prepare.setInt(1, roomTypeID);
	            prepare.setDate(2, Date.valueOf(checkOutDate)); 
	            prepare.setDate(3, Date.valueOf(checkInDate)); 
	            result = prepare.executeQuery();

	            // If available room is found, assign it to the user
	            if (result.next()) {
	            	
	            	
	            	
	            	
	            	
	               /* int roomNo = result.getInt("Room_no");

	                // Update room status to occupied
	                String updateStatusQuery = "UPDATE room SET Status = 'Occupied' WHERE Room_no = ?";
	                prepare = connection.prepareStatement(updateStatusQuery);
	                prepare.setInt(1, roomNo);
	                prepare.executeUpdate();
	                */

	           
	                connection.close();

	                // Update UI or display message to indicate successful booking
	             //   System.out.println("Room " + roomNo + " has been assigned to the user.");
	            } else {
	                System.out.println("No available rooms of type " + roomTypeID + " found.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
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
