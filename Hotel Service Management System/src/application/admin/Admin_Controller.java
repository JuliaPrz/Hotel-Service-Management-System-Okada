package application.admin;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Pattern;

import application.AlertMessage;
import application.DB_Connection;
import application.guest.GuestPage_Controller;
import application.logIn_signUp.LogIn_Controller;
import application.logIn_signUp.SignUp_Controller.DeriveAge;
import application.tableData.Booked;
import application.tableData.Receptionist;
import application.tableData.WalkIn;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class Admin_Controller extends DB_Connection{

		@FXML
	    private TextField fName_txtField, lName_txtField;
	    @FXML
	    private DatePicker bdate_datePicker;
	    @FXML
	    private TextField contact_txtField;
	    @FXML
	    private TextField email_txtField;
	    @FXML
	    private PasswordField pass_field;

	    @FXML
	    private Button clear_btn, save_btn, update_btn, delete_btn, report_btn;

	    @FXML
	    private TableView<Receptionist> receptionist_table;
	    @FXML
	    private TableColumn<Receptionist, Integer> empID_col;
	    @FXML
	    private TableColumn<Receptionist, String> name_col;
	    @FXML
	    private TableColumn<Receptionist, String> bdate_col;
	    @FXML
	    private TableColumn<Receptionist, String> contact_col;

	    AlertMessage alert = new AlertMessage();
	
	 // UPDATES THE TABLE IN THE BOOKING PAGE
		TableCell<Receptionist, String> updateEmployeeTable() {
			
		ObservableList<Receptionist> allReceptionistList = FXCollections.observableArrayList(); 
		receptionist_table.setFixedCellSize(50);
		
		String query = "SELECT Employee_ID, CONCAT(First_Name, ' ', Last_Name) AS `Name`, Birthdate, Contact_No " +
	             "FROM RECEPTIONIST;";
		try {
		
		 prepare = connection.prepareStatement(query);
		 result = prepare.executeQuery();
		
		 while (result.next()) {
		
		 	// Define a formatter for the desired date format
		 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
		
		 	// Retrieve the check-in and check-out dates from the result set
		     LocalDate bdate = result.getDate("Birthdate").toLocalDate();
		     String formattedBDate = bdate.format(formatter); // Format the date
		 		
		     allReceptionistList.add(new Receptionist(
		 	    result.getInt("Employee_ID"),
		 	    result.getString("Name"),
		 	    formattedBDate,
		 	   result.getString("Contact_No")
		 	));
		 }
		
		 // Set the items to the table before applying cell factory
		 receptionist_table.setItems(allReceptionistList);
		 receptionist_table.refresh();
		
		 empID_col.setCellValueFactory(new PropertyValueFactory<>("empID"));
		 name_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		 bdate_col.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
		 contact_col.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	    
		public static boolean isEmailValid(TextField email) 
	    { 
	        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
	        
	        Pattern pat = Pattern.compile(regexPattern); 
	        if (email == null || email.getText().isEmpty()) 
	            return false; 
	        return  pat.matcher(email.getText()).matches();
	    }
	    
		// INSERT DATA TO RECEPTIONIST TABLE
		@FXML
	    void saveBtnAction(ActionEvent event) {
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
	    	
	    	
	     // Check if all required fields are filled
			if (fName_txtField.getText().isEmpty() 
			        || lName_txtField.getText().isEmpty() 
			        || birthdate == null 
			        || contact_txtField.getText().isEmpty()
					|| email_txtField.getText().isEmpty() 
					|| pass_field.getText().isEmpty()){
				alert.errorMessage("Please fill up all details.");
			} else if (!fNameOnlyLetters || !lNameOnlyLetters || fName_txtField.getText().length() > 30 || lName_txtField.getText().length() > 20) {
			    alert.errorMessage("Invalid name.");
			}else if (isEmailValid(email_txtField) == false ) { // Check if email format is valid
		    		alert.errorMessage("Invalid email format.");
			} else if (age < 18) {
			    alert.errorMessage("You must be 18 years old and above to create an account.");
			} else if (contact.length() != 11 || !firstTwoNum.equals("09") || !phoneIsNumeric) {
			    alert.errorMessage("Invalid phone number.");
			} else {
				try {
	        		// capitalizes first letter of the name
	    			String firstName = fName_txtField.getText();
	    			String lastName = lName_txtField.getText();
	    			firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
	    			lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	    				        		
	    			String insertReceptionistData = "INSERT INTO RECEPTIONIST " 
	    						+ "(Last_Name, First_Name, Email, Password, Contact_No ,Birthdate, Age) " 
	    						+ "VALUES (?,?,?,?,?,?,?) ";
	    			
	    				prepare = connection.prepareStatement (insertReceptionistData); 
	    				prepare.setString (1, lastName); 
	    				prepare.setString (2, firstName); 
	    				prepare.setString (3, email_txtField.getText()); 
	    				prepare.setString (4, pass_field.getText()); 
	    				prepare.setString (5, contact);
	    			    Date bdate = Date.valueOf(birthdate); // Convert LocalDate to java.sql.Date
	    				prepare.setDate (6, bdate);
	    				prepare.setInt (7, age);
	    				prepare.executeUpdate();
	    				
	    				alert.infoMessage("New receptionist has been added successfully!");
 		            	updateEmployeeTable();
 		            	clearFields();
 		            	
				} catch (SQLException e) {
    	            e.printStackTrace();
				}
			}
	    }
	    
	 // DELETE DATA TO RECEPTIONIST TABLE
	    @FXML
	    void deleteBtnAction(ActionEvent event) {
	    	if (receptionist_table.getSelectionModel().getSelectedItem() != null) {
		    	int selectedData = receptionist_table.getSelectionModel().getSelectedIndex();
		    	int receptID = Integer.parseInt(String.valueOf(receptionist_table.getItems().get(selectedData).getEmpID()));
		    	try {
		    		connection = connect();
	    				// Confirm if the admin wants to delete the selected data
	    			    Optional<ButtonType> result = alert.confirmationMessage("Are you sure you want to delete the selected data?");
	
	    			    // Check if the user clicked "Yes"
	    			    if (result.isPresent() && result.get() == ButtonType.OK) {
	    			    	String deleteReceptionistData = "DELETE FROM RECEPTIONIST WHERE Employee_ID = ? ";
	        				prepare = connection.prepareStatement (deleteReceptionistData); 
	        				prepare.setInt (1, receptID); 
	        				prepare.execute();
	    			        alert.infoMessage("Data deleted successfully.");
	    			        updateEmployeeTable();
	    			    
	    			    }
			  } catch (SQLException e) {
		            e.printStackTrace();
				}
		    } else 
		    	alert.infoMessage("Please select a row to delete.");
	    }
	    
	 // UPDATE DATA TO RECEPTIONIST TABLE
	    @FXML
	    void updateBtnAction(ActionEvent event) {
	    	// Check if a row is selected in the table
	        if (receptionist_table.getSelectionModel().getSelectedItem() != null) {
		    	int selectedData = receptionist_table.getSelectionModel().getSelectedIndex();
		    	int receptID = Integer.parseInt(String.valueOf(receptionist_table.getItems().get(selectedData).getEmpID()));
		    	
		    	try {
		    		connection = connect();
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
			    	
			    	
			     // Check if all required fields are filled
					if (fName_txtField.getText().isEmpty() 
					        || lName_txtField.getText().isEmpty() 
					        || birthdate == null 
					        || contact_txtField.getText().isEmpty()
							|| email_txtField.getText().isEmpty() 
							|| pass_field.getText().isEmpty()){
						alert.errorMessage("Please fill up all details.");
					} else if (!fNameOnlyLetters || !lNameOnlyLetters || fName_txtField.getText().length() > 30 || lName_txtField.getText().length() > 20) {
					    alert.errorMessage("Invalid name.");
					}else if (isEmailValid(email_txtField) == false ) { // Check if email format is valid
				    		alert.errorMessage("Invalid email format.");
					} else if (age < 18) {
					    alert.errorMessage("You must be 18 years old and above to create an account.");
					} else if (contact.length() != 11 || !firstTwoNum.equals("09") || !phoneIsNumeric) {
					    alert.errorMessage("Invalid phone number.");
					} else {
						
						// capitalizes first letter of the name
		    			String firstName = fName_txtField.getText();
		    			String lastName = lName_txtField.getText();
		    			firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
		    			lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
						
						String updateReceptionistData = "UPDATE RECEPTIONIST "
													  + "SET Last_name = ?, First_Name = ?, Email = ?, Password = ?, Contact_No = ?, Birthdate = ?, Age = ? "
													  + "WHERE Employee_ID = ?";
						prepare = connection.prepareStatement (updateReceptionistData); 
	    				prepare.setString (1, lastName); 
	    				prepare.setString (2, firstName); 
	    				prepare.setString (3, email_txtField.getText()); 
	    				prepare.setString (4, pass_field.getText()); 
	    				prepare.setString (5, contact);
	    			    Date bdate = Date.valueOf(birthdate); // Convert LocalDate to java.sql.Date
	    				prepare.setDate (6, bdate);
	    				prepare.setInt (7, age);
	    				prepare.setInt (8, receptID);
	    				prepare.executeUpdate();
	    				
	    				alert.infoMessage(firstName + "'s" + " data has been updated successfully!");
			            	updateEmployeeTable();
			            	clearFields();
					}
			  } catch (SQLException e) {
		            e.printStackTrace();
				}
		    }else alert.infoMessage("Please select a row to update."); 
	        
	    }
	   
	    public void clearFields() {
	    	fName_txtField.setText("");
	    	lName_txtField.setText("");
	    	bdate_datePicker.setValue(null);
	    	contact_txtField.setText("");
	    	email_txtField.setText("");
	    	pass_field.setText("");
	    }
	    
    public void initialize() {
    	
    	// Restricts the available date in birthdate datepicker
    			LocalDate minDate = LocalDate.of(1930, 1, 1);
    			LocalDate maxDate = LocalDate.now();
    			bdate_datePicker.setDayCellFactory(d -> new DateCell() {
    				@Override 
    			 	public void updateItem(LocalDate item, boolean empty) {
    							super.updateItem(item, empty);
    			                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
    			               }});
    	connection = connect();
    	 updateEmployeeTable();

    	
   	}
    
    
    
    
}
