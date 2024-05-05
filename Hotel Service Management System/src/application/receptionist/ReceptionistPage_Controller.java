package application.receptionist;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import application.AlertMessage;
import application.DB_Connection;
import application.roomData.Booked;
import application.roomData.Room;
import application.roomData.WalkIn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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
    
    // BOOKING PAGE
    
    @FXML
    private Label incomingNum_label, todayNum_Label, totalBooking_label;
    @FXML
    private Button bookingCheckOut_Btn;
    @FXML
    private TextField bookingSearch_TxtField;

    @FXML
    private TableView<Booked> booking_table;
    @FXML
    private TableColumn<Booked, Integer> bTransaction_col;
    @FXML
    private TableColumn<Booked, String> bGuestName_col;
    @FXML
    private TableColumn<Booked, String> bArrivalDate_col, bDepartureDate_col;
    @FXML
    private TableColumn<Booked, Integer> bRoomNo_col;

    // WALK-IN PAGE
    
    @FXML
    private Button check_Btn;
    @FXML
    private Button checkIn_Btn, checkOut_Btn;
    @FXML
    private DatePicker checkIn_datePicker, checkOut_datePicker;
    @FXML 
    private ComboBox<String> roomType_cbb;
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
    
    // ROOM (MANAGEMENT) PAGE
    @FXML
    private TextField rmSearch_txtField;
    @FXML
    private TableView<Room> rmTable;
    @FXML
    private TableColumn<Room, Integer> rm_roomNum; 
    @FXML
    private TableColumn<Room, String> rm_roomType, rm_status;
    @FXML
    private TableColumn<Room, Double> rm_price;
    
	
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
	        case "walkIn_btn":	
	        	walkIn_page.toFront();
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

	
	//             ---------     SHOW DATA IN THE TABLE     --------
	
	ObservableList<Booked> allBookedList = FXCollections.observableArrayList(); 
    // UPDATES THE TABLE IN THE BOOKING PAGE
    TableCell<Booked, String> updateBookedTable() {
    	booking_table.setFixedCellSize(50);
    	
    	String query = "SELECT t.Transaction_ID, CONCAT(g.First_Name, ' ' ,g.Last_Name) AS Guest_Name, t.Check_In_Date, t.Check_Out_Date, t.Room_No "
    			+ "FROM `TRANSACTION` AS t "
    			+ "JOIN GUEST AS g ON g.Guest_ID = t.Guest_ID "
    			+ "WHERE g.Guest_Type = 'Booked Guest' "
    			+ "AND t.Check_Out_Date > CURRENT_DATE()" // does not include the guests which already departed
    			+ "ORDER BY t.Transaction_ID ASC ";
    		//	+ "AND t.Check_In_Date <= CURDATE() " // Check if check_in_date is today or before today
    		//	+ "AND t.Check_Out_Date >= CURDATE();"; // Check if check_out_date is today
    	try {
    		//connection = connect();
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

    	    	allBookedList.add(new Booked(
    	    	    result.getInt("Transaction_ID"),
    	    	    result.getString("Guest_Name"),
    	    	    formattedArrivalDate,
    	    	    formattedDepartureDate,
    	    	    result.getInt("Room_No")
    	    	));
    	    }

    	    // Set the items to the table before applying cell factory
    	    booking_table.setItems(allBookedList);
    	    booking_table.refresh();
    	   
    	    bTransaction_col.setCellValueFactory(new PropertyValueFactory<>("transactNum"));
    	    bGuestName_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getgName()));
    	    bArrivalDate_col.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
    	    bDepartureDate_col.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
    	    bRoomNo_col.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
			

    	 // SEARCH FILTER
    	    FilteredList<Booked> filteredData = new FilteredList<>(allBookedList, b -> true);
    	    bookingSearch_TxtField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    	filteredData.setPredicate(booked -> {
    	    		 if (newValue.isEmpty() || newValue.isBlank()) {
    	    	            // If the search text is empty, display all items
    	    	            return true;
    	    	        }

    	    	        // Convert the search text to lowercase for case-insensitive search
    	    	        String searchKeyword = newValue.toLowerCase();

    	    	     // Check if any of the properties contain the search text
    	    	        return String.valueOf(booked.getTransactNum()).contains(searchKeyword) ||
    	    	               booked.getgName().toLowerCase().contains(searchKeyword) ||
    	    	               booked.getArrivalDate().toString().toLowerCase().contains(searchKeyword) ||
    	    	               booked.getDepartureDate().toString().toLowerCase().contains(searchKeyword) ||
    	    	               String.valueOf(booked.getRoomNum()).contains(searchKeyword);
    	    	});
    	    });
    	    
    	    SortedList<Booked> sortedData = new SortedList<>(filteredData);
    	    sortedData.comparatorProperty().bind(booking_table.comparatorProperty());
    	    booking_table.setItems(sortedData);
    	    
    	    
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    	return null;
    }
	
    ObservableList<WalkIn> allWalkInList = FXCollections.observableArrayList(); 
    // UPDATES THE TABLE IN THE WALK-IN PAGE
    TableCell<WalkIn, String> updateWalkInTable() {
    	walkIn_table.setFixedCellSize(50);
    	
    	String query = "SELECT r.Room_No, rt.Name AS RoomType_Name, CONCAT(g.First_Name, ' ' ,g.Last_Name) AS Guest_Name, t.Check_In_Date, t.Check_Out_Date "
    			+ "FROM Room AS r "
    			+ "JOIN Room_type AS rt ON r.Type_ID = rt.Type_ID "
    			+ "JOIN `Transaction` AS t ON r.Room_No = t.Room_No "
    			+ "JOIN Guest AS g ON t.Guest_ID = g.Guest_ID "
    			+ "WHERE g.Guest_Type = 'Walk-In Guest'"
    			+ "AND t.Check_In_Date <= CURDATE() " // Check if check_in_date is today or before today
    			+ "AND t.Check_Out_Date >= CURDATE();"; // Check if check_out_date is today
    
    	try {
    	//	connection = connect();
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
			
    	    
    	    // SEARCH FILTER
    	    FilteredList<WalkIn> filteredData = new FilteredList<>(allWalkInList, b -> true);
    	    wSearch_txtField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    	filteredData.setPredicate(walkIn -> {
    	    		 if (newValue.isEmpty() || newValue.isBlank()) {
    	    	            // If the search text is empty, display all items
    	    	            return true;
    	    	        }

    	    	        // Convert the search text to lowercase for case-insensitive search
    	    	        String searchKeyword = newValue.toLowerCase();

    	    	        // Check if any of the properties contain the search text
    	    	        return String.valueOf(walkIn.getRoomNum()).contains(searchKeyword) ||
    	    	               walkIn.getRtName().toLowerCase().contains(searchKeyword) ||
    	    	               walkIn.getGuestName().toLowerCase().contains(searchKeyword) ||
    	    	               walkIn.getArrivalDate().toString().toLowerCase().contains(searchKeyword) ||
    	    	               walkIn.getDepartureDate().toString().toLowerCase().contains(searchKeyword);
    	    	});
    	    });
    	    
    	    SortedList<WalkIn> sortedData = new SortedList<>(filteredData);
    	    sortedData.comparatorProperty().bind(walkIn_table.comparatorProperty());
    	    walkIn_table.setItems(sortedData);
    	    

    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    	return null;
    }
	
	ObservableList<Room> allRoomList = FXCollections.observableArrayList(); 
    // UPDATES THE TABLE IN THE ROOM PAGE
    TableCell<Room, String> updateRoomTable() {
    	rmTable.setFixedCellSize(50);
   // 	rmTable.getSelectionModel().clearSelection();
    	
    	String query = "SELECT room.Room_No, room_type.Name, room_type.Price_per_Night, room_type.Tax, room.`Status` "
    			+ "FROM room "
    			+ "INNER JOIN room_type ON room.Type_ID = room_type.Type_ID;";
    
    	try {
    	//	connection = connect();
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
			

	    	// SEARCH FILTER
    	    FilteredList<Room> filteredData = new FilteredList<>(allRoomList, b -> true);
    	    rmSearch_txtField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    	filteredData.setPredicate(room -> {
    	    		 if (newValue.isEmpty() || newValue.isBlank()) {
    	    	            // If the search text is empty, display all items
    	    	            return true;
    	    	        }

    	    	        // Convert the search text to lowercase for case-insensitive search
    	    	        String searchKeyword = newValue.toLowerCase();

    	    	     // Check if any of the properties contain the search text
    	    	        return String.valueOf(room.getRoomNum()).contains(searchKeyword) ||
    	    	               room.getRtName().toLowerCase().contains(searchKeyword) ||
    	    	               String.valueOf(room.getRtPrice()).contains(searchKeyword) ||
    	    	               room.getRoomStatus().toLowerCase().contains(searchKeyword);
    	    	});
    	    });
    	    
    	    SortedList<Room> sortedData = new SortedList<>(filteredData);
    	    sortedData.comparatorProperty().bind(rmTable.comparatorProperty());
    	    rmTable.setItems(sortedData);
	    	
	    	
	    	
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    	return null;
    }
	
	
	
    
    
    /*				TO BE CONTINUEDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
//  ---------     BOOKING TAB COMPONENT ACTIONS    --------
    void bookingController() {
    	// update the table
    	updateBookedTable();
    	// query to count all of the available rooms today
    	String countAvailRoomQuery = "SELECT COUNT(r.Room_No) AS NumOfAvailRooms " +
                "FROM room_type AS rt " +
                "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " +
                "WHERE r.Room_no NOT IN ( " +
                "    SELECT Room_no " +
                "    FROM `transaction` " +
                "    WHERE check_in_date <= CURDATE() " +
                "    AND check_out_date >= CURDATE() " +
                ")";
    
    	try {
    	    prepare = connection.prepareStatement(countAvailRoomQuery);
    	    result = prepare.executeQuery();
    	    
    	    // AVAILABLE ROOMS TODAY
    	 // Move cursor to the first row of the result set
    	    if (result.next()) {
    	        // Retrieve the value of NumOfAvailRooms from the result set
    	        int totalAvailRooms = result.getInt("NumOfAvailRooms");
    	        // Set the text of the availRoom_label using the retrieved value
    	        availRoom_label.setText(String.valueOf(totalAvailRooms));
    	    }
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    }
    
    */
    
    
