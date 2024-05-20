package application.receptionist;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import application.AlertMessage;
import application.DB_Connection;
import application.guest.GuestPage_Controller;
import application.logIn_signUp.LogIn_Controller;
import application.tableData.AllTransaction;
import application.tableData.Booked;
import application.tableData.Room;
import application.tableData.WalkIn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;




public class ReceptionistPage_Controller extends DB_Connection {
	
	private static ReceptionistPage_Controller instance;
	public ReceptionistPage_Controller() {
		instance = this;
	}
	public static ReceptionistPage_Controller getInstance() {
		return instance;
	}

	//          ---------     SWITCH PAGE     --------
	@FXML
	private AnchorPane dashboard_page, room_page, booking_page, walkIn_page, transact_page, profile_page;
	@FXML
	private Button dashboard_btn, room_btn, booking_btn, walkIn_btn, transact_btn, profile_btn;
    
    // BOOKING PAGE
    
    @FXML
    private Label incomingNum_label, todayNum_Label;
    @FXML
    private TextField bookingSearch_TxtField;

    @FXML
    public TableView<Booked> booking_table;
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
    protected Label availRoom_label;
    @FXML
    protected TextField wSearch_txtField;
    
    @FXML
    protected TableView<WalkIn> walkIn_table;

    @FXML
    protected TableColumn<WalkIn, LocalDate> wArrival_col, wDeparture_col;
    @FXML
	protected TableColumn<WalkIn, Integer> wRoomNum_col;
    @FXML
    protected TableColumn<WalkIn, String> wRoomType_col, wGuest_col;
    
    // ROOM PAGE
    @FXML
    protected Label totalRoom_label, occupied_label, availroom_label;
    @FXML
    protected TextField rmSearch_txtField;
    @FXML
    protected TableView<Room> rmTable;
    @FXML
    protected TableColumn<Room, Integer> rm_roomNum; 
    @FXML
    protected TableColumn<Room, String> rm_roomType, rm_status;
    @FXML
    protected TableColumn<Room, Double> rm_price;
    
 // DASHBOARD PAGE
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart roomType_pieChart, guestType_pieChart;
    @FXML
    private Label dAvailRooms_label;
    @FXML
    private Label dBookedGuests_label;
    @FXML
    private Label dTotalGuests_label;
    
 //  TRANSACTION PAGE
    
    @FXML
    private Label allTransact_label, totalGuest_label, receptionist_label;   
    @FXML
    private TextField transactionSearch_TxtField;
    
    @FXML
    protected TableView<AllTransaction> transact_table;
    @FXML
    private TableColumn<AllTransaction, Integer> tTransactNum_col;
    @FXML
    private TableColumn<AllTransaction, String> tDate_col, tArrivalDate_col, tDepartureDate_col;
    @FXML
    private TableColumn<AllTransaction, String> tGuest_col;
    @FXML
    private TableColumn<AllTransaction, String> tTime_col;


 // PROFILE PAGE
    @FXML
    private Label bdate_label, email_label, pass_label, phone_label, name_label;
    @FXML
    private Button hidePass_Btn, showPass_Btn;
    String password, hiddenPassword;
    
	
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
	        {
	        	profile_page.toFront();
	        	showProfileInfo();
	            break;
	        }
	        default:
	        	System.out.println("Invalid");
	        
