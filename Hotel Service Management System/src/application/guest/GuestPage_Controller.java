package application.guest;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.AlertMessage;
import application.DB_Connection;
import application.logIn_signUp.LogIn_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
	    private Label book_stayCount, book_stayCount2; //count2 is the label in the summary
	    
	    @FXML
	    private Group bookingSummary;
	    @FXML
	    private Label noEntry, dates, roomPrice, roomTax, chosenRoom, booking_total, roomNumLabel;
	    
	    
	    
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
	        	 bookingSummary.setVisible(false); // hide booking summary
	        	 
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
	    		
	    		
	    		//calculate the stay count based on check-in and check-out dates
	    		// Bind calculateStayCount method to the valueProperty of date pickers
	    	    book_checkInPicker.valueProperty().addListener((obs, oldVal, newVal) -> calculateStayCount());
	    	    book_checkOutPicker.valueProperty().addListener((obs, oldVal, newVal) -> calculateStayCount());
	    		calculateStayCount();
	    		
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
	    
	   
	 // Method to calculate the stay count based on check-in and check-out dates
	    @FXML
	    private Long calculateStayCount() {
	        AlertMessage alert = new AlertMessage();
	        LocalDate checkInDate = book_checkInPicker.getValue();
	        LocalDate checkOutDate = book_checkOutPicker.getValue();

	        String day, night;
	        boolean errorMessageShown = false; // Flag to track if error message has been shown
	      

	        if (checkInDate == null || checkOutDate == null) {
	            book_stayCount.setText("");
	        } else if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
	            if (!errorMessageShown) {
	                alert.errorMessage("Check-out date cannot be before or during the check-in date.");
	                errorMessageShown = true; // Set flag to true to indicate error message has been shown
	            }
	        } else {
	            // Calculate the stay count
	            long stayCount = ChronoUnit.DAYS.between(checkInDate, checkOutDate) + 1;
	            long nightStayCount = 0;
	            
	            //set the count for night stay the number of days - 1
	            nightStayCount = stayCount-1;
	            
	            if (stayCount > 1) day = " Days ";
	            else day = " Day ";
	            
	            if (stayCount-1 > 1) night = " Nights";
	            else night = " Night";
	            
	            book_stayCount.setText(stayCount + day + nightStayCount + night);
	           // book_stayCount2.setText(stayCount + day + nightStayCount + night);
	            
	            return nightStayCount;
	        }
	        
	        return null;
	      
	    }
	    
	    
	    // disables the BOOK buttons if the user changed the date; they must check the availability first to enable it
	    @FXML
	    private void datePickerAction(ActionEvent event) {
	    	 Button[] bookButtons = {book1, book2, book3, book4};

	    	    // Disable all book buttons
	    	    for (Button button : bookButtons) {
	    	        button.setDisable(true);
	    	    }
	    }
	    
	    
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
	        int roomTypeID = typeIDs[buttonIndex];
	        
	        LocalDate checkInDate = book_checkInPicker.getValue();
    	    LocalDate checkOutDate = book_checkOutPicker.getValue();
    	    
    	 // Create a DateTimeFormatter to format the dates
    	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");

    	    // Format the dates into strings
    	    String formattedCheckInDate = checkInDate.format(formatter);
    	    String formattedCheckOutDate = checkOutDate.format(formatter);

    	    // Concatenate the formatted dates and display them in the label
    	    String labelValue = formattedCheckInDate + " - " + formattedCheckOutDate;
    	    
    	    
	        try {
	            // Establish connection
	        //	connection = connect();

	        	// Find an available room of the selected type that is not occupied
	            String findRoomQuery = "SELECT r.Room_No, rtB.Name, rtB.Price_per_Night, rtB.Tax " +
	                       "FROM room_type AS rtB " +
	                       "INNER JOIN room AS r ON r.Type_ID = rtB.Type_ID " +
	                       "WHERE r.Status = 'Available' " +
	                       "AND rtB.Type_ID = ? " +
	                       "AND r.Room_no NOT IN (" +
	                       "SELECT Room_no FROM booking_transaction WHERE Room_no = r.Room_no AND (? < check_out_date AND ? > check_in_date)" +
	                       ")";
	            
	            prepare = connection.prepareStatement(findRoomQuery);
	            prepare.setInt(1, roomTypeID);
	            prepare.setDate(2, Date.valueOf(checkOutDate)); 
	            prepare.setDate(3, Date.valueOf(checkInDate)); 
	            result = prepare.executeQuery();

	            // If available room is found, show the booking summary
	            if (result.next()) {
	            	String roomName = result.getString("Name");
	            	String dbRoomPrice = result.getString("Price_per_Night");
	            	String dbRoomTax = result.getString("Tax"); 	
	            	int roomNo = result.getInt("Room_no");
	            	
	            	Long night = calculateStayCount();
	            	
	            	// Convert the price and tax to numbers
	                double pricePerNight = Double.parseDouble(dbRoomPrice) * night;
	                double tax = Double.parseDouble(dbRoomTax) * night;

	                // Calculate the total
	                double total = pricePerNight + tax;
	            	
	            	// Create a DecimalFormat object for formatting the numbers
	                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

	                // Format the price and tax with commas and a currency symbol
	                String formattedPrice = decimalFormat.format(pricePerNight) + " PHP";
	                String formattedTax = decimalFormat.format(tax) + " PHP";
	                String formattedTotal = decimalFormat.format(total) + " PHP";
	            	
	                
	                String dayString;
	                String nightString;
	                // Calculate the stay count
		            long stayCount = ChronoUnit.DAYS.between(checkInDate, checkOutDate) + 1;
		            long nightStayCount = 0;
		             
		            nightStayCount = stayCount-1;
		            
		            if (stayCount > 1) dayString = " Days ";
		            else dayString = " Day ";
		            
		            if (stayCount-1 > 1) nightString = " Nights";
		            else nightString = " Night";
		            
		            book_stayCount2.setText(stayCount + dayString + nightStayCount + nightString);
		            roomNumLabel.setText(roomNo+ "");
	                
	            	dates.setText(labelValue);
	                chosenRoom.setText(roomName);
	                roomPrice.setText(formattedPrice);
	                roomTax.setText(formattedTax);                
	            	booking_total.setText("" +  formattedTotal);
	            	
	            	noEntry.setVisible(false);
	            	bookingSummary.setVisible(true);

	                //connection.close();

	                // Update UI or display message to indicate successful booking
	             //   System.out.println("Room " + roomNo + " has been assigned to the user.");
	            } else {
	                System.out.println("No available rooms of type " + roomTypeID + " found.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    private void payButtonAction(ActionEvent event) {
	    	AlertMessage alert = new AlertMessage();
	       	String labelValue = dates.getText(); 

        	// Split the dates text based on the delimiter "-"
        	String[] dates = labelValue.split(" - ");

        	// Extract the check-in date and check-out date
        	String formattedCheckInDate = dates[0].trim(); // Remove leading/trailing whitespaces
        	String formattedCheckOutDate = dates[1].trim(); // Remove leading/trailing whitespaces

        	// Parse the formatted dates into LocalDate objects
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
        	
        	// used to find a pattern if the CCV is numeric numbers only; continues in the if else statement
        	
    		boolean CCVisNumeric = bookCCV.getText().matches("[0-9]+");
    		boolean nameCardIsOnlyLetters = bookNameCard.getText().matches("[a-zA-Z\\s]+"); // will match any string that contains one or more letters (uppercase or lowercase) and/or spaces.
    		
    		// Get the expiry date from the bookExp TextField
    		String expiryDate = bookExp.getText();

    		// Regular expression to match the format "MM/YY"
    		String regex = "^(0[1-9]|1[0-2])/(\\d{2})$";
		
    		    // Split the expiry date into month and year
    		    String[] parts = expiryDate.split("/");
    		
        	if (bookCardNum.getText().isEmpty() || bookCCV.getText().isEmpty() || bookExp.getText().isEmpty() || bookNameCard.getText().isEmpty())
        		alert.errorMessage("Please fill up all payment information.");
        	else if (bookCardNum.getText().length() < 16 || bookCardNum.getText().length() > 16) 
        		alert.errorMessage("Invalid card number.");
        	else if (!CCVisNumeric || bookCCV.getText().length() > 3 ||  bookCCV.getText().length() < 3)
        		alert.errorMessage("Invalid CCV (security code) number.");
        	else if (!nameCardIsOnlyLetters)
        		alert.errorMessage("Invalid card name.");
        	else if (!expiryDate.matches(regex))  // Check if the expiry date matches the required format
    		    alert.errorMessage("Invalid expiry date. Please use the format MM/YY.");
        	else if (parts.length != 2) {
        	    alert.errorMessage("Invalid expiry date format. Please use the format MM/YY.");
        	} else {
        	    try {
        	        int expMonth = Integer.parseInt(parts[0]);
        	        int expYear = Integer.parseInt(parts[1]);

        	        // Get the current month and year
        	        LocalDate currentDate = LocalDate.now();
        	        int currentMonth = currentDate.getMonthValue();
        	        int currentYear = currentDate.getYear() % 100; // Get the last two digits of the year

        	        if (expYear < currentYear || (expYear == currentYear && expMonth < currentMonth)) {
        	            alert.errorMessage("Card has expired.");
        	        } else {
        	        	try {
                			// Get the total string from the booking_total label
                			String totalString = booking_total.getText();
                			// Remove commas and currency symbols from the string
                			String cleanTotalString = totalString.replaceAll("[^\\d.]", "");

        	                // Get the necessary information from the UI components
        	        		int roomNo = Integer.parseInt(roomNumLabel.getText());
        		            String transacType = "Booking Transaction";
        		            Double total = Double.parseDouble(cleanTotalString);
        		            LocalDate date =  LocalDate.now();
        		            LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS); // current time with seconds
        		            LocalDate checkInDate = LocalDate.parse(formattedCheckInDate, formatter);
        		        	LocalDate checkOutDate = LocalDate.parse(formattedCheckOutDate, formatter);

        		        	int accountID = LogIn_Controller.getAccountID();
        		        	
        		            // Use a SQL query to insert the booking information into the database
        		            String insertQuery = "INSERT INTO booking_transaction (guest_account_id, room_no, transaction_type, total_price, date, time, check_in_date, check_out_date) "
        		            					+ "VALUES (?,?,?,?,?,?,?,?)";
        		            prepare = connection.prepareStatement(insertQuery);
        		            prepare.setInt(1, accountID);
        		            prepare.setInt(2, roomNo);
        		            prepare.setString(3, transacType);
        		            prepare.setDouble(4, total);
        		            prepare.setDate(5, java.sql.Date.valueOf(date)); // Convert LocalDate to java.sql.Date
        		            prepare.setTime(6, java.sql.Time.valueOf(time)); // Convert LocalTime to java.sql.Time
        		            prepare.setDate(7, java.sql.Date.valueOf(checkInDate)); // Convert LocalDate to java.sql.Date
        		            prepare.setDate(8, java.sql.Date.valueOf(checkOutDate)); // Convert LocalDate to java.sql.Date
        		            // Execute the insert query
        		            prepare.executeUpdate();

        		            // Optionally, display a message to indicate successful payment
        		           alert.infoMessage("Booked successfully! See you!");
        		           clearPaymentFields(); //clear fields
        	                // Update room status to occupied
        	                String updateStatusQuery = "UPDATE room SET Status = 'Occupied' WHERE Room_no = ?";
        	                prepare = connection.prepareStatement(updateStatusQuery);
        	                prepare.setInt(1, roomNo);
        	                prepare.executeUpdate();
        	               
        	                connection.close();
 
        	        } catch (SQLException e) {
        	            e.printStackTrace();
        	        }    		
        	        }
        	    } catch (NumberFormatException e) {
        	        alert.errorMessage("Invalid expiry date format. Please use numeric values for month and year.");
        	    }
        	}	
 }

	    // clears the text fields
	    public void clearPaymentFields() {
	    	bookCardNum.setText("");
	    	bookCCV.setText("");
	    	bookExp.setText("");
	    	bookNameCard.setText("");
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
