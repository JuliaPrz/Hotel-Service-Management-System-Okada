package application.receptionist;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import application.DB_Connection;
import application.roomData.Room;
import application.roomData.WalkIn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;




public class ReceptionistPage_Controller extends DB_Connection {
	
	//          ---------     SWITCH PAGE     --------
	@FXML
	private AnchorPane dashboard_page, room_page, booking_page, walkIn_page, transact_page, profile_page;
	@FXML
	private Button dashboard_btn, room_btn, booking_btn, walkIn_btn, transact_btn, profile_btn;
	
	// ROOM (MANAGEMENT) PAGE
    @FXML
    private TextField rmSearch;
    @FXML
    private TableView<Room> rmTable;
    @FXML
    private TableColumn<Room, Integer> rm_roomNum; 
    @FXML
    private TableColumn<Room, String> rm_roomType, rm_status;
    @FXML
    private TableColumn<Room, Double> rm_price;
    
    // WALK-IN PAGE
    
    @FXML
    private Button wSearch_Btn;
    @FXML
    private Label indicator_label;
    @FXML
    private Button check_Btn;
    @FXML
    private Button checkIn_Btn, checkOut_Btn;
    @FXML
    private DatePicker checkIn_datePicker, checkOut_datePicker;
    @FXML
    private Label availRoom_label;
    @FXML
    private TextField wSearch_txtField;
    
    @FXML
    private TableView<WalkIn> walkIn_table;
    @FXML
    private TableColumn<WalkIn, LocalDate> wArrival_col, wDeparture_col;
    @FXML
    private TableColumn<WalkIn, Integer> wRoomNum_col;
    @FXML
    private TableColumn<WalkIn, String> wRoomType_col, wGuest_col;
    

	
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
	        {
	        	
	        	room_page.toFront();
	            break;
	            
	        }
	        case "booking_btn":
	        	booking_page.toFront();
	            break; 
	        case "walkIn_btn":
	        {
	        	
	        	walkIn_page.toFront();
	            break;
	        }
	        	
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

	
	//             ---------     SHOW DATA IN THE TABLE     --------

    ObservableList<WalkIn> allWalkInList = FXCollections.observableArrayList(); 
    // UPDATES THE WALK-IN TABLE 
    TableCell<WalkIn, String> updateWalkInTable() {
    	walkIn_table.setFixedCellSize(50);
    	
    	String query = "SELECT r.Room_No, rt.Name AS RoomType_Name, CONCAT(g.First_Name, ' ' ,g.Last_Name) AS Guest_Name, t.Check_In_Date, t.Check_Out_Date "
    			+ "FROM Room AS r "
    			+ "JOIN Room_type AS rt ON r.Type_ID = rt.Type_ID "
    			+ "JOIN `Transaction` AS t ON r.Room_No = t.Room_No "
    			+ "JOIN Guest AS g ON t.Guest_ID = g.Guest_ID "
    			+ "WHERE g.Guest_Type = 'Booked Guest'"
    			+ "AND t.Check_In_Date <= CURDATE() " // Check if check_in_date is today or before today
    			+ "AND t.Check_Out_Date >= CURDATE(); -- Check if check_out_date is today;";
    
    	try {
    		connection = connect();
    	    prepare = connection.prepareStatement(query);
    	    result = prepare.executeQuery();

    	    while (result.next()) {

    	    	// Define a formatter for the desired date format
    	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

    	    	// Retrieve the check-in and check-out dates from the result set
    	        LocalDate checkInDate = result.getDate("Check_In_Date").toLocalDate();
    	        LocalDate checkOutDate = result.getDate("Check_Out_Date").toLocalDate();
    	        
    	        // Format the check-in and check-out dates
    	        String formattedArrivalDate = checkInDate.format(formatter);
    	        String formattedDepartureDate = checkOutDate.format(formatter);

    	    	allWalkInList.add(new WalkIn(
    	    	    result.getInt("Room_No"),
    	    	    result.getString("RoomType_Name"),
    	    	    result.getString("Guest_Name"),
    	    	    formattedArrivalDate,
    	    	    formattedDepartureDate
    	    	));
    	    }

    	    // Set the items to the table before applying cell factory
    	    walkIn_table.setItems(allWalkInList);
    	    walkIn_table.refresh();
    	   
    	    wRoomNum_col.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
    	    wRoomType_col.setCellValueFactory(new PropertyValueFactory<>("rtName"));
    	    wGuest_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuestName()));
    	    wArrival_col.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
    	    wDeparture_col.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
			

    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    	return null;
    }
	
	
	ObservableList<Room> allRoomList = FXCollections.observableArrayList(); 
    // UPDATES THE ROOM TABLE 
    TableCell<Room, String> updateRoomTable() {
    	rmTable.setFixedCellSize(50);
   // 	rmTable.getSelectionModel().clearSelection();
    	
    	String query = "SELECT room.Room_No, room_type.Name, room_type.Price_per_Night, room_type.Tax, room.`Status` "
    			+ "FROM room "
    			+ "INNER JOIN room_type ON room.Type_ID = room_type.Type_ID;";
    
    	try {
    		connection = connect();
    	    prepare = connection.prepareStatement(query);
    	    result = prepare.executeQuery();
    	    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 PHP");

    	    while (result.next()) {
    	    	
    	        double pricePerNight = result.getDouble("Price_per_Night") + result.getDouble("Tax");
    	        String formattedPrice = decimalFormat.format(pricePerNight);

    	        String status = result.getString("Status");
    	        allRoomList.add(new Room(
    	                result.getInt("Room_No"),
    	                result.getString("Name"),
    	                formattedPrice,
    	                status
    	        ));
    	    }

    	    
    	    // Set the items to the table before applying cell factory
    	    rmTable.setItems(allRoomList);
    	    rmTable.refresh();
    	        
    	 // Apply CSS class to the status column
    	   rm_status.setCellFactory(new Callback<TableColumn<Room, String>, TableCell<Room, String>>() {
    	        @Override
    	        public TableCell<Room, String> call(TableColumn<Room, String> param) {
    	            return new TableCell<Room, String>() {
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
    	  
    	    rm_roomNum.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
	    	rm_roomType.setCellValueFactory(new PropertyValueFactory<>("rtName"));
	    	rm_price.setCellValueFactory(new PropertyValueFactory<>("rtPrice"));
	    	rm_status.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
			

    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    	return null;
    }
	
	
	
	
    public void initialize() {

    	updateWalkInTable();
    	updateRoomTable();
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