	            break;
	    }
	}

	//  ---------     SHOW DATA IN THE TABLE     --------
	
	
	// UPDATES THE TABLE IN THE BOOKING PAGE
	TableCell<Booked, String> updateBookedTable() {
		
	GuestPage_Controller.getInstance().updateRoomStatus();
	ObservableList<Booked> allBookedList = FXCollections.observableArrayList(); 
	booking_table.setFixedCellSize(50);
	
	String query = "SELECT t.Transaction_ID, CONCAT(g.First_Name, ' ' ,g.Last_Name) AS Guest_Name, t.Check_In_Date, t.Check_Out_Date, t.Room_No "
			+ "FROM `TRANSACTION` AS t "
			+ "JOIN GUEST AS g ON g.Guest_ID = t.Guest_ID "
			+ "WHERE g.Guest_Type = 'Booked Guest' "
			+ "AND t.Check_Out_Date > CURRENT_DATE()" // does not include the guests which already departed
			+ "ORDER BY t.Transaction_ID ASC ";
	try {
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
	
	// UPDATES THE TABLE IN THE WALK-IN PAGE
	TableCell<WalkIn, String> updateWalkInTable() {
		// Update the data in the ObservableList that populates the TableView
		GuestPage_Controller.getInstance().updateRoomStatus();
		ObservableList<WalkIn> allWalkInList = FXCollections.observableArrayList(); 
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
	 walkIn_table.setFixedCellSize(50);
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
	
	// UPDATES THE TABLE IN THE ROOM PAGE
	TableCell<Room, String> updateRoomTable() {
		
	GuestPage_Controller.getInstance().updateRoomStatus();
	ObservableList<Room> allRoomList = FXCollections.observableArrayList(); 
	rmTable.setFixedCellSize(50);
	
	String query = "SELECT room.Room_No, room_type.Name, room_type.Price_per_Night, room_type.Tax, room.`Status` "
			+ "FROM room "
			+ "INNER JOIN room_type ON room.Type_ID = room_type.Type_ID;";
	
	try {
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
	             @Override	//POLYMORPHISM
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
	    	
	
	// UPDATES THE TABLE IN THE TRANSACTION PAGE
		TableCell<AllTransaction, String> updateTransactionTable() {
			
		GuestPage_Controller.getInstance().updateRoomStatus();
		ObservableList<AllTransaction> allTransactList = FXCollections.observableArrayList(); 
		transact_table.setFixedCellSize(50);
		
		String query = "SELECT t.Transaction_ID, t.`Date`, t.`Time`, t.Check_In_Date, t.Check_Out_Date, " +
	             "CONCAT(g.First_Name, ' ', g.Last_Name) AS Guest_Name " +
	             "FROM `TRANSACTION` AS t " +
	             "JOIN `GUEST` AS g ON t.Guest_ID = g.Guest_ID;";
		try {	
		 prepare = connection.prepareStatement(query);
		 result = prepare.executeQuery();
		
		 while (result.next()) {
		
		 	// Define a formatter for the desired date format
		 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
		 	// Define a formatter for the desired time format
		 	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		
		 	// Retrieve the check-in and check-out dates from the result set
		 	LocalDate date = result.getDate("Date").toLocalDate();
		     LocalDate checkInDate = result.getDate("Check_In_Date").toLocalDate();
		     LocalDate checkOutDate = result.getDate("Check_Out_Date").toLocalDate();
		     
		     // Format the check-in and check-out dates
		     String formattedDate = date.format(formatter);
		     String formattedArrivalDate = checkInDate.format(formatter);
		     String formattedDepartureDate = checkOutDate.format(formatter);
		     
		     // Retrieve the transaction time from the result set
		     LocalTime transactionTime = result.getTime("Time").toLocalTime();
		     // Format the transaction time
		     String formattedTransactionTime = transactionTime.format(timeFormatter);
		
		     allTransactList.add(new AllTransaction(
		 	    result.getInt("Transaction_ID"),
		 	    result.getString("Guest_Name"),
		 	    formattedDate,
		 	    formattedTransactionTime,
		 	    formattedArrivalDate,
		 	    formattedDepartureDate
		 	));
		 }
		
		 // Set the items to the table before applying cell factory
		 transact_table.setItems(allTransactList);
		 transact_table.refresh();
		
		 tTransactNum_col.setCellValueFactory(new PropertyValueFactory<>("transactNum"));
		 tGuest_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuestName()));
		 tDate_col.setCellValueFactory(new PropertyValueFactory<>("date"));
		 tTime_col.setCellValueFactory(new PropertyValueFactory<>("time"));
		 tArrivalDate_col.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
		 tDepartureDate_col.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
		
			
		
		// SEARCH FILTER
		 FilteredList<AllTransaction> filteredData = new FilteredList<>(allTransactList, b -> true);
		 transactionSearch_TxtField.textProperty().addListener((observable, oldValue, newValue) -> {
		 	filteredData.setPredicate(transact -> {
		 		 if (newValue.isEmpty() || newValue.isBlank()) {
		 	            // If the search text is empty, display all items
		 	            return true;
		 	        }
		
		 	        // Convert the search text to lowercase for case-insensitive search
		 	        String searchKeyword = newValue.toLowerCase();
		
		 	     // Check if any of the properties contain the search text
		 	        return String.valueOf(transact.getTransactNum()).contains(searchKeyword) ||
		 	        		transact.getGuestName().toLowerCase().contains(searchKeyword) ||
		 	        		transact.getDate().toString().toLowerCase().contains(searchKeyword) ||
		 	        		transact.getTime().toString().toLowerCase().contains(searchKeyword) ||
		 	        		transact.getArrivalDate().toString().toLowerCase().contains(searchKeyword) ||
		 	        		transact.getDepartureDate().toString().toLowerCase().contains(searchKeyword);
		 	               
		 	});
		 });
		 
		 SortedList<AllTransaction> sortedData = new SortedList<>(filteredData);
		 sortedData.comparatorProperty().bind(transact_table.comparatorProperty());
		 transact_table.setItems(sortedData);
		 
		 
		} catch (SQLException e) {
		 e.printStackTrace();
		
		}	
		return null;
		}
	
	
	
	
	
	public void revenueChart() {
		String revenueQuery = "SELECT DATE(t.`Date`) AS Transaction_Date, SUM(pd.Total_Price) AS Revenue " +
	             "FROM `transaction` t " +
	             "INNER JOIN payment_details pd ON t.Payment_ID = pd.Payment_ID " +
	             "WHERE t.`Date` >= CURRENT_DATE() - INTERVAL 5 DAY " +
	             "GROUP BY DATE(t.`Date`) " +
	             "ORDER BY DATE(t.`Date`) ASC;";
		try {
			XYChart.Series<String, Number> chartData = new Series<String, Number>();
			prepare = connection.prepareStatement(revenueQuery);
    	    result = prepare.executeQuery();
 
    	    
    	    while (result.next()) {
    	    	XYChart.Data<String, Number> data = new XYChart.Data<>(result.getString(1), result.getInt(2));
                chartData.getData().add(data);
            //    chartData.getNode().setStyle("-fx-bar-fill: green;");
    	    }
    	    barChart.setLegendVisible(false);
    	    barChart.getData().add(chartData);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void pieChart() {
		String roomTypePieQuery  = "SELECT rt.Name AS RoomType, COUNT(*) AS BookingCount " +
	             "FROM room_type AS rt " +
	             "JOIN room AS r ON rt.Type_ID = r.Type_ID " +
	             "JOIN `transaction` AS t ON r.Room_No = t.Room_No " +
	             "GROUP BY rt.Name;";
		try {
	        prepare = connection.prepareStatement(roomTypePieQuery);
	        result = prepare.executeQuery();

	        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

	        while (result.next()) {
	            String roomType = result.getString("RoomType");
	            int bookingCount = result.getInt("BookingCount");
	            
	            // Create PieChart.Data for each room type and booking count
	            PieChart.Data data = new PieChart.Data(roomType, bookingCount);
	            
	            // Add the data to the observable list
	            chartData.add(data);
	        }
	        
	        roomType_pieChart.getData().addAll(chartData);

	     // Calculate the total sum of all pie values
            double total = roomType_pieChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum();

            // Iterate through each slice of the pie chart
            roomType_pieChart.getData().forEach(data -> {
                // Calculate the percentage for the current slice
                double percentage = (data.getPieValue() / total) * 100;

                // Format the percentage string
                String formattedPercentage = String.format("%.2f%%", percentage);

                // Create and install tooltip with the formatted percentage
                Tooltip toolTip = new Tooltip(formattedPercentage);
                Tooltip.install(data.getNode(), toolTip);
            });
		           
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		 
		 String guestTypePieQuery = "SELECT " +
			        "SUM(CASE WHEN g.Guest_Type = 'Walk-in Guest' THEN 1 ELSE 0 END) AS NumWalkInGuests, " +
			        "SUM(CASE WHEN g.Guest_Type = 'Booked Guest' THEN 1 ELSE 0 END) AS NumBookedGuests " +
			        "FROM GUEST AS g " +
			        "JOIN `TRANSACTION` AS t ON g.Guest_ID = t.Guest_ID;";
		 try {
		        prepare = connection.prepareStatement(guestTypePieQuery);
		        result = prepare.executeQuery();

		        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

		        while (result.next()) {
		            int numWalkInGuests = result.getInt("NumWalkInGuests");
		            int numBookedGuests = result.getInt("NumBookedGuests");

		            // Create PieChart.Data for walk-in guests and booked guests
		            PieChart.Data walkInData = new PieChart.Data("Walk-in Guests", numWalkInGuests);
		            PieChart.Data bookedData = new PieChart.Data("Booked Guests", numBookedGuests);

		            // Add the data to the observable list
		            chartData.addAll(walkInData, bookedData);
		            
		        }
	            guestType_pieChart.getData().addAll(chartData);
	            
	         // Calculate the total sum of all pie values
	            double total = guestType_pieChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum();

	            // Iterate through each slice of the pie chart
	            guestType_pieChart.getData().forEach(data -> {
	                // Calculate the percentage for the current slice
	                double percentage = (data.getPieValue() / total) * 100;

	                // Format the percentage string
	                String formattedPercentage = String.format("%.2f%%", percentage);

	                // Create and install tooltip with the formatted percentage
	                Tooltip toolTip = new Tooltip(formattedPercentage);
	                Tooltip.install(data.getNode(), toolTip);
	            });
	            

	            
			} catch (SQLException e) {
			    e.printStackTrace();
			}

		 
		
	}
	
//  ---------     DASHBOARD TAB COMPONENT ACTIONS    --------
    public void dashboardController() {
    	
    	revenueChart();
    	pieChart();
    	
    	// query to count all guests today
    				String guestTodayQuery = "SELECT COUNT(Room_No) AS 'TodaysGuest' FROM Room WHERE Status = 'Occupied'";
    				
    				try {
    					prepare = connection.prepareStatement(guestTodayQuery);
    					result = prepare.executeQuery();
    		// GUESTS TODAY
    					if (result.next()) {
    					 int total_guests = result.getInt("TodaysGuest");
    					 dTotalGuests_label.setText(String.valueOf(total_guests));
    					}
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}	
    				
    				String countBookedGuestsToday = "SELECT COUNT(*) AS AllBookedGuests " +
								                "FROM GUEST AS g " +
								                "JOIN `TRANSACTION` AS t ON g.Guest_ID = t.Guest_ID " +
								                "WHERE g.Guest_Type = 'Booked Guest' " +
								                "AND t.Check_In_Date >= CURRENT_DATE();";
    				try {
    					prepare = connection.prepareStatement(countBookedGuestsToday);
    					result = prepare.executeQuery();
    				
    		// AVAILABLE ROOMS TODAY
    			
    					if (result.next()) {
    					 int totalBookedGuests = result.getInt("AllBookedGuests");
    					 dBookedGuests_label.setText(String.valueOf(totalBookedGuests));
    					}
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
    				
			    	String countAvailRoomQuery = "SELECT COUNT(r.Room_No) AS NumOfAvailRooms " +
												 "FROM room_type AS rt " +
												 "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " +
												 "WHERE r.Room_no NOT IN ( " +
												 "    SELECT Room_no " +
												 "    FROM `transaction` " +
												 "    WHERE check_in_date <= CURRENT_DATE() " +
												 "    AND check_out_date > CURRENT_DATE() " +
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
						 dAvailRooms_label.setText(String.valueOf(totalAvailRooms));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}	
    	
    }

//  ---------     BOOKING TAB COMPONENT ACTIONS    --------
    public void bookingController() {
    	// update the table
    	updateBookedTable();
    	// query to count all of the BOOKED rooms today
    	String countBookedRoomsToday = "SELECT COUNT(*) AS NumBookedGuests " +
                "FROM GUEST AS g " +
                "JOIN `TRANSACTION` AS t ON g.Guest_ID = t.Guest_ID " +
                "WHERE g.Guest_Type = 'Booked Guest' " +
                "AND t.Check_In_Date = CURRENT_DATE();";
    	
    	String countBookedRoomsIncoming = "SELECT COUNT(*) AS IncomingBookedGuests " +
                "FROM GUEST AS g " +
                "JOIN `TRANSACTION` AS t ON g.Guest_ID = t.Guest_ID " +
                "WHERE g.Guest_Type = 'Booked Guest' " +
                "AND t.Check_In_Date > CURRENT_DATE();";
    
    	try {
    	    prepare = connection.prepareStatement(countBookedRoomsToday);
    	    result = prepare.executeQuery();
    	    
    	    // BOOKED GUESTS TODAY
 
    	    if (result.next()) {
    	        // Retrieve the value of NumOfBookedGuests from the result set
    	        int numOfBookedRoomsToday = result.getInt("NumBookedGuests");
    	        // Set the text of the availRoom_label using the retrieved value
    	        todayNum_Label.setText(String.valueOf(numOfBookedRoomsToday));
    	    }
    	    
    	    prepare = connection.prepareStatement(countBookedRoomsIncoming);
    	    result = prepare.executeQuery();
    	    
    	    // INCOMING BOOKED GUESTS 
 
    	    if (result.next()) {
    	        // Retrieve the value of NumOfBookedGuests from the result set
    	        int numOfBookedRoomsIncoming = result.getInt("IncomingBookedGuests");
    	        // Set the text of the availRoom_label using the retrieved value
    	        incomingNum_label.setText(String.valueOf(numOfBookedRoomsIncoming));
    	    }
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	 
    	}	
    }
	
		//  ---------     WALK-IN TAB COMPONENT ACTIONS    ---------------------------
		    
		public void walkInController() {
		updateWalkInTable(); // update the table
		
		// query to count all of the available rooms today
		String countAvailRoomQuery = "SELECT COUNT(r.Room_No) AS NumOfAvailRooms " +
		 "FROM room_type AS rt " +
		 "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " +
		 "WHERE r.Room_no NOT IN ( " +
		 "    SELECT Room_no " +
		 "    FROM `transaction` " +
		 "    WHERE check_in_date <= CURRENT_DATE() " +
		 "    AND check_out_date > CURRENT_DATE() " +
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
	
		
	//  ---------     ROOM TAB COMPONENT ACTIONS    --------------------
		public void roomController() {
			updateRoomTable(); // update the table
			
			// query to count all of the available rooms today
			String countTotalRoomQuery = "SELECT COUNT(*) AS 'Total Rooms' FROM ROOM";
			
			try {
				prepare = connection.prepareStatement(countTotalRoomQuery);
				result = prepare.executeQuery();
	// ALL ROOMS
				if (result.next()) {
				 int totalRooms = result.getInt("Total Rooms");
				 totalRoom_label.setText(String.valueOf(totalRooms));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
			
			String countAvailRoomQuery = "SELECT COUNT(r.Room_No) AS NumOfAvailRooms " +
					 "FROM room_type AS rt " +
					 "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " +
					 "WHERE r.Room_no NOT IN ( " +
					 "    SELECT Room_no " +
					 "    FROM `transaction` " +
					 "    WHERE check_in_date <= CURRENT_DATE() " +
					 "    AND check_out_date > CURRENT_DATE() " +
					 ")";
			try {
				prepare = connection.prepareStatement(countAvailRoomQuery);
				result = prepare.executeQuery();
			
	// AVAILABLE ROOMS TODAY
		
				if (result.next()) {
				 int totalAvailRooms = result.getInt("NumOfAvailRooms");
				 availroom_label.setText(String.valueOf(totalAvailRooms));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String countOccupiedRoomQuery = "SELECT COUNT(r.Room_No) AS NumOfOccupiedRooms " +
					 "FROM room_type AS rt " +
					 "INNER JOIN room AS r ON r.Type_ID = rt.Type_ID " +
					 "WHERE r.Room_no IN ( " +
					 "    SELECT Room_no " +
					 "    FROM `transaction` " +
					 "    WHERE check_in_date <= CURRENT_DATE() " +
					 "    AND check_out_date > CURRENT_DATE() " +
					 ")";
			
			try {
				prepare = connection.prepareStatement(countOccupiedRoomQuery);
				result = prepare.executeQuery();
			
	// OCCUPIED ROOMS TODAY
		
			if (result.next()) {
			 int totalOccupiedRooms = result.getInt("NumOfOccupiedRooms");
			 occupied_label.setText(String.valueOf(totalOccupiedRooms));
			}

			
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
			                    "    AND check_out_date > ? " +
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
    
    @FXML
    void checkInButtonAction(ActionEvent event) throws IOException {
        // Load the FXML file and create a new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/receptionist/Check-In.fxml"));
        Parent root = loader.load();  
        
        Scene scene = new Scene(root);

        // Create a new stage for the scene
        Stage checkInStage = new Stage();
     
        checkInStage.setScene(scene);

        // Set the owner of the new stage to the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        checkInStage.initOwner(currentStage);
        checkInStage.setTitle("OKADA Manila - Hotel Check-In");
        checkInStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/purple logo.png")));

        // Position the new stage relative to the current stage
        checkInStage.setX(currentStage.getX() + 50); // Offset the new stage by 50 pixels from the current stage's X position
        checkInStage.setY(currentStage.getY() + 50); // Offset the new stage by 50 pixels from the current stage's Y position
        checkInStage.setResizable(false);
        // Show the new stage
        checkInStage.show();
        if (walkIn_table == null)
    		System.out.println(walkIn_table);
    }

    
    public void transactionController() {
		updateTransactionTable(); // update the table
		// query to count all transactions
		String countTransactQuery = "SELECT COUNT(*) AS Total_Transactions FROM `TRANSACTION`;";

		
		try {
			prepare = connection.prepareStatement(countTransactQuery);
			result = prepare.executeQuery();
// ALL ROOMS
			if (result.next()) {
			 int totalTransactions = result.getInt("Total_Transactions");
			 allTransact_label.setText(String.valueOf(totalTransactions));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		String countTotalGuestQuery = "SELECT COUNT(DISTINCT g.Guest_ID) AS Total_Guests " +
             "FROM GUEST AS g " +
             "JOIN `TRANSACTION` AS t ON g.Guest_ID = t.Guest_ID;";

		try {
			prepare = connection.prepareStatement(countTotalGuestQuery);
			result = prepare.executeQuery();
		
	// TOTAL GUEST
	
			if (result.next()) {
			 int totalGuest = result.getInt("Total_Guests");
			 totalGuest_label.setText(String.valueOf(totalGuest));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String countReceptionistQuery = "SELECT COUNT(*) AS Total_Receptionists FROM RECEPTIONIST;";

		try {
			prepare = connection.prepareStatement(countReceptionistQuery);
			result = prepare.executeQuery();
		
// OCCUPIED ROOMS TODAY
	
		if (result.next()) {
		 int totalReceptionist = result.getInt("Total_Receptionists");
		 receptionist_label.setText(String.valueOf(totalReceptionist));
		}

		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
    
//  ======================================         PROFILE PAGE        ==============================================
    
		    public void showProfileInfo() {
		    	int accountID = LogIn_Controller.getAccountID();	// get the Guest account ID when they logged in
		    	try {
		        	// SQL query to retrieve info from sign in page to get the personal information of user
		        	String query = "SELECT CONCAT(r.First_Name, ' ', r.Last_Name) AS Name, r.Email, r.Password, r.Contact_No, r.Birthdate " +
		                    "FROM RECEPTIONIST r " +
		                    "WHERE r.Employee_ID = ?";
		
		        	prepare = connection.prepareStatement(query);
		            prepare.setInt(1, accountID); // Set the guest_acc_ID
		        	result = prepare.executeQuery();
		        	
		        	 // If available room is found, show the booking summary
		            if (result.next()) {
		            	String name = result.getString("Name");
		            	String email = result.getString("Email");
		            	String pass = result.getString("Password"); 	
		            	String contactNum = result.getString("Contact_No");
		            	Date bday = result.getDate("Birthdate");
		            	
		            	// display password placeholders as bullet points
		                String hiddenPass = "";
		                for (int i = 0; i < pass.length(); i++) {
		                    hiddenPass += "\u2022"; // Unicode for bullet point
		                }
		                
		                password = pass;
		                hiddenPassword = hiddenPass;
		                
		            	LocalDate birthdate = bday.toLocalDate(); // Convert SQL Date to LocalDate
		            	  // Format LocalDate to "Month Day, Year"
		            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
		            	String formattedBirthdate = birthdate.format(formatter);
		            	
		                name_label.setText(name);
		                email_label.setText(email);
		                pass_label.setText(hiddenPassword);
		        		pass_label.setFont(new Font("Amasis MT W1G", 35));
		               	
		                phone_label.setText(contactNum);
		                bdate_label.setText(formattedBirthdate);
		            	
		            }
		            	
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		  }
		    @FXML
		  public void showPassButtonAction(ActionEvent event) {
			  showPass_Btn.setVisible(false);
			  hidePass_Btn.setVisible(true);
			  
			  pass_label.setText(password);
			  pass_label.setFont(new Font("Amasis MT W1G", 16));
		  }
		  
		    @FXML
		  public void hidePassButtonAction(ActionEvent event) {
			  showPass_Btn.setVisible(true);
			  hidePass_Btn.setVisible(false); 
			  
			  pass_label.setText(hiddenPassword);
			  pass_label.setFont(new Font("Amasis MT W1G", 35));
		  }
		    
		    
		    // =============================       REPORT BUTTONS      ===========================================
		    
		    // Transaction Report
		    @FXML
		    void transactionReportAction(ActionEvent event) {
		    	String query = "SELECT t.Transaction_ID, t.`Date`, t.`Time`, t.Check_In_Date, t.Check_Out_Date, " +
		                "CONCAT(g.First_Name, ' ', g.Last_Name) AS Guest_Name " +
		                "FROM `TRANSACTION` AS t " +
		                "JOIN `GUEST` AS g ON t.Guest_ID = g.Guest_ID;";

		    	try {
		    		// Load the JasperDesign from the specified JRXML file
					JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\ACER\\JaspersoftWorkspace\\Hotel Booking Reports\\TransactionReport.jrxml");
					// Create a new JRDesignQuery object to hold the SQL query
					JRDesignQuery updateQuery = new JRDesignQuery();
					updateQuery.setText(query);
					
					// Create a new JRDesignQuery object to hold the SQL query
					jdesign.setQuery(updateQuery);
					
					// Compile the JasperDesign to produce a JasperReport object
					JasperReport jreport = JasperCompileManager.compileReport(jdesign);
					// Fill the report with data from the database connection
				    // The second parameter is for passing parameters to the report, which is null in this case
					JasperPrint jprint = JasperFillManager.fillReport(jreport, null, connection);
					// View the report using JasperViewer
					JasperViewer.viewReport(jprint, false); // The 'false' parameter ensures the application does not exit  
		    	} catch (JRException e) {
					e.printStackTrace();
				}
		    }
		
		    // Booking Report
		    @FXML
		    void bookingReportAction(ActionEvent event) {
		    	String query = "SELECT t.Transaction_ID, CONCAT(g.First_Name, ' ' ,g.Last_Name) AS Guest_Name, t.Check_In_Date, t.Check_Out_Date, t.Room_No "
		    			+ "FROM `TRANSACTION` AS t "
		    			+ "JOIN GUEST AS g ON g.Guest_ID = t.Guest_ID "
		    			+ "WHERE g.Guest_Type = 'Booked Guest' "
		    			+ "AND t.Check_Out_Date > CURRENT_DATE()" // does not include the guests which already departed
		    			+ "ORDER BY t.Transaction_ID ASC ";

		    	try {
		    		// Load the JasperDesign from the specified JRXML file
					JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\ACER\\JaspersoftWorkspace\\Hotel Booking Reports\\BookingReport.jrxml");
					JRDesignQuery updateQuery = new JRDesignQuery();
					updateQuery.setText(query);
					
					jdesign.setQuery(updateQuery);
					
					JasperReport jreport = JasperCompileManager.compileReport(jdesign);
					JasperPrint jprint = JasperFillManager.fillReport(jreport, null, connection);

					JasperViewer.viewReport(jprint, false); // The 'false' parameter ensures the application does not exit  
		    	} catch (JRException e) {
					e.printStackTrace();
				}
		    }
		    
		    // Walk-In Guests Report
		    @FXML
		    void walkInReportAction(ActionEvent event) {
		    	String query = "SELECT r.Room_No, rt.Name AS RoomType_Name, CONCAT(g.First_Name, ' ' ,g.Last_Name) AS Guest_Name, t.Check_In_Date, t.Check_Out_Date "
		    			+ "FROM Room AS r "
		    			+ "JOIN Room_type AS rt ON r.Type_ID = rt.Type_ID "
		    			+ "JOIN `Transaction` AS t ON r.Room_No = t.Room_No "
		    			+ "JOIN Guest AS g ON t.Guest_ID = g.Guest_ID "
		    			+ "WHERE g.Guest_Type = 'Walk-In Guest'"
		    			+ "AND t.Check_In_Date <= CURDATE() " // Check if check_in_date is today or before today
		    			+ "AND t.Check_Out_Date >= CURDATE();"; // Check if check_out_date is today

		    	try {
		    		// Load the JasperDesign from the specified JRXML file
					JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\ACER\\JaspersoftWorkspace\\Hotel Booking Reports\\WalkIn_Report.jrxml");
					JRDesignQuery updateQuery = new JRDesignQuery();
					updateQuery.setText(query);
					
					jdesign.setQuery(updateQuery);
					
					JasperReport jreport = JasperCompileManager.compileReport(jdesign);
					JasperPrint jprint = JasperFillManager.fillReport(jreport, null, connection);

					JasperViewer.viewReport(jprint, false); // The 'false' parameter ensures the application does not exit  
		    	} catch (JRException e) {
					e.printStackTrace();
				}
		    }
		    
		    // Room Report
		    @FXML
		    void roomReportAction(ActionEvent event) {
		    	String query = "SELECT room.Room_No, room_type.Name, room_type.Price_per_Night, room_type.Tax, room.`Status` "
		    			+ "FROM room "
		    			+ "INNER JOIN room_type ON room.Type_ID = room_type.Type_ID;";

		    	try {
		    		// Load the JasperDesign from the specified JRXML file
					JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\ACER\\JaspersoftWorkspace\\Hotel Booking Reports\\RoomReport.jrxml");
					JRDesignQuery updateQuery = new JRDesignQuery();
					updateQuery.setText(query);
					
					jdesign.setQuery(updateQuery);
					
					JasperReport jreport = JasperCompileManager.compileReport(jdesign);
					JasperPrint jprint = JasperFillManager.fillReport(jreport, null, connection);

					JasperViewer.viewReport(jprint, false); // The 'false' parameter ensures the application does not exit  
		    	} catch (JRException e) {
					e.printStackTrace();
				}
		    }
		    
		    
	
    public void initialize() {
    	connection = connect();
    	
    	dashboardController();
    	walkInController();
    	roomController();
    	bookingController();
    	transactionController();
    }   
    
}
