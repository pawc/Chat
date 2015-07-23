package pawc.chat.shared.model;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable{
    
    private String command;
    private List arguments;

    public Data(String command, List arguments){
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
