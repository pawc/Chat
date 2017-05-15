package pl.pawc.chat.shared;

import java.io.Serializable;

public class PrivateMessage implements Serializable {

    private String sender;
    private String recipient;
    private String message;
    
    public PrivateMessage(String sender, String recipient, String message){
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }
    public String getSender(){
        return sender;
    }
    
    public String getRecipient(){
        return recipient;
    }
    
    public String getMessage(){
        return message;
    }
}
