package application;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertMessage {
	
	private Alert alert;
	

	public void errorMessage (String message) {
		alert = new Alert (AlertType.ERROR); 
		alert.setTitle("Error Message"); 
		alert.setHeaderText (null);
		alert.setContentText (message);
		alert.showAndWait(); //blocks execution until the stage is closed
	}
	
	public void infoMessage (String message) {
		alert = new Alert (AlertType.INFORMATION); 
		alert.setTitle("Information Message");
		alert.setHeaderText (null);
		alert.setContentText (message);
		alert.showAndWait();
	}
	
	public Optional<ButtonType> confirmationMessage(String message) {
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    return alert.showAndWait();
	}
	
	public void warningMessage (String message) {
		alert = new Alert (AlertType.WARNING); 
		alert.setTitle("Warning");
		alert.setHeaderText (null);
		alert.setContentText (message);
		alert.showAndWait();
	}
}
