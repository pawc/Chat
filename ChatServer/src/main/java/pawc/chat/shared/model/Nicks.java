package pawc.chat.shared.model;

import java.util.List;

public class Nicks {

    private List nicks;
    
    public Nicks(List nicks){
        this.nicks=nicks;
    }
    
    public List getNicks(){
        return nicks;
    }
    
}
