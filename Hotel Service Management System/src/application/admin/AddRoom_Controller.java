package application.admin;



import java.sql.SQLException;


import application.AlertMessage;
import application.Choices;
import application.DB_Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TextField;




public class AddRoom_Controller extends DB_Connection {

    //             ---------     ADD ROOM [ROOM MANAGEMENT]     --------
    
    @FXML
    private TextField rm_RoomNum;
    @FXML
    private ComboBox<String> rm_cbb_roomType;
    @FXML
    private ComboBox<String> rm_cbb_status;
    
 // Method to display the bed type options
    public void roomStatusChoices() {
        Choices roomStatus = new Choices(); 
        roomStatus.roomStatusChoices(rm_cbb_status);
    }
    
    
    public void roomTypeChoices (){    
    	try {
    		 connection = connect();
             String query = "SELECT Name FROM Room_Type";
             prepare = connection.prepareStatement(query);
             result = prepare.executeQuery(query);
             
             ObservableList<String> listData = FXCollections.observableArrayList();
            
             while (result.next()){       
            	 String cbb_item = result.getString("Name");
                 listData.addAll(cbb_item);
             }
             rm_cbb_roomType.setItems(listData);
             
        }
        catch (Exception e) {
			
			e.printStackTrace();
		}
    }

    
    // 
    public void addRoom() {
    	connection = connect();
    	
    	String insertData = "INSERT INTO room " 
				+ "(Room_No, Type_ID, Status) " 
				+ "VALUES (?,?,?)";
		
	    try {    	
	    	AlertMessage alert = new AlertMessage();
	    	// Check if there are empty fields
	    	if (rm_RoomNum.getText().isEmpty() 
	    			||rm_cbb_roomType.getSelectionModel().getSelectedItem() == null
	    			|| rm_cbb_status.getSelectionModel().getSelectedItem() == null
	    		) 	
	    		alert.errorMessage("Please fill up all details.");
	    	else {
	    		String checkRoomNum = "SELECT * FROM room WHERE room_no = ?";
	    		
	    		//statement = connection.createStatement();
    	        prepare = connection.prepareStatement(checkRoomNum);
    	        prepare.setString(1, rm_RoomNum.getText());
    	        result = prepare.executeQuery();
    	        
    	        if (result.next()) 
    	            alert.errorMessage(rm_RoomNum.getText() + " already exists.");
    	        else {
    	        	// Fetch the Type_ID corresponding to the selected room type
                    String roomTypeName = (String) rm_cbb_roomType.getSelectionModel().getSelectedItem();
                    int typeId = fetchRoomTypeId(roomTypeName); // Fetch the type ID from the database
                    
	    		        prepare = connection.prepareStatement(insertData);
	    		        prepare.setInt (1, Integer.parseInt(rm_RoomNum.getText())); 
	    				prepare.setInt (2, typeId); 
	    				prepare.setString (3, (String)rm_cbb_status.getSelectionModel().getSelectedItem()); 
	
	    		        prepare.executeUpdate();
	    		        alert.infoMessage("New room data has been added successfully!");
	    		        clearAddRoomFields();	
    	        }
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
    }
    
 // Method to fetch the Type_ID corresponding to the given room type name
    private int fetchRoomTypeId(String roomTypeName) throws SQLException {
        String query = "SELECT Type_ID FROM room_type WHERE Name = ?";
        prepare = connection.prepareStatement(query);
        prepare.setString(1, roomTypeName);
        result = prepare.executeQuery();
        if (result.next()) {
            return result.getInt("Type_ID");
        } else {
            throw new IllegalArgumentException("Room type not found: " + roomTypeName);
        }
    }
    
    
    public void clearAddRoomFields() {
    	rm_RoomNum.setText("");
    	rm_cbb_status.getSelectionModel().clearSelection();
    	rm_cbb_roomType.getSelectionModel().clearSelection();
    }
    
    
	
    public void initialize() {

    	roomStatusChoices();
    	roomTypeChoices();

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
