package pl.pawc.chat.server.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.pawc.chat.server.Main;
import pl.pawc.chat.server.model.Client;
import pl.pawc.chat.shared.Data;

public class Util {

	public void sendMessageToAll(String message){
	    
	    Data data = new Data("message", message);
	    
        for(Client client : Main.clientContainer){
            try{
                client.out.writeObject(data);
                client.out.flush();
            }
            catch(IOException e){
                e.printStackTrace();
                continue;
            }
            
        }
	    
	}
	
	public void sendMessageToNicks(String nick1, String nick2, Data data) throws IOException{
	    for(Client client : Main.clientContainer){
	        if(client.getNick().equals(nick1)||client.getNick().equals(nick2)){
	            client.out.writeObject(data);
	            client.out.flush();	            
	        }
	    }
	}
	
	public void sendNicksToAll(){
	    List list = new ArrayList<String>();
	    for(Client client : Main.clientContainer){
	        list.add(client.getNick());
	    }
	    Data data = new Data("nicks", list);
	    
	    for(Client client : Main.clientContainer){
	        try{
	            client.out.writeObject(data);
	            client.out.flush();
	        }
	        catch(IOException e){
	            Main.log.warning("Error sending nicks list to: "+client.getNick());
	            e.printStackTrace();
	            continue;
	        }
        }
    }

	public boolean checkIfNickAlreadyInUse(String nick){
	    boolean answer = false;
	    for(Client client : Main.clientContainer){
	        if(client.getNick().equals(nick)) answer = true;
	        break;
	    }
	    return answer;
	}
	
}