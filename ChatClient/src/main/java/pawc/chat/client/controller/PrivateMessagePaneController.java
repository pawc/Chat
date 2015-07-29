package pawc.chat.client.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PrivateMessagePaneController {

    private String nick;
    @FXML TextArea area;
    @FXML TextField field;
    
    public PrivateMessagePaneController(String nick){
        this.nick=nick;
    }
    
    public void initialize(){
        
        area.setEditable(false);
        area.setText("chat with "+nick);
        
        field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ENTER){
                    
                }
                
            }
        });
    
    }
    
    public void setNick(String nick){
        this.nick = nick;
    }
    
    public String getNick(){
        return nick;
    }
    
    
}
