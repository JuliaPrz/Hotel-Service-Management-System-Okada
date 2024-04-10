package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	// log in controller
	// sign up controller
	// admin, employee, guest controller

	@Override
	public void start(Stage stage) throws Exception {
			
			Parent root = FXMLLoader.load(getClass().getResource("Sign Up Scene.fxml"));
			Scene scene = new Scene(root);
			
			//String css = this.getClass().getResource("styling.css").toExternalForm();
			//scene.getStylesheets().add(css);
			
			stage.setScene(scene);
			stage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);

	}
}
