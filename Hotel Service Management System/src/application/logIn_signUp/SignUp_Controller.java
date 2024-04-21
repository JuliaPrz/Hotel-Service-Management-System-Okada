package application.logIn_signUp;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

import application.AlertMessage;
import application.DB_Connection;

import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUp_Controller extends DB_Connection implements Initializable  {

	@FXML
    private TextField signUpEmail;
    @FXML
    private PasswordField signUpPass;
    @FXML
    private PasswordField ConfirmPass;
    @FXML
    private TextField signUpFName;
    @FXML
    private TextField signUpLName;
    @FXML
    private DatePicker signUp_bdate;
    @FXML
    private TextField signUpContact;
    @FXML
    private Button logInLink;
    @FXML
    private Button signUp_button;
     
    // Class to calculate the age based on birthdate given
    class DeriveAge {
        public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
           if ((birthDate != null) && (currentDate != null)) {
                    return Period.between(birthDate, currentDate).getYears();
            } else {
                    return 0;
                }
            }
    }
    
    // confirms if the email is a valid email by verifying its format
    // this is regular expression for email validation provided by the RFC (Request for comments) standards.
    // A Request for Comments (RFC) is a formal document from the Internet Engineering Task Force (IETF) that contains specifications and organizational notes about topics related to the Internet and computer networking
    public static boolean isEmailValid(TextField email) 
    { 
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        
        Pattern pat = Pattern.compile(regexPattern); 
        if (email == null) 
            return false; 
        return  pat.matcher(email.getText()).matches();
    }
    
     
    public void signUp() {
    	connection = connect();
    	
    	AlertMessage alert = new AlertMessage();
    	
    	// used to verify the length of the phone number; continues in the if else statement
    	String signUp_contact = signUpContact.getText();
    	String firstTwoNum = null;
    	if (signUp_contact.length() >= 2) 
    		firstTwoNum = signUp_contact.substring(0, 2);
    	
    	// used to find a pattern if the phone number is numeric numbers only; continues in the if else statement
		boolean phoneIsNumeric = signUp_contact.matches("[0-9]+");
    	
    	// Calculate age using DeriveAge
        LocalDate currentDate = LocalDate.now();
        LocalDate birthdate = signUp_bdate.getValue();
        int age = DeriveAge.calculateAge(birthdate, currentDate);
    	
    	// Check if there are empty fields
    	if (signUpEmail.getText().isEmpty() 
    			|| signUpPass.getText().isEmpty() 
    			|| ConfirmPass.getText().isEmpty() 
    			|| signUpFName.getText().isEmpty() 
    			|| signUpLName.getText().isEmpty() 
    			|| signUp_bdate.getValue() == null 
    			|| signUpContact.getText().isEmpty()) 
    	
    		alert.errorMessage("Please fill up all details.");
    	
    	// verifies the email format
    	else if (isEmailValid(signUpEmail) == false ) 
    		alert.errorMessage("Invalid email format.");
    	
    	// check if the value of pass is similar to confirm pass
    	else if (!signUpPass.getText().equals(ConfirmPass.getText()))  
    		alert.errorMessage("Password does not match.");
    	
    	// check the length of the password
    	else if (signUpPass.getText().length() < 8) 
    		alert.errorMessage("Invalid password, at least 8 characters needed");
    	
    	// makes sure that the guest is of legal age
    	else if (age < 18 ) 
    		alert.errorMessage("You must be 18 years old and above to create an account.");
    	
    	// checks the length of the contact number
    	else if (signUp_contact.length() <11 || signUp_contact.length() > 11) 
    		alert.errorMessage("Invalid phone number.");
    	
    	// checks if the phone number starts with 09 or if phone is not numeric
    	else if (!firstTwoNum.equals("09") || !phoneIsNumeric) 
    			 alert.errorMessage("Invalid phone number.");
    	
    	// check if the email is already taken
    	else {
    	   
    		String checkEmail = "SELECT * FROM guest WHERE Email = ?";
    		
    	    try {
    	    	//statement = connection.createStatement();
    	        prepare = connection.prepareStatement(checkEmail);
    	        prepare.setString(1, signUpEmail.getText());
    	        result = prepare.executeQuery();
    	        
    	        if (result.next()) {
    	            alert.errorMessage(signUpEmail.getText() + " is already taken.");
    			} else {
    				// capitalizes first letter of the name
	    			String firstName = signUpFName.getText();
	    			String lastName = signUpLName.getText();
	    			if (!firstName.isEmpty() && !lastName.isEmpty()) {
	    			    firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
	    			    lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	    			}
    				
	    			String insertData = "INSERT INTO guest " 
	    						+ "(Last_Name, First_Name, Email, Password, Birthdate, Age, Contact_No) " 
	    						+ "VALUES (?,?,?,?,?,?,?) ";

	    			prepare = connection.prepareStatement (insertData); 
	    			prepare.setString (1, lastName); 
	    			prepare.setString (2, firstName); 
	    			prepare.setString (3, signUpEmail.getText()); 
	    			prepare.setString (4, signUpPass.getText());
	    			 // Convert LocalDate to java.sql.Date
	    		    Date bdate = Date.valueOf(birthdate);
	    			prepare.setDate (5, bdate);
	    			prepare.setInt (6, age);
	    			prepare.setString (7, signUpContact.getText());
	    			
	    			prepare.executeUpdate();
	    	
	    			
	    			alert.infoMessage("Signed In Successfully!");
	    			clearSignUpFields();
    			}
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		finally {
    			close();
    		}
    	}	
    } 

    public void clearSignUpFields() {
    	signUpLName.setText("");
    	signUpFName.setText("");
    	signUpEmail.setText("");
    	signUpPass.setText("");
    	ConfirmPass.setText("");
    	signUp_bdate.setValue(null);
    	signUpContact.setText("");
    }
    
    // this method is applied to the log in link
    public void openLogInPage (ActionEvent event) throws IOException {
    		 // Load the FXML file of log in page
            Parent root = FXMLLoader.load(getClass().getResource("/application/logIn_signUp/Log In Page.fxml"));
			
			// Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene in the stage
            stage.setScene(scene);
            stage.show();	
    }
    
    
    
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// Restricts the available date in birthdate datepicker
		LocalDate minDate = LocalDate.of(1930, 1, 1);
		LocalDate maxDate = LocalDate.now();
		signUp_bdate.setDayCellFactory(d -> new DateCell() {
			@Override 
		 	public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
		                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
		               }});
		

	}

}
