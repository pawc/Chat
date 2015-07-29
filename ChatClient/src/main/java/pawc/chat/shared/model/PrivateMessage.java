package pawc.chat.shared.model;

public class PrivateMessage {

    private String sender;
    private String recipient;
    private String message;
    
    public PrivateMessage(String sender, String recipient, String message){
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }
    
}
