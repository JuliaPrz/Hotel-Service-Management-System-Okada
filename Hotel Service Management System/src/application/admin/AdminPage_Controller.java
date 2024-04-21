package application.admin;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import application.AlertMessage;
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



public class AdminPage_Controller extends DB_Connection {

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
    
    String[] typeOfBeds = {"1 Single Bed", "1 Double Bed", "1 Queen Size Bed", "1 King Size Bed", "2 Single Bed", "2 Double Bed"};
    
    public void bedTypeChoices() {
    	ObservableList<String> bedOptions = FXCollections.observableArrayList(typeOfBeds);
        rt_bedType.setItems(bedOptions);
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
	    		        
	    		        prepare.executeQuery();
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
	    	    	 alert.errorMessage("Either description or amenities\' text is too long");
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
    
    
    /*
    public ObservableList<RoomType> allRoomType(){
    	ObservableList<RoomType> listData = FXCollections.observableArrayList();
    	String query = "SELECT * FROM room_type " ;
    	connection = connect();
    	
    	try {
	        prepare = connection.prepareStatement(query);
	        result = prepare.executeQuery();
	        
	        RoomType roomType;
	        while(result.next()){
	        roomType = new RoomType(
	        		  result.getInt("rtID")
	        		 ,result.getString("rtName")
	        		 ,result.getDouble("rtPrice")
	        		 ,result.getString("bedType")
	        		 ,result.getString("rtDescription")
	        		 ,result.getString("rtAmenities")
	        		 ,result.getString("rtImage")
	        		 );
			listData.add(roomType);
		} 
    }
	    catch (Exception e) {
			e.printStackTrace();
		} return listData;
	}	*/

    
    
    //////////////// SHOW DATA IN THE TABLE 
    @FXML
    private TextField rmSearch;
    @FXML
    private TableColumn<?, ?> rm_actions;
    @FXML
    private TableColumn<?, ?> rm_price;
    @FXML
    private TableColumn<?, ?> rm_roomNum;
    @FXML
    private TableColumn<?, ?> rm_roomType;
    @FXML
    private TableColumn<?, ?> rm_status;
 
    
    
    
    
    
	
    public void initialize() {
    	
    	// wraps the content of text area
    	rt_description.setWrapText(true);
    	rt_amenities.setWrapText(true);
    	
    	bedTypeChoices();
    	
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
