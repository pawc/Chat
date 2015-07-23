package pawc.chat.shared.model;

public class Message {
    
    private String message;
    private String author;
    
    public Message(String message, String author){
        this.message=message;
        this.author=author;
    }
    
    public String getMessage(){
        return message;
    }
    
    public String getAuthor(){
        return author;
    }
    
    public String toString(){
        return author+": "+message;
    }

}
