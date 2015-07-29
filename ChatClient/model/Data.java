package pawc.chat.shared.model;

public class Data {

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
