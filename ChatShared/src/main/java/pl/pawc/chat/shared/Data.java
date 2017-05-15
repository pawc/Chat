package pl.pawc.chat.shared;

import java.io.Serializable;

public class Data implements Serializable {

    String command;
    Object arguments;
    
    public Data(String command, Object arguments){
        this.command=command;
        this.arguments=arguments;
    }
    
    public String getCommand(){
        return command;
    }
    
    public Object getArguments(){
        return arguments;
    }
    
}
