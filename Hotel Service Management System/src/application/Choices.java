package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class Choices {

	// BED TYPE OPTIONS IN ADMIN - ADD ROOM TYPE
	String[] typeOfBeds = {"1 Single Bed", "1 Double Bed", "1 Queen-Sized Bed", "1 King-Sized Bed", "2 Single Bed", "2 Double Bed"};
    
    public void bedTypeChoices(ComboBox<String> bedType) {
    	ObservableList<String> bedOptions = FXCollections.observableArrayList(typeOfBeds);
        bedType.setItems(bedOptions);
    }

    // ROOM STATUS OPTIONS IN ADMIN - ADD ROOM
	String[] roomStatus = {"Available", "Occupied"};
	
	public void roomStatusChoices(ComboBox<String> bedType) {
		ObservableList<String> bedOptions = FXCollections.observableArrayList(roomStatus);
	    bedType.setItems(bedOptions);
	}
}
