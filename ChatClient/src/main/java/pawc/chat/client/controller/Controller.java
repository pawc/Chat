package pawc.chat.client.controller;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pawc.chat.client.Main;
import pawc.chat.shared.model.Data;

public class Controller {

    @FXML
    protected TextArea area;
    
    @FXML
    protected TextField field;
    
    @FXML
    protected ListView list;
    
    @FXML private MenuItem connect;
    @FXML private MenuItem settings;
    @FXML private MenuItem about;
    
    protected ObservableList<String> observableList;
  
    protected static String nick = "guest";	
    protected static String host = "pawc.ddns.net";
    protected static int port = 3000;
    protected Socket socket;
    protected boolean connected = false;

    
    public void initialize(){
    	
    	observableList = FXCollections.observableArrayList();
    	list.setItems(observableList);
    	
    	area.setText("Welcome to ChatClient. Edit nick, host and port"
    			+ " in Chat->Settings. \nTo connect click Chat->Connect\n");
    	area.setEditable(false);
    	area.wrapTextProperty().set(true);
    	
    	/*field.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		
    		public void handle(KeyEvent e){
    			if(connected&&!field.getText().equals("")){
	    			if(e.getCode()==KeyCode.ENTER){
	    				try{
	    				    List<String> arguments = new ArrayList<String>();
	    				    arguments.add(field.getText());
	    				    Data data = new Data("message", arguments);	
	    				    clienout.writeObject(data);
	    				    out.flush();
	    				    out.close();
	    				}
	    				catch(IOException er){
	    				    log("Error sending message");
	    				    log(er.toString());
	    				}
	    			}
    			}
    		
    		}
    	});
    	*/
    	connect.setOnAction(event->{
    		new Connection(this).start();
    	});
    	
    	settings.setOnAction(event->{
    		AnchorPane settingsPane;
    		try {
				settingsPane = (AnchorPane) FXMLLoader.load(Main.class.getResource("controller/Settings.fxml"));
			} catch (IOException e) {
				log("Couldn't load settings pane: "+e.toString());
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
    			aboutPane = FXMLLoader.load(Main.class.getResource("About.fxml"));
    		}
    		catch(IOException e){
    			log("Couldn't load about pane: "+e.toString());
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
    
    public void addNicks(List<String> nicks){
    	Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				for(String nick : nicks){
				    observableList.add(nick);
				}
				
			}
		});
    }
    
    public void removeNicks(){
    	Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				observableList.clear();
				
			}
		});
    }
    
    
    public void log(String string){
    	area.appendText(string+"\n");
    }
    
    public Socket getSocket(){
        return socket;
    }

    
}
        

    
  