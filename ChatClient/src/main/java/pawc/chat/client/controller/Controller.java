package pawc.chat.client.controller;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Controller {

    
    @FXML
    public TextArea area;
    
    @FXML
    public TextField field;
    
    @FXML
    public ListView list;
  


    
    public void initialize(){
    	
    	area.setEditable(false);
    	
    	field.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		
    		public void handle(KeyEvent e){
    			if(!field.getText().equals("")){
	    			if(e.getCode()==KeyCode.ENTER){
	    				area.appendText(field.getText()+"\n");
	    				field.setText("");
	    			}
    			}
    		}
    	});
    	
    }
    
}
        

    
  