package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class Choices {

    // ROOM STATUS OPTIONS IN ADMIN - ADD ROOM
	String[] userOptions = {"Guest", "Employee"};
	
	public void userChoices(ComboBox<String> userType) {
		ObservableList<String> userTypeList = FXCollections.observableArrayList(userOptions);
		userType.setItems(userTypeList);
	}
}
