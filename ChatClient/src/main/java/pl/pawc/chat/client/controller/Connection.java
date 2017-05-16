package pl.pawc.chat.client.controller;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import pl.pawc.chat.shared.Data;
import pl.pawc.chat.shared.PrivateMessage;
import pl.pawc.chat.client.controller.Controller;

public class Connection extends Thread {
	
	private Controller controller;

	public Connection(Controller controller){
		this.controller=controller;
	}
	
	public void run(){
		
	    	if(controller.nick.equals("")){ 
	    		controller.log("Set your nick in Chat>Settings and try again");
	    		return;
	    	}
	    	
	    	if(controller.host.equals("")){ 
	    		controller.log("Set host in Chat>Settings and try again");
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
	    	
	    	Data dataNick = new Data("introduction", controller.nick);
	    	
	    	try{
	    	    // sending an introduction to the server
	    	    out.writeObject(dataNick);
	    	    while(controller.connected){
	    	        // main loop receiving Data from the server
    	    	    Data data = (Data) in.readObject();
    	    	    String command = data.getCommand();
    	    	    GregorianCalendar calendar = new GregorianCalendar();
    	    	    int hours = calendar.getTime().getHours();
    	    	    int minutes = calendar.getTime().getMinutes();
    	    	    String time = hours+":"+minutes;
    	    	    if(command.equals("message")) {
    	    	        String message = (String) data.getArguments();
    	    	        message = controller.crypto.decrypt(message);
    	    	        message = message.replace(":)", "\u263a");
    	    	        message = message.replace(":(", " \u2639");
    	    	        controller.area.appendText(time+" "+message);
    	    	        if(!message.startsWith(Controller.nick)) sound("message");
    	    	    }
    	    	    if(command.equals("nicks")){
    	    	        controller.removeNicks();
    	    	        controller.addNicks((List) data.getArguments());
    	    	        sound("changeInNicksList");
    	    	    }
    	    	    if(command.equals("NickAlreadyInUse")){
    	    	        controller.area.appendText("Nick already in use. Choose a different one and reconnect\n");
    	    	        break;
    	    	    }
    	    	    if(command.equals("privateMessage")){
    	    	        PrivateMessage privateMessage = (PrivateMessage) data.getArguments();
    	    	        String sender = privateMessage.getSender();
    	    	        String recipient = privateMessage.getRecipient();
    	    	        String message = privateMessage.getMessage();
    	    	        message = message.replace(":)", "\u263a");
                        message = message.replace(":(", " \u2639");
    	    	        
    	    	        if(sender.equals(Controller.nick)){
    	    	            // when this client sent the private message
    	    	            PrivateMessagePaneController c = returnPMcontrollerOfANick(recipient);
    	    	            c.appendToArea(time+" "+sender+": "+message);
    	    	            continue;
    	    	        }
    	    	        if(recipient.equals(Controller.nick)){
    	    	            // when the private message is addressed to this client
    	    	          if(controller.isPMalreadyOpened(sender)){
    	    	            PrivateMessagePaneController c = returnPMcontrollerOfANick(sender);
    	    	            if(c==null) controller.log("Error with PM controller of "+sender);
    	    	            c.appendToArea(time+" "+sender+": "+message);
    	    	            sound("privateMessage");
    	    	          }
    	    	          else{
    	    	            PrivateMessagePaneController c = new PrivateMessagePaneController(sender);
    	    	            Controller.privateMessagePaneControllerContainer.add(c);
    	    	            String initialMessage = time+" "+sender+": "+message;
    	    	            controller.openNewPrivateWindow(c, initialMessage);
    	    	            sound("privateMessage");
    	    	           }
    	    	        }
    	    	    }
    	    	}
    	    	out.close();
    	    	in.close();
    	    	controller.socket.close();
    	    	controller.connected = false;
	    	}
	    	catch(NullPointerException e){
	    	    controller.log(e.toString()+". Disconnecting");
                e.printStackTrace();
                controller.connected=false;
                return;
	    	}
	    	catch(IOException | ClassNotFoundException e){
	    	    controller.log(e.toString()+". Disconnecting");
	    	    e.printStackTrace();
                controller.connected=false;
                return;
	    	}
	
	}
	
	public PrivateMessagePaneController returnPMcontrollerOfANick(String nick){
	    
	    for(PrivateMessagePaneController c : Controller.privateMessagePaneControllerContainer){
	        if(c.getNick().equals(nick)) return c;
	        //break
	    }
	    return null;
	}
	
	private void sound(String soundName){
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(ClassLoader.getSystemResourceAsStream("sounds/"+soundName+".wav")));
                    clip.open(inputStream);
                    clip.start();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                    e.printStackTrace();
                }
                
            }
        }).start();
    }
	
}
