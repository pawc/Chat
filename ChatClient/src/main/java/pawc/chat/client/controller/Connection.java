package pawc.chat.client.controller;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.GregorianCalendar;
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
	    	//controller.log("Initializing streams");
	    	ObjectInputStream in;
	    	ObjectOutputStream out;
	    	
	    	
	    	try{
	    	out = new ObjectOutputStream(controller.getSocket().getOutputStream());
	    	out.flush();
	    	in = new ObjectInputStream(controller.getSocket().getInputStream());
	    	controller.out = out;
	    	
	    	}
	    	catch(IOException e){
    	        controller.log("Couldn't initialize streams");
                controller.log(e.toString());
                return; 
	    	}
	    	//controller.log("Streams initialized");
	    	
	    	Data dataNick = new Data("introduction", controller.nick);
	    	
	    	try{
	    	    out.writeObject(dataNick);
	    	    //controller.log("Nick sent. Entering main loop");
    	    	while(controller.connected){
    	    	    Data data = (Data) in.readObject();
    	    	    String command = data.getCommand();
    	    	    GregorianCalendar calendar = new GregorianCalendar();
    	    	    int hours = calendar.getTime().getHours();
    	    	    int minutes = calendar.getTime().getMinutes();
    	    	    String time = hours+":"+minutes;
    	    	    if(command.equals("message")) controller.area.appendText(time+" "+(String) data.getArguments());
    	    	    if(command.equals("nicks")){
    	    	        controller.removeNicks();
    	    	        controller.addNicks((List) data.getArguments());
    	    	    }
    	    	    if(command.equals("NickAlreadyInUse")){
    	    	        controller.area.appendText("Nick already in use. Choose a different one and reconnect\n");
    	    	        break;
    	    	    }
    	    	}
    	    	out.close();
    	    	in.close();
    	    	controller.socket.close();
    	    	controller.connected = false;
	    	}
	    	catch(IOException | ClassNotFoundException | NullPointerException e){
	    	    controller.log(e.toString());
                controller.connected=false;
                return;
	    	}
	
	}
}
