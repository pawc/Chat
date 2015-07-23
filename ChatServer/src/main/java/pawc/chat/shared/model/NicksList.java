package pawc.chat.shared.model;

import java.util.List;

public class NicksList {

    private List nicks;
    
    public NicksList(List nicks){
        this.nicks=nicks;
    }
    
    public List getNicks(){
        return nicks;
    }
    
}
