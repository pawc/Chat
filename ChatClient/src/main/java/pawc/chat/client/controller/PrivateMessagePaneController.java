package pawc.chat.client.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PrivateMessagePaneController {

    @FXML TextArea area;
    @FXML TextField field;
    
    public void initialize(){
        area.setEditable(false);
        
        field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ENTER){
                    
                }
                
            }
        });
    
    }
    
    
    
}
