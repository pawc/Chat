package pawc.chat.shared.model;

import java.util.List;

public class Request {
    
    private String command;
    private List arguments;

    public Request(String command, List arguments){
        this.command=command;
        this.arguments=arguments;
    }
    
    public String getCommand(){
        return command;
    }
    
    public List getArguments(){
        return arguments;
    }
    
}
