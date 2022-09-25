package pl.pawc.chat.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainClient extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		BorderPane borderPane = (BorderPane) FXMLLoader.load(ClassLoader.getSystemResource("ui/Main.fxml"));
		primaryStage.setTitle("Chat");
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		//Image icon = new Image(ClassLoader.getSystemResourceAsStream("icons/icon2.png"));
        //primaryStage.getIcons().add(icon);
        primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
			
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
