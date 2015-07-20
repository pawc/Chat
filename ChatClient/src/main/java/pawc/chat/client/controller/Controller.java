package pawc.chat.client.controller;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;



public class Controller {

    
    @FXML
    protected TextArea area;
    
    @FXML
    protected TextField field;
    
    @FXML
    protected ListView list;
    
    @FXML private MenuItem connect;
    @FXML private MenuItem settings;
    @FXML private MenuItem quit;
    
    @FXML private MenuItem about;
  
    protected String nick = "pawc";
    protected String host = "localhost";
    protected int port = 3000;
    protected Socket socket;
    protected BufferedReader bfr;
    protected DataOutputStream out;
    protected boolean connected = false;

    
    public void initialize(){
    	
    	area.setEditable(false);
    	
    	field.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		
    		public void handle(KeyEvent e){
    			if(connected&&!field.getText().equals("")){
	    			if(e.getCode()==KeyCode.ENTER){
	    				try{
	    				out.writeBytes(field.getText()+"\n");
	    				}
	    				catch(IOException error){
	    					log("Couldn't send a message");
	    					log(error.toString());
	    				}
	    				field.setText("");
	    			}
    			}
    			//else{log("Blank input or not connected to the server");}
    		}
    	});
    	
    	connect.setOnAction(event->{
    		new Connection(this).start();
    	});
    	
    }
    
    
    
    public void log(String string){
    	area.appendText(string+"\n");
    }

    
}
        

    
  