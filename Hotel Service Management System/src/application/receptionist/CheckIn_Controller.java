package application.receptionist;

import java.sql.SQLException;
import java.time.LocalDate;

import application.AlertMessage;
import application.DB_Connection;
import application.logIn_signUp.SignUp_Controller.DeriveAge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private Label change_label;
    @FXML
    private TextField cardNum_txtField, nameOnCard_txtField, CCV_txtField, expiryDate_txtField;

 //   @FXML
    // private Button checkIn_Btn;
    
    
    
    
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
								  "FROM ROOM_TYPE;";
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
        
	    
	    String[] paymentOptions = {"CASH", "CARD"};
		ObservableList<String> options = FXCollections.observableArrayList(paymentOptions);
		paymentOption_cbb.setItems(options);
    	        
        	       String paymentOption = paymentOption_cbb.getValue();
        	       String promptText = paymentOption_cbb.getPromptText();

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
    	else {
    	
    	
    	
    	
    	
    	}
    	
    	
    	
    	
    	
  }
	
    
    
    
    
    public void clearFields() {

    }
	

 
	
    public void initialize() {
    	String[] paymentOptions = {"CASH", "CARD"};
		ObservableList<String> options = FXCollections.observableArrayList(paymentOptions);
		paymentOption_cbb.setItems(options);
    	        
        	       String paymentOption = paymentOption_cbb.getValue();
        	       String promptText = paymentOption_cbb.getPromptText();

        	  if (paymentOptions[0].equals(paymentOption) || paymentOptions[0].equals(promptText)) {
        	      cashGroup.setVisible(true); 
        	      cardGroup.setVisible(false); 
        	  } else {
        		  cardGroup.setVisible(true); 
        		  cashGroup.setVisible(false); 
        	  }



    	
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
