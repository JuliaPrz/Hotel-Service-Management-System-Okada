package application.employee;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import application.AlertMessage;
import application.Choices;
import application.DB_Connection;
import application.encapsulatedData.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;



public class CheckIn_Controller extends DB_Connection {
	

	//          ---------     ROOM TYPE     --------

    @FXML
    private AnchorPane rt_form;
    @FXML
    private ImageView rtImageView;
	@FXML
    private TextArea rt_amenities;
    @FXML
    private ComboBox<String> rt_bedType;
    @FXML
    private TextArea rt_description;
    @FXML
    private TextField rt_price;
    @FXML
    private TextField rt_typeID;
    @FXML
    private TextField rt_typeName;
	
    public class getData{
    	public static String path;
    }
    
    boolean priceIsNumeric = rt_price.getText().matches("[0-9]+");
    
    private Image image;
    // import image for room type
    public void rtInsertImage() {
    	FileChooser open = new FileChooser();
    	open .setTitle("Import Image");
    	open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));
    	
    	File file = open.showOpenDialog(rt_form.getScene().getWindow());
    	
    	if (file != null) {
    		
    		getData.path = file.getAbsolutePath();
    		image = new Image(file.toURI().toString(), 222, 130, false, true);
    		rtImageView.setImage(image);		
    	}
    }

  
    public void addRoomType() {
    	connection = connect();
    	
    	String insertData = "INSERT INTO room_type " 
				+ "(Type_ID, Name, Price_per_Night, Bed_Type, Description, Amenities, Image) " 
				+ "VALUES (?,?,?,?,?,?,?) ";
		
	    try {    	
	    	AlertMessage alert = new AlertMessage();
	    	// Check if there are empty fields
	    	if (rt_typeID.getText().isEmpty() 
	    			|| rt_typeName.getText().isEmpty() 
	    			|| rt_price.getText().isEmpty() 
	    			|| rt_bedType.getSelectionModel().getSelectedItem() == null
	    			|| rt_description.getText().isEmpty() 
	    			|| rt_amenities.getText().isEmpty() 
	    			|| getData.path == null || getData.path == "") 
	    	
	    		alert.errorMessage("Please fill up all details.");
	    	else if (!priceIsNumeric) 
	    		alert.errorMessage("Invalid price.");
	    	else {
	    		
	    		String checkTypeID = "SELECT * FROM room_type WHERE Type_ID = ?";
	    		
	    		//statement = connection.createStatement();
    	        prepare = connection.prepareStatement(checkTypeID);
    	        prepare.setString(1, rt_typeID.getText());
    	        result = prepare.executeQuery();
    	        
    	        if (result.next()) 
    	            alert.errorMessage(rt_typeID.getText() + " is already taken.");
    	        else {
	    	        	//statement = connection.createStatement();
	    		        prepare = connection.prepareStatement(insertData);
	    		        prepare.setInt (1, Integer.parseInt(rt_typeID.getText())); 
	    				prepare.setString (2, rt_typeName.getText()); 
	    				prepare.setDouble (3, Double.parseDouble(rt_price.getText())); 
	    				prepare.setString (4, (String)rt_bedType.getSelectionModel().getSelectedItem()); 
	    				prepare.setString (5, rt_description.getText()); 
	    				prepare.setString (6, rt_amenities.getText()); 
	
	    				String uri = getData.path;
	    				uri = uri.replace("\\", "\\\\");
	    				prepare.setString(7, uri);
	    		        
	    		        prepare.executeUpdate();
	    		        alert.infoMessage("New room type has been added successfully!");
	    		        clearRoomTypeFields();	
    	        }
	    	}
	    }
	    	
	    	catch (SQLException e) {
	    		AlertMessage alert = new AlertMessage();
	    	    // Check if the exception is due to data truncation
	    	    if (e.getSQLState().startsWith("22001")) {
	    	        // Data is too long, provide user feedback or handle it as needed
	    	    	 alert.errorMessage("Text is too long");
	    	    } else {
	    	        // Handle other SQL exceptions
	    	        e.printStackTrace(); // Print the stack trace for debugging
	    	    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
    }
	
    public void clearRoomTypeFields() {
    	rt_typeID.setText("");
    	rt_typeName.setText("");
    	rt_price.setText("");
    	rt_bedType.getSelectionModel().clearSelection();
    	rt_description.setText("");
    	rt_amenities.setText("");
    	getData.path = "";
    	
    	rtImageView.setImage(null);
    }
 
	
    public void initialize() {
    	
    	// wraps the content of text area
            rt_description.setWrapText(true);
        	rt_amenities.setWrapText(true);
        


    	
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
