package pl.pawc.chat.client.controller;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pl.pawc.chat.shared.Data;
import pl.pawc.chat.shared.PrivateMessage;

public class PrivateMessagePaneController {

    private String nick;
    @FXML protected TextArea area;
    @FXML private TextField field;
    
    public PrivateMessagePaneController(String nick){
        this.nick=nick;
    }
    
    public void initialize(){
        
        area.setEditable(false);
        area.setWrapText(true);
                
        field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ENTER&&!field.getText().equals("")){
                    PrivateMessage privateMessage = new PrivateMessage(Controller.nick, nick, field.getText());
                    Data data = new Data("privateMessage", privateMessage);
                    try{
                    Controller.out.writeObject(data);
                    Controller.out.flush();
                    field.setText("");
                    }
                    catch(IOException e){
                        area.appendText("Couldn't send private message to "+nick);
                        area.appendText(e.toString());
                        e.printStackTrace();
                        
                    }
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
    
    public void appendToArea(String message){
        area.appendText(message+"\n");
    }    
    
}