package pawc.chat.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		BorderPane borderPane = (BorderPane) FXMLLoader.load(Main.class.getResource("Main.fxml"));
		primaryStage.setTitle("ChatClient");
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
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
