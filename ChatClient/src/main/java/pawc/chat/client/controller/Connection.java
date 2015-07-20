package pawc.chat.client.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
	    	try{
	    		controller.log("Connected to the host. Initializing streams");
	    		controller.bfr = new BufferedReader(new InputStreamReader(controller.socket.getInputStream()));
	    		controller.out = new DataOutputStream(controller.socket.getOutputStream());
	    	}
	    	catch(IOException e){
	    		controller.log("Couldn't initialize streams");
	    		controller.log(e.toString());
	    		return;
	    	}
	    
	    	try{
	    		controller.out.writeBytes(controller.nick+"\n"); // moze byc problem z \n
	    		controller.connected=true;
	    		String line = "";
	    		
	    		while(controller.connected&&line!=null){
	    			
		    			try{
		    				line = controller.bfr.readLine();
		    				//if(line!=null&&controller.connected&&line.startsWith("-")) handleNicks(line);
				    		
			    			if(line!=null) controller.log(line);
			    			
		    			}
		    			catch(NullPointerException e){
		    				break;
		    			}
		    	    
		    		
	    			
	    		}
	    		
	    		controller.log("Disconnected from the server");
	    		controller.connected=false;
	    		
	    	}
	    	catch(IOException e){
	    		controller.log("Error. Connect again");
	    		controller.log(e.toString());
	    		controller.connected = false;
	    		return;
	    	}
	}
	/*
	public void handleNicks(String line){
		String[] nicks = line.split("-");
		controller.observableList.clear();
		for(int i=1; i<nicks.length; i++){
			controller.observableList.add(nicks[i]);
		}
	}
	*/
	

}
