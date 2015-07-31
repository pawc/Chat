package pawc.chat.client.controller;

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
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pawc.chat.client.Main;
import pawc.chat.shared.model.Data;

public class Controller {

    @FXML protected TextArea area;
    @FXML protected TextField field;
    @FXML protected ListView list;
    @FXML private MenuItem connect;
    @FXML private MenuItem settings;
    @FXML private MenuItem about;
    
    protected ObservableList<String> observableList;
    protected static String nick = "gues";	
    protected static String host = "pawc.ddns.net";
    protected static int port = 3000;
    protected static ArrayList<PrivateMessagePaneController> privateMessagePaneControllerContainer;
    protected Socket socket;
    protected static ObjectOutputStream out = null;
    protected boolean connected = false;

    public void initialize(){
    	
        privateMessagePaneControllerContainer = new ArrayList<PrivateMessagePaneController>();
        
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
    	
    	field.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		public void handle(KeyEvent e){
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
    		}
    	});
    	
    	connect.setOnAction(event->{
    		new Connection(this).start();
    	});
    	
    	settings.setOnAction(event->{
    	    if(connected){
    	        log("Can't edit setting when connected");
    	        return;
    	    }
    		AnchorPane settingsPane;
    		try {
				settingsPane = (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("ui/Settings.fxml"));
			} catch (IOException e) {
				log("Couldn't load settings pane: "+e.toString());
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
    
    public boolean isPMalreadyOpened(String nick){
        boolean answer = false;
        for(PrivateMessagePaneController controller : privateMessagePaneControllerContainer){
            if(nick.equals(controller.getNick())) answer = true;
            break;
        }
        return answer;
        
    }

    public void openNewPrivateWindow(PrivateMessagePaneController c, String initialMessage){
        
        Platform.runLater(new Runnable() {
            
        public void run(){
            
            BorderPane PrivateMessagePane = null;
            
            FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("ui/PrivateMessagePane.fxml"));
            
            fxmlLoader.setController(c);
            fxmlLoader.setRoot(PrivateMessagePane);
            
            try{
                PrivateMessagePane = (BorderPane) fxmlLoader.load();
            }
            catch(IOException e){
                log("Couldn't load private message pane: "+e.toString());
                return;
            }
            
            Scene scene = new Scene(PrivateMessagePane);
            Stage stage = new Stage();
            stage.setTitle("Private conversation with "+c.getNick());
            stage.setResizable(true);
            
            c.appendToArea(initialMessage);
            
            stage.setScene(scene);
            
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            
                @Override
                public void handle(WindowEvent event) {
                    privateMessagePaneControllerContainer.remove(c);
                }
                
            });
            stage.show();
            
            }
        
        });
    }
        
    
    
}
        

    
  