package application.receptionist;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import application.AlertMessage;
import application.DB_Connection;
import application.guest.GuestPage_Controller;
import application.logIn_signUp.LogIn_Controller;
import application.logIn_signUp.SignUp_Controller.DeriveAge;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CheckIn_Controller extends DB_Connection {
	

    @FXML
    private TextField fName_txtField, lName_txtField;
    @FXML
    private DatePicker bdate_datePicker;
    @FXML
    private TextField contact_txtField;

    @FXML
    private Button checkAvail_Btn;
    @FXML
    private ComboBox<String> roomType_cbb;
    @FXML
    private ComboBox<String> roomNum_cbb;
    @FXML
    private DatePicker checkIn_datePicker;
    @FXML
    private DatePicker checkOut_datePicker;
 
    @FXML
    private Group cashGroup, cardGroup;
    @FXML
    private ComboBox<String> paymentOption_cbb;
    @FXML
    private TextField cashAmount_txtField;
    @FXML
    private Label change_label, total_label;
    @FXML
    private TextField cardNum_txtField, nameOnCard_txtField, CCV_txtField, expiryDate_txtField;

    @FXML
     private Button checkIn_Btn;
    private int roomTypeID = -1;
    
    
    
    @FXML
    void checkIn(){
    	connection = connect();
    	AlertMessage alert = new AlertMessage();
    	
    	// used to verify the length of the phone number; continues in the if else statement
    	String contact = contact_txtField.getText();
    	String firstTwoNum = null;
    	if (contact.length() >= 2) 
    		firstTwoNum = contact.substring(0, 2);
    	
    	// used to find a pattern if the phone number is numeric numbers only; continues in the if else statement
		boolean phoneIsNumeric = contact.matches("[0-9]+");
		boolean fNameOnlyLetters = fName_txtField.getText().matches("[a-zA-Z.]+");
		boolean lNameOnlyLetters = lName_txtField.getText().matches("[a-zA-Z.]+");
    	
    	// Calculate age using DeriveAge
        LocalDate currentDate = LocalDate.now();
        LocalDate birthdate = bdate_datePicker.getValue();
        int age = DeriveAge.calculateAge(birthdate, currentDate); // from SignUp_Controller
        
        // POPULATES ROOM TYPE CBB
		String getRoomTypeQuery = "SELECT `Name` AS RoomTypes " +
								  "FROM ROOM_TYPE " +
								  "WHERE Type_ID IN ( " +
								  "    SELECT Type_ID " +
								  "    FROM ROOM " +
								  "    WHERE `Status` = 'Available' " +
								  ")";
		try {
		    prepare = connection.prepareStatement(getRoomTypeQuery);
		    result = prepare.executeQuery();
		    
		    ObservableList<String> listRoomTypes = FXCollections.observableArrayList();
		    
		    while(result.next()) {
		    	String roomTypes = result.getString("RoomTypes");
		    	listRoomTypes.add(roomTypes);
		    }
		    roomType_cbb.setItems(listRoomTypes);
		    
		    
		} catch (SQLException e) {
    	    e.printStackTrace();
    	}
		
		// POPULATES ROOM NUMBER CBB
		LocalDate checkInDate = checkIn_datePicker.getValue();
	    LocalDate checkOutDate = checkOut_datePicker.getValue();
	    if (checkInDate == null || checkOutDate == null)
	        alert.errorMessage("Choose check-in and check-out date.");
	    else if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) 
            alert.errorMessage("Check-out date cannot be before or during the check-in date.");
	    else  {
	    	String roomType = roomType_cbb.getValue();
		    String promptText =  roomType_cbb.getPromptText();
		    
		 // Check if a value is selected; if not, use the prompt text
		    String selectedRoomType = roomType != null ? roomType : promptText;
	    	
	    	String checkQuery = "SELECT r.Room_No AS AvailRooms " +
			                    "FROM room_type AS rt " +
			                    "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " +
			                    "WHERE rt.`Name` = ? " +
			                    "AND r.Room_No NOT IN ( " +
			                    "    SELECT Room_no " +
			                    "    FROM `transaction` " +
			                    "    WHERE check_in_date <= ? " +
			                    "    AND check_out_date >= ? " +
			                    ")";
	    	try {
				  prepare = connection.prepareStatement(checkQuery);
				  prepare.setString(1, selectedRoomType);
				  prepare.setDate(2, java.sql.Date.valueOf(checkOutDate));
				  prepare.setDate(3, java.sql.Date.valueOf(checkInDate));
				  result = prepare.executeQuery();
				  
				  ObservableList<String> listAvailRoom = FXCollections.observableArrayList();
				    
				    while(result.next()) {
				    	String roomTypes = result.getString("AvailRooms");
				    	listAvailRoom.add(roomTypes);
				    }
				    roomNum_cbb.setItems(listAvailRoom);
			  
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
	 }
	    
	 // Attach an event listener to the ComboBox for room types to get the type_ID
        roomType_cbb.setOnAction(event -> {
            String selectedRoomType = roomType_cbb.getSelectionModel().getSelectedItem();
            if (selectedRoomType != null) {
                try {
                    //  query to retrieve the Type_ID based on the selected room type
                    String getTypeIDQuery = "SELECT Type_ID " +
                                            "FROM room_type " +
                                            "WHERE Name = ?";
                    prepare = connection.prepareStatement(getTypeIDQuery);
                    prepare.setString(1, selectedRoomType);
                    result = prepare.executeQuery();
                    
                    // Check if a result is found
                    if (result.next()) {
                         roomTypeID = result.getInt("Type_ID"); //Type_ID corresponding to the selected room type 
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	    
	  
     // RETRIEVE DATA OF ROOM TO CALCULATE THE TOTAL
        // SQL query to find available rooms of a specific type for a given date range
	       String findRoomQuery = "SELECT rt.Name, rt.Price_per_Night, rt.Tax " + // Select room number, name, price per night, and tax
	    		   			"FROM room_type AS rt " + // From room_type table
	                        "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " + // Join with room table based on Type_ID
	                        "WHERE rt.Type_ID = ? " + // Filter by the specified room type
	                        "AND r.Room_no NOT IN (" + // Exclude rooms that are already booked for the specified date range
	                       		"SELECT Room_no " +
	                       		"FROM `Transaction` " + // Subquery to find booked rooms
	                       		"WHERE Room_no = r.Room_no " + // Filter by room number
	                       		"AND (check_in_date <= ? AND check_out_date >= ?))"; // Condition to check for overlapping bookings
	            
	       try {
	    	   prepare = connection.prepareStatement(findRoomQuery);
	    	   prepare.setInt(1, roomTypeID);
	    	   if (checkOutDate != null)
		       prepare.setDate(2, Date.valueOf(checkOutDate)); 
		       if (checkInDate != null)  
		       prepare.setDate(3, Date.valueOf(checkInDate)); 
		       result = prepare.executeQuery();
		            
		            
		       // If available room is found
		        if (result.next()) {
		            String roomPrice = result.getString("Price_per_Night");
		           	String roomTax = result.getString("Tax"); 	
		         
		       // Calculate the stay count
		            long stayCount = ChronoUnit.DAYS.between(checkInDate, checkOutDate) + 1;
		            long nightStayCount = 0;
		            
		            //set the count for night stay the number of days - 1
		            nightStayCount = stayCount-1;
		       // Convert the price and tax to numbers
	             double pricePerNight = Double.parseDouble(roomPrice) * nightStayCount;
	             double tax = Double.parseDouble(roomTax) * nightStayCount;

	             // Calculate the total
	             double total = pricePerNight + tax;
	      // Create a DecimalFormat object for formatting the numbers
	           DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
	           String formattedTotal = decimalFormat.format(total) + " PHP";
	           total_label.setText("" +  formattedTotal);
		        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	       
        
	    String[] paymentOptions = {"CASH", "CARD"};
		ObservableList<String> options = FXCollections.observableArrayList(paymentOptions);
		paymentOption_cbb.setItems(options);
    	        
       String paymentOption = paymentOption_cbb.getValue();
       String promptText = paymentOption_cbb.getPromptText();

       
       // used to find a pattern if the card number and CCV is numeric numbers only; continues in the if else statement
   		boolean CardNumisNumeric = cardNum_txtField.getText().matches("[0-9]+");
		boolean CCVisNumeric = CCV_txtField.getText().matches("[0-9]+");
		boolean nameCardIsOnlyLetters = nameOnCard_txtField.getText().matches("[a-zA-Z\\s]+"); // will match any string that contains one or more letters (uppercase or lowercase) and/or spaces.
		boolean CashisNumeric = cashAmount_txtField.getText().matches("[0-9]+");
		// Get the expiry date from the bookExp TextField
		String expiryDate = expiryDate_txtField.getText();

		// Regular expression to match the format "MM/YY"
		String regex = "^(0[1-9]|1[0-2])/(\\d{2})$";
	
		// Split the expiry date into month and year
		String[] parts = expiryDate.split("/");
      
		    // CONDITIONS
    	
    	// Check if there are empty fields
    	if (fName_txtField.getText().isEmpty() 
    			|| lName_txtField.getText().isEmpty() 
    			|| birthdate == null 
    			|| contact_txtField.getText().isEmpty() 
    			|| checkIn_datePicker.getValue().equals(null)
    			|| checkOut_datePicker.getValue().equals(null)
    			|| roomType_cbb.getValue().equals(null)
    			|| roomNum_cbb.getValue().equals(null)
    			
    			) 
    		alert.errorMessage("Please fill up all details.");
    		// checks if cash amount 
    	else if (paymentOptions[0].equals(paymentOption) || paymentOptions[0].equals(promptText) && cashAmount_txtField.getText().isEmpty())
    		alert.errorMessage("Please fill up all details.");
    	else if (paymentOptions[1].equals(paymentOption) 
    			&& cardNum_txtField.getText().isEmpty() 
    			|| nameOnCard_txtField.getText().isEmpty()
    			|| CCV_txtField.getText().isEmpty()
    			|| expiryDate_txtField.getText().isEmpty())
    		alert.errorMessage("Please fill up all details.");
    	else if (!fNameOnlyLetters || ! lNameOnlyLetters || fName_txtField.getText().length() > 30 || lName_txtField.getText().length() > 20)
    		alert.errorMessage("Invalid name.");   	
    	// makes sure that the guest is of legal age
    	else if (age < 18 ) 
    		alert.errorMessage("You must be 18 years old and above to create an account.");
    	// checks the length of the contact number
    	else if (contact.length() <11 || contact.length() > 11) 
    		alert.errorMessage("Invalid phone number.");	
    	// checks if the phone number starts with 09 or if phone is not numeric
    	else if (!firstTwoNum.equals("09") || !phoneIsNumeric) 
    		alert.errorMessage("Invalid phone number.");
    	else if (paymentOptions[1].equals(paymentOption) && !CardNumisNumeric || cardNum_txtField.getText().length() < 16 || cardNum_txtField.getText().length() > 16) 
    		alert.errorMessage("Invalid card number.");
    	else if (paymentOptions[1].equals(paymentOption)  && !CCVisNumeric || CCV_txtField.getText().length() > 3 ||  CCV_txtField.getText().length() < 3)
    		alert.errorMessage("Invalid CCV (security code) number.");
    	else if (paymentOptions[1].equals(paymentOption)  && !nameCardIsOnlyLetters)
    		alert.errorMessage("Invalid card name.");
    	else if (paymentOptions[1].equals(paymentOption)  && !expiryDate.matches(regex))  // Check if the expiry date matches the required format
		    alert.errorMessage("Invalid expiry date. Please use the format MM/YY.");
    	else if (paymentOptions[1].equals(paymentOption)  && parts.length != 2) 
    	    alert.errorMessage("Invalid expiry date format. Please use the format MM/YY.");
    	else if (paymentOptions[0].equals(paymentOption) && !CashisNumeric || cashAmount_txtField.getText().length() > 16) 
    		alert.errorMessage("Invalid cash amount.");
    	else {
    		try {
    	        int expMonth = Integer.parseInt(parts[0]);
    	        int expYear = Integer.parseInt(parts[1]);

    	        // Get the current month and year  	        
    	        int currentMonth = currentDate.getMonthValue();
    	        int currentYear = currentDate.getYear() % 100; // Get the last two digits of the year

    	        if (paymentOptions[1].equals(paymentOption) && expYear < currentYear || (expYear == currentYear && expMonth < currentMonth)) {
    	            alert.errorMessage("Card has expired.");
    	        } else {
    	        	try {
    	        		// capitalizes first letter of the name
    	    			String firstName = fName_txtField.getText();
    	    			String lastName = lName_txtField.getText();
    	    			if (!firstName.isEmpty() && !lastName.isEmpty()) {
    	    			    firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
    	    			    lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
    	    			}    	        		
    	        		// Insert into GUEST table in the database
    	    			String insertGuestData = "INSERT INTO GUEST " 
    	    						+ "(Last_Name, First_Name, Birthdate, Age, Contact_No, Guest_Type) " 
    	    						+ "VALUES (?,?,?,?,?,?) ";
    	    			
    	    				prepare = connection.prepareStatement (insertGuestData, Statement.RETURN_GENERATED_KEYS); 
    	    				prepare.setString (1, lastName); 
    	    				prepare.setString (2, firstName); 
    	    				 // Convert LocalDate to java.sql.Date
    	    			    Date bdate = Date.valueOf(birthdate);
    	    				prepare.setDate (3, bdate);
    	    				prepare.setInt (4, age);
    	    				prepare.setString (5, contact);
    	    				prepare.setString (6, "Walk-In Guest");
    	    				
    	    				prepare.executeUpdate();
    	    				
    	    				// INSERT INTO WALK-IN GUEST TABLE
    	    				result = prepare.getGeneratedKeys();
    	    		            int guestID = -1; // Initialize to a default value
    	    		            if (result.next()) {
    	    		                guestID = result.getInt(1); // Get the generated payment_ID
    	    		            }
    	    		            
    	    		            if (guestID != -1) {
    	    		            // Insert into BOOKED_GUEST table
    	    		    			String insertData = "INSERT INTO WALK_IN_GUEST (Guest_ID) " 
    	    	 						+ "VALUES (?) ";
    	    		    			prepare = connection.prepareStatement (insertData); 
    	    		    			prepare.setInt(1, guestID);
    	    		    			prepare.executeUpdate(); 	
    	    		            }
    	        	
    	// INSERT DATA TO PAYMENT DETAILS
    	    		            
    	    		         // Get the total string from the booking_total label
                    			String totalString = total_label.getText();
                    			// Remove commas and currency symbols from the string
                    			String cleanTotalString = totalString.replaceAll("[^\\d.]", "");
                    	
                    			 // Get the necessary information from the UI components
                    			int roomNo = Integer.parseInt(roomNum_cbb.getValue());
                    			Double total = Double.parseDouble(cleanTotalString);
             		            LocalDate date =  LocalDate.now();
             		            LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS); // current time with seconds
             		            
             		        // Get the selected payment option from the ComboBox
             		           String payType = paymentOption_cbb.getValue();

             		           // If no value is selected, use the prompt text as the payment option
             		           if (payType == null || payType.isEmpty()) {
             		               payType = paymentOption_cbb.getPromptText();
             		           }
             		           
             		           String payment_type = "";
             		           if (paymentOptions[1].equals(paymentOption)) 
             		        	  payment_type = "Card Payment";
             		           else 
             		        	  payment_type = "Cash Payment";
             		           

           		            String insertPaymentDetails = "INSERT INTO payment_details (Total_Price, payment_type) "
           		                + "VALUES (?,?)";
           		            prepare = connection.prepareStatement(insertPaymentDetails, Statement.RETURN_GENERATED_KEYS);
           		            prepare.setDouble(1, total);
           		            prepare.setString(2, payment_type);
           		            prepare.executeUpdate();
           		            
           		         // Retrieve the generated payment_ID
           		            result = prepare.getGeneratedKeys();
           		            int paymentId = -1; // Initialize to a default value
           		            if (result.next()) {
           		                paymentId = result.getInt(1); // Get the generated payment_ID
           		            }
    	    		            
           		         if (paymentId != -1) {
     		            	
     		            	// insert the booking information into the transaction table in the database
         		            String insertBooking = "INSERT INTO `Transaction` (Guest_ID, Room_no, Payment_ID,  Date, Time, Check_in_date, Check_out_date) "
         		            					+ "VALUES (?,?,?,?,?,?,?)";
         		            prepare = connection.prepareStatement(insertBooking);
         		            prepare.setInt(1, guestID);
         		            prepare.setInt(2, roomNo);
         		            prepare.setInt(3, paymentId); // Set the retrieved payment_ID
         		            prepare.setDate(4, java.sql.Date.valueOf(date)); // Convert LocalDate to java.sql.Date
         		            prepare.setTime(5, java.sql.Time.valueOf(time)); // Convert LocalTime to java.sql.Time
         		            prepare.setDate(6, java.sql.Date.valueOf(checkInDate)); // Convert LocalDate to java.sql.Date
         		            prepare.setDate(7, java.sql.Date.valueOf(checkOutDate)); // Convert LocalDate to java.sql.Date
         		            
         		            // Execute the insert query
         		            prepare.executeUpdate();
     		            	
         		            
         		            if (paymentOptions[1].equals(paymentOption)) {
         		            	
         		            	 // Insert the card payment information with the retrieved payment_ID
         		                String insertCardInfo = "INSERT INTO CARD_PAYMENT (Payment_ID, Card_Number, Name_OnCard, CCV, Expiry_Date) " +
         		                						"VALUES (?, ?, ?, ?, ?)";
         		                prepare = connection.prepareStatement(insertCardInfo);
         		                prepare.setInt(1, paymentId); // Set the retrieved payment_ID
         		                prepare.setString(2, cardNum_txtField.getText());
         		                prepare.setString(3, nameOnCard_txtField.getText());
         		                prepare.setString(4, CCV_txtField.getText());
         		                prepare.setString(5, expiryDate_txtField.getText());
         		                prepare.executeUpdate();
         		                
         		            } else if (paymentOptions[0].equals(paymentOption))  {
         		            // Insert the card payment information with the retrieved payment_ID
         		                String insertCashInfo = "INSERT INTO CASH_PAYMENT (Payment_ID, Cash_Amount, Change) " +
         		                						"VALUES (?, ?, ?)";
         		                prepare = connection.prepareStatement(insertCashInfo);
         		                prepare.setInt(1, paymentId); // Set the retrieved payment_ID
         		                prepare.setString(2, cashAmount_txtField.getText());
         		                prepare.setString(3, change_label.getText());
         		                prepare.executeUpdate();
   	
         		            }else
         		            	// for debugging; failed to retrieve the payment_ID
         		                System.out.println("Failed to retrieve payment_ID.");
         		            	// indicate successful payment
         		            	alert.infoMessage("Checked-in successfully!");
         		            	clearFields();
         		            	
         		            	// UPDATE THE STATUS OF THE ROOM
         		            	GuestPage_Controller updateStatus = new GuestPage_Controller();
         		            	updateStatus.updateRoomStatus();
         		            	
           		 }	
    	        } catch (SQLException e) {
    	            e.printStackTrace();
    	        }    		
    	     }
    	  } catch (NumberFormatException e) {
    	        alert.errorMessage("Invalid expiry date format. Please use numeric values for month and year.");
    	  }

    	} // end of else statement
  } // end of method
	
    
    
    
    
    public void clearFields() {
    	fName_txtField.setText("");
    	lName_txtField.setText("");
    	bdate_datePicker.setValue(null);
    	contact_txtField.setText("");
    	
    	checkIn_datePicker.setValue(null);
    	checkOut_datePicker.setValue(null);
    	roomType_cbb.setValue(null);
    	roomNum_cbb.setValue(null);
    	
    	total_label.setText("");
    	
    	cardNum_txtField.setText("");
    	nameOnCard_txtField.setText("");
    	CCV_txtField.setText("");
    	expiryDate_txtField.setText("");
    	
    	cashAmount_txtField.setText("");
    	change_label.setText("");
    	

    }
	

 
	
    public void initialize() {
    	// populate the paymentOption_cbb with either cash or card
    	String[] paymentOptions = {"CASH", "CARD"};
		ObservableList<String> options = FXCollections.observableArrayList(paymentOptions);
		paymentOption_cbb.setItems(options);
    	        
		String paymentOption = paymentOption_cbb.getValue();
		String promptText = paymentOption_cbb.getPromptText();

        	       // SHOW THE CHOSEN PAYMENT OPTION UI
		if (paymentOptions[0].equals(paymentOption) || paymentOptions[0].equals(promptText)) {
        	cashGroup.setVisible(true); 
            cardGroup.setVisible(false); 
        } else {
        	cardGroup.setVisible(true); 
        	cashGroup.setVisible(false); 
        }
		
		// Restricts the available date in birthdate datepicker
		LocalDate bDayMinDate = LocalDate.of(1930, 1, 1);
		LocalDate bDayMaxDate = LocalDate.now();
		bdate_datePicker.setDayCellFactory(d -> new DateCell() {
			@Override 
			public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);
			setDisable(item.isAfter(bDayMaxDate) || item.isBefore(bDayMinDate));
		}});
		
		// Restricts the available date in check-in datepicker
		LocalDate checkInMinDate = LocalDate.now();
		LocalDate checkInMaxDate = LocalDate.now();
		checkIn_datePicker.setDayCellFactory(d -> new DateCell() {
			@Override 
			public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);
			setDisable(item.isAfter(checkInMaxDate) || item.isBefore(checkInMinDate));
		}});	
		
		
		// Restricts the available date in check-out datepicker
		LocalDate checkOutMinDate = LocalDate.now();
		LocalDate checkOutMaxDate = LocalDate.now().plusYears(5);
		checkOut_datePicker.setDayCellFactory(d -> new DateCell() {
			@Override 
			public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);
			setDisable(item.isAfter(checkOutMaxDate) || item.isBefore(checkOutMinDate));
		}});
				
		
		// Bind the text property of the change_label to the difference between total and cashAmount_txtfield value
		change_label.textProperty().bind(
		    Bindings.createStringBinding(() -> {
		        String cashText = cashAmount_txtField.getText();
		        double cashAmount = 0.0;
		        
		        // Check if the text in cashAmount_txtfield is numeric
		        boolean cashIsNumeric = cashText.matches("[0-9]+");
		        
		        // If it's numeric, parse it to double
		        if (cashIsNumeric) {
		            cashAmount = Double.parseDouble(cashText);
		        }
		        
		        
		     // Get the total string from the booking_total label
		        String totalString = total_label.getText();
		        // Remove commas and currency symbols from the string
		        String cleanTotalString = totalString.replaceAll("[^\\d.]", "");
		        // Parse total as Double if it's not empty
		        Double total = cleanTotalString.isEmpty() ? 0.0 : Double.parseDouble(cleanTotalString);

		        // Check if the total is empty
		        if (cleanTotalString.isEmpty()) {
		            // If total is empty, set the visibility of the label to false
		            change_label.setVisible(false);
		        } else {
		            // If total is not empty, calculate the change
		            double change = total > 0 ? total - cashAmount : 0.00;

		            // Format the change as a string and return it
		            return String.format("%.2f", change);
		        }
				return null;

		    }, cashAmount_txtField.textProperty())
		);

		
    	
    }
    
}
