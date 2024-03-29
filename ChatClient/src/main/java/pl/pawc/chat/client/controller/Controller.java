package pl.pawc.chat.client.controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.pawc.chat.shared.Data;

public class Controller {

    @FXML protected TextArea area;
    @FXML protected TextField field;
    @FXML protected ListView list;
    @FXML private MenuItem connect;
    @FXML private MenuItem settings;
    @FXML private MenuItem about;
    
    protected static ObservableList<String> observableList;
    protected static String nick = "guest";	
    protected static String host = "localhost";
    protected static int port = 3000;
    protected static ArrayList<PrivateMessagePaneController> privateMessagePaneControllerContainer;
    protected static Socket socket;
    protected static ObjectOutputStream out = null;
    protected static boolean connected = false;
    
    public void initialize(){
    		
        privateMessagePaneControllerContainer = new ArrayList<>();
        
    	observableList = FXCollections.observableArrayList();
    	list.setItems(observableList);
    	
    	area.setText("Welcome to ChatClient. Edit nick, host and port"
    			+ " in Chat>Settings. \nTo connect click Chat>Connect\n");
    	area.setEditable(false);
    	area.wrapTextProperty().set(true);
    	
    	list.setOnMouseClicked(event ->{
    	   SelectionModel selected = list.getSelectionModel();
    	   String nick = selected.getSelectedItem().toString();
    	   if(event.getClickCount()==2){
    	       if(isPMalreadyOpened(nick)) log("Conversation window with "+nick+" already opened");
    	       else if(nick.equals(Controller.nick)) log("Can't pm with yourself");
    	       else{
    	           PrivateMessagePaneController c = new PrivateMessagePaneController(nick);
    	           Controller.privateMessagePaneControllerContainer.add(c);
    	           openNewPrivateWindow(c, "private chat with "+nick);
    	       }
    	   }
    	});
    	
    	field.setOnKeyPressed(e -> {
			if(connected&&!field.getText().equals("")){
				if(e.getCode()==KeyCode.ENTER){
					try{
						String arguments = nick+": "+field.getText()+"\n";
						Data data = new Data("message", arguments);
						out.writeObject(data);
						out.flush();
						field.setText("");
					}
					catch(IOException er){
						log("Error sending message");
						log(er.toString());
					}
				}
			}
		});
    	
    	connect.setOnAction(event-> new Connection(area).start());
    	
    	settings.setOnAction(event->{
    	    if(connected){
    	        log("Can't edit setting when connected");
    	        return;
    	    }
    		AnchorPane settingsPane;
    		try {
				settingsPane = FXMLLoader.load(ClassLoader.getSystemResource("ui/Settings.fxml"));
			} catch (IOException e) {
				log("Couldn't load settings pane: "+e);
				e.printStackTrace();
				return;
			}
    		Scene scene = new Scene(settingsPane);
    		Stage stage = new Stage();
    		stage.setTitle("Chat settings");
    		stage.setResizable(false);
    		stage.setScene(scene);
    		stage.show();
    	});
    	
    	about.setOnAction(event->{
    		AnchorPane aboutPane;
    		try{
    			aboutPane = FXMLLoader.load(ClassLoader.getSystemResource("ui/About.fxml"));
    		}
    		catch(IOException e){
    			log("Couldn't load about pane: "+e);
    			return;
    		}
    		Scene scene = new Scene(aboutPane);
    		Stage stage = new Stage();
    		stage.setTitle("About");
    		stage.setResizable(false);
    		stage.setScene(scene);
    		stage.show();
    		
    	});
    	
    }
    
    public static void addNicks(List<String> nicks){
    	Platform.runLater(() -> observableList.addAll(nicks));
    }
    
    public static void removeNicks(){
    	Platform.runLater(() -> observableList.clear());
    }

    public static boolean isPMalreadyOpened(String nick){
        boolean answer = false;
        for(PrivateMessagePaneController controller : privateMessagePaneControllerContainer){
            if(nick.equals(controller.getNick())){
				answer = true;
				break;
			}
        }
        return answer;
        
    }

    public static void openNewPrivateWindow(PrivateMessagePaneController c, String initialMessage){
        
        Platform.runLater(() -> {

            BorderPane PrivateMessagePane = null;

            FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("ui/PrivateMessagePane.fxml"));

            fxmlLoader.setController(c);
            fxmlLoader.setRoot(PrivateMessagePane);

            try{
                PrivateMessagePane = fxmlLoader.load();
            }
            catch(IOException e){
                return;
            }

            Scene scene = new Scene(PrivateMessagePane);
            Stage stage = new Stage();
            stage.setTitle("Private conversation with "+c.getNick());
            stage.setResizable(true);

            c.appendToArea(initialMessage);

            stage.setScene(scene);

            stage.setOnCloseRequest(event -> privateMessagePaneControllerContainer.remove(c));
            stage.show();

		});
    }

	protected void log(String string){
		area.appendText(string+"\n");
	}
 
}