package application.hotelCoord;

import java.sql.SQLException;
import java.text.DecimalFormat;

import application.DB_Connection;
import application.encapsulatedData.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;




public class HotelCoordPage_Controller extends DB_Connection {
	
	//          ---------     SWITCH PAGE     --------
	@FXML
	private AnchorPane dashboard_page, room_page, booking_page, transact_page, profile_page;
	@FXML
	private Button dashboard_btn, room_btn, booking_btn, transact_btn, profile_btn;
	
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
    
    TableCell<RoomType, String> updateRoomTable() {
    	rmTable.setFixedCellSize(50);
   // 	rmTable.getSelectionModel().clearSelection();
    	connection = connect();
    	String query = "SELECT room.Room_No, room_type.Name, room_type.Price_per_Night, room.`Status`\r\n"
    			+ "FROM room\r\n"
    			+ "INNER JOIN room_type ON room.Type_ID = room_type.Type_ID;";
    
    	try {
    	    prepare = connection.prepareStatement(query);
    	    result = prepare.executeQuery();
    	    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 PHP");

    	    while (result.next()) {
    	        double pricePerNight = result.getDouble("Price_per_Night");
    	        String formattedPrice = decimalFormat.format(pricePerNight);

    	        String status = result.getString("Status");
    	        allRoomList.add(new RoomType(
    	                result.getInt("Room_No"),
    	                result.getString("Name"),
    	                formattedPrice,
    	                status
    	        ));
    	    }

    	    // Set the items to the table before applying cell factory
    	    rmTable.setItems(allRoomList);
    	    
    	 // Apply CSS class to the status column
    	    rm_status.setCellFactory(new Callback<TableColumn<RoomType, String>, TableCell<RoomType, String>>() {
    	        @Override
    	        public TableCell<RoomType, String> call(TableColumn<RoomType, String> param) {
    	            return new TableCell<RoomType, String>() {
    	                @Override
    	                protected void updateItem(String item, boolean empty) {
    	                    super.updateItem(item, empty);
    	                    setText(item);
    	                    if (!empty) {
    	                        String status = getTableView().getItems().get(getIndex()).getRoomStatus();

    	                        // Determine the CSS class based on the status
    	                        String statusCssClass = "cell-status-default";
    	                        if (status.equals("Available")) {
    	                            statusCssClass = "cell-status-available";
    	                        } else if (status.equalsIgnoreCase("Occupied")) {
    	                            statusCssClass = "cell-status-not-available";
    	                        }

    	                        getStyleClass().clear(); // Clear existing style classes
    	                        getStyleClass().add(statusCssClass); // Apply the CSS class
    	                        getStyleClass().add("status_column");
    	                       
    	                    
    	                    }
    	                }
    	            };
    	        }
    	    });

    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
			rm_roomNum.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
	    	rm_roomType.setCellValueFactory(new PropertyValueFactory<>("rtName"));
	    	rm_price.setCellValueFactory(new PropertyValueFactory<>("rtPrice"));
	    	rm_status.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
			return null;
	    	
    
		
    }
    
    
   
    
	
    public void initialize() {

    	updateRoomTable();
    	
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
