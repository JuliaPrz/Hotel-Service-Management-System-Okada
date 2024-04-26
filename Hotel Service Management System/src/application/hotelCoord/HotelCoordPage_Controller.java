package application.hotelCoord;

import java.sql.SQLException;

import application.DB_Connection;
import application.encapsulatedData.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;




public class HotelCoordPage_Controller extends DB_Connection {
	
	//          ---------     SWITCH PAGE     --------
	@FXML
	private AnchorPane dashboard_page, room_page, booking_page, food_page, hk_page, transact_page, profile_page;
	@FXML
	private Button dashboard_btn, room_btn, booking_btn, food_btn, hk_btn, transact_btn, profile_btn;
	
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
	        case "booking_btn":
	        	booking_page.toFront();
	            break;
	        case "food_btn":
	        	food_page.toFront();
	            break;
	        case "hk_btn":
	        	hk_page.toFront();
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

    
    //             ---------     SHOW DATA IN THE TABLE [ROOM MANAGEMENT]     --------
    @FXML
    private TextField rmSearch;
    @FXML
    private TableView<RoomType> rmTable;
    @FXML
    private TableColumn<RoomType, Integer> rm_roomNum; 
    @FXML
    private TableColumn<RoomType, String> rm_roomType, rm_status;
    @FXML
    private TableColumn<RoomType, Double> rm_price;
    @FXML
    private TableColumn<RoomType, Button> rm_actions;

    ObservableList<RoomType> allRoomList = FXCollections.observableArrayList(); 
    
    void updateRoomTable() {
    	rmTable.setFixedCellSize(50);
   // 	rmTable.getSelectionModel().clearSelection();
    	connection = connect();
    	String query = "SELECT room.Room_No, room_type.Type_ID, room_type.Price_per_Night, room.`Status`\r\n"
    			+ "FROM room\r\n"
    			+ "INNER JOIN room_type ON room.Type_ID = room_type.Type_ID;";
    
    	try {
			prepare = connection.prepareStatement(query);
			result = prepare.executeQuery();
			
			while(result.next()) {
				allRoomList.add(new RoomType(
						result.getInt("Room_No"),
						result.getInt("Type_ID"),
						result.getDouble("Price_per_Night"),
						result.getString("Status")
					));
				rmTable.setItems(allRoomList);
			}
		//	prepare.close();
		//	result.close();
			
			rm_roomNum.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
	    	rm_roomType.setCellValueFactory(new PropertyValueFactory<>("rtID"));
	    	rm_price.setCellValueFactory(new PropertyValueFactory<>("rtPrice"));
	    	rm_status.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
	    	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
    
    
    
    
	
    public void initialize() {

    	updateRoomTable();
    	
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
