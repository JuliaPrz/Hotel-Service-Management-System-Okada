package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage stage) throws Exception {
			
			Parent root = FXMLLoader.load(getClass().getResource("logIn_signUp/Log In Page.fxml"));
			
			Scene scene = new Scene(root);			
			stage.setScene(scene);
			stage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);

	}
}




//Parent root = FXMLLoader.load(getClass().getResource("guest/GuestPage.fxml"));
//Parent root = FXMLLoader.load(getClass().getResource("receptionist/ReceptionistPage.fxml"));
//Parent root = FXMLLoader.load(getClass().getResource("receptionist/Check-In.fxml"));
