package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class LogIn_Controller implements Initializable {

	@FXML
	private ComboBox<String> logIn_cbb;
	
	private String[] user = {"Guest", "Employee"};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		logIn_cbb.getItems().addAll(user);
	}
	
	

	
}
