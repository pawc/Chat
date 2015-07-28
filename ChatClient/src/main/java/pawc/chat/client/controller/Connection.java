package pawc.chat.client.controller;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import pawc.chat.client.controller.Controller;
import pawc.chat.shared.model.Data;

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
	    		controller.connected = true;
	    	}
	    	catch(IOException e){
	    		controller.log("Couldn't connect to the server");
	    		controller.log(e.toString());
	    		return;
	    	}
	    	
	    	controller.log("Connected");
	    	controller.log("Initializing streams");
	    	ObjectInputStream in;
	    	ObjectOutputStream out;
	    	
	    	try{
	    	in = new ObjectInputStream(controller.getSocket().getInputStream());
	    	out = new ObjectOutputStream(controller.getSocket().getOutputStream());
	    	}
	    	catch(IOException e){
    	        controller.log("Couldn't initialize streams");
                controller.log(e.toString());
                return; 
	    	}
	    	controller.log("Streams initialized");
	    	
	    	
	    	Data dataNick = new Data("introduction", controller.nick);
	    	
	    	try{
	    	    out.writeObject(dataNick);
	    	    out.close();
	    	    controller.log("Nick sent. Entering main loop");
    	    	while(controller.connected){
    	    	    Data data = (Data) in.readObject();
    	    	    String command = data.getCommand();
    	    	    if(command.equals("message")) controller.area.appendText((String) data.getArguments());
    	    	    if(command.equals("nicks")){
    	    	        controller.removeNicks();
    	    	        controller.addNicks((List) data.getArguments());
    	    	    }
    	    	}
	    	}
	    	catch(IOException | ClassNotFoundException | NullPointerException e){
	    	    controller.log(e.toString());
                controller.connected=false;
                return;
	    	}
	
	}
}