//                 ---------     WALK-IN TAB COMPONENT ACTIONS    --------
    
    void walkInController() {
    	updateWalkInTable(); // update the table
    	
    	// query to count all of the available rooms today
    	String countAvailRoomQuery = "SELECT COUNT(r.Room_No) AS NumOfAvailRooms " +
                "FROM room_type AS rt " +
                "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " +
                "WHERE r.Room_no NOT IN ( " +
                "    SELECT Room_no " +
                "    FROM `transaction` " +
                "    WHERE check_in_date <= CURDATE() " +
                "    AND check_out_date >= CURDATE() " +
                ")";
    
    	try {
    	    prepare = connection.prepareStatement(countAvailRoomQuery);
    	    result = prepare.executeQuery();
    	    
    	    // AVAILABLE ROOMS TODAY
    	 // Move cursor to the first row of the result set
    	    if (result.next()) {
    	        // Retrieve the value of NumOfAvailRooms from the result set
    	        int totalAvailRooms = result.getInt("NumOfAvailRooms");
    	        // Set the text of the availRoom_label using the retrieved value
    	        availRoom_label.setText(String.valueOf(totalAvailRooms));
    	    }
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    	    
    	    // DATEPICKERS
    	 // Restricts the available date to check in to the current date and 5 years from now
    		LocalDate minDate = LocalDate.now();
    		LocalDate maxDate = LocalDate.now().plusYears(5);
    		checkIn_datePicker.setDayCellFactory(d -> new DateCell() {
    			@Override 
    		 	public void updateItem(LocalDate item, boolean empty) {
    						super.updateItem(item, empty);
    		                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
    		               }});
    		checkOut_datePicker.setDayCellFactory(d -> new DateCell() {
    			@Override 
    		 	public void updateItem(LocalDate item, boolean empty) {
    						super.updateItem(item, empty);
    		                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
    		               }});
    	    
    	    // COMBOBOX
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

    		    // Process the result of the second query
    		} catch (SQLException e) {
        	    e.printStackTrace();
        	 
        	}	
    }
    @FXML
    void checkBtnAction (ActionEvent event) {
    	AlertMessage alert = new AlertMessage();
    	
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
	    	
	    	String checkQuery = "SELECT r.Room_No AS NumOfRooms " +
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
	    		
				  ArrayList<String> availableRooms = new ArrayList<>(); // List to store available room numbers

				    while (result.next()) {
				        String roomNumber = result.getString("NumOfRooms");
				        availableRooms.add(roomNumber); // Add room number to the list
				    }

				 // Get the number of available rooms
				    int numOfAvailableRooms = availableRooms.size();
				    if (numOfAvailableRooms > 0) { 		// Check if there are available rooms
				    	 // If there is more than one available room, join the room numbers with a comma
				        String roomNumbersMessage = String.join(", ", availableRooms);
				        alert.infoMessage("Available rooms: " + roomNumbersMessage);
				    } else if (numOfAvailableRooms == 1) { 
				        alert.infoMessage("Available rooms: " + availableRooms);
				    } else {
				        // If there are no available rooms, display a message indicating so
				        alert.infoMessage("No available rooms.");
				    }

	    	}catch (SQLException e) {
	    		e.printStackTrace();
	    	}
           
	    	
	    	
	    }
    	
    }
    
    
    
    
	
    public void initialize() {

    	connection = connect();
    	
    	walkInController();
    	//bookingController();
    	
    	updateBookedTable();
    	updateRoomTable();
    

    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    
}
