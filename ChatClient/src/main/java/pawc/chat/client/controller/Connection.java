package pawc.chat.client.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.GregorianCalendar;

import pawc.chat.client.controller.Controller;

public class Connection extends Thread {
	
	Controller controller;

	public Connection(Controller controller){
		this.controller=controller;
	}
	
	public void run(){
		
	    	if(controller.nick.equals("")){ 
	    		controller.log("Set your nick in Chat->Settings and try again");
	    		return;
	    	}
	    	
	    	if(controller.host.equals("")){ 
	    		controller.log("Set host in Chat->Settings and try again");
	    		return;
	    	}
	    	
	    	if(controller.connected){ 
	    		controller.log("Already connected");
	    		return;
	    	}
	    	
	    	try{
	    		controller.socket = new Socket(controller.host, controller.port);
	    	}
	    	catch(IOException e){
	    		controller.log("Couldn't connect to the server");
	    		controller.log(e.toString());
	    		return;
	    	}
	    	
	    	  
	
	
	}
}
