package application;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Main extends Application{
	
	// log in controller
	// sign up controller
	//  employee, guest controller

	@Override
	public void start(Stage stage) throws Exception {
			
			//Parent root = FXMLLoader.load(getClass().getResource("logIn_signUp/Sign Up Page.fxml"));
		//	Parent root = FXMLLoader.load(getClass().getResource("guest/GuestPage.fxml"));
			Parent root = FXMLLoader.load(getClass().getResource("hotelCoord/HotelCoordPage.fxml"));
			
			Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
			double width = resolution.getWidth();
			double height = resolution.getHeight(); 
			double w = width/1800;  // your window width
			double h = height/900;  // your window height
			Scale scale = new Scale(w, h, 0, 0);
			root.getTransforms().add(scale);
			Scene scene = new Scene(root);
			
			//String css = this.getClass().getResource("styling.css").toExternalForm();
			//scene.getStylesheets().add(css);
			
			//stage.setResizable(false);
			//stage.setMaximized(true);
			
			stage.setScene(scene);
			stage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);

	}
}
