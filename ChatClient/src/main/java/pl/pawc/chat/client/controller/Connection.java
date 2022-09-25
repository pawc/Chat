package pl.pawc.chat.client.controller;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.control.TextArea;
import pl.pawc.chat.shared.Data;
import pl.pawc.chat.shared.PrivateMessage;

public class Connection extends Thread {

	private final TextArea area;

	public Connection(TextArea area) {
		this.area = area;
	}

	public void run(){

		if(Controller.nick.equals("")){
			log("Set your nick in Chat>Settings and try again");
			return;
		}

		if(Controller.host.equals("")){
			log("Set host in Chat>Settings and try again");
			return;
		}

		if(Controller.connected){
			log("Already connected");
			return;
		}

		try{
			Controller.socket = new Socket(Controller.host, Controller.port);
			Controller.connected = true;
		}
		catch(IOException e){
			log("Couldn't connect to the server");
			log(e.toString());
			return;
		}

		log("Connected");

		ObjectInputStream in;
		ObjectOutputStream out;

		try{
			out = new ObjectOutputStream(Controller.socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(Controller.socket.getInputStream());
			Controller.out = out;
		}
		catch(IOException e){
			log("Couldn't initialize streams");
			log(e.toString());
			return;
		}

		Data dataNick = new Data("introduction", Controller.nick);
		GregorianCalendar calendar = new GregorianCalendar();

		try{
			// sending an introduction to the server
			out.writeObject(dataNick);
			while(Controller.connected){
				// main loop receiving Data from the server
				Data data = (Data) in.readObject();
				String command = data.getCommand();

				int hours = calendar.toZonedDateTime().getHour();
				int minutes = calendar.toZonedDateTime().getMinute();
				String time = hours+":"+minutes;
				if(command.equals("message")) {
					String message = (String) data.getArguments();
					message = Controller.crypto.decrypt(message);
					message = message.replace(":)", "\u263a");
					message = message.replace(":(", " \u2639");
					this.area.appendText(time+" "+message);
					if(!message.startsWith(Controller.nick)) sound("message");
				}
				if(command.equals("nicks")){
					Controller.removeNicks();
					List<String> nicks  = (List<String>) data.getArguments();
					Controller.addNicks(nicks);
					sound("changeInNicksList");
				}
				if(command.equals("NickAlreadyInUse")){
					this.area.appendText("Nick already in use. Choose a different one and reconnect\n");
					break;
				}
				if(command.equals("privateMessage")){
					PrivateMessage privateMessage = (PrivateMessage) data.getArguments();
					String sender = privateMessage.getSender();
					String recipient = privateMessage.getRecipient();
					String message = privateMessage.getMessage();
					message = Controller.crypto.decrypt(message);
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
					  if(Controller.isPMalreadyOpened(sender)){
						PrivateMessagePaneController c = returnPMcontrollerOfANick(sender);
						if(c==null) log("Error with PM controller of "+sender);
						else c.appendToArea(time+" "+sender+": "+message);
						sound("privateMessage");
					  }
					  else{
						PrivateMessagePaneController c = new PrivateMessagePaneController(sender);
						Controller.privateMessagePaneControllerContainer.add(c);
						String initialMessage = time+" "+sender+": "+message;
						  Controller.openNewPrivateWindow(c, initialMessage);
						sound("privateMessage");
					   }
					}
				}
			}
			out.close();
			in.close();
			Controller.socket.close();
			Controller.connected = false;
		}
		catch(NullPointerException | IOException | ClassNotFoundException e){
			log(e +". Disconnecting");
			e.printStackTrace();
			Controller.connected=false;
		}

	}
	
	public PrivateMessagePaneController returnPMcontrollerOfANick(String nick){

		return Controller.privateMessagePaneControllerContainer.stream()
				.filter(pmController -> pmController.getNick().equals(nick))
				.findFirst().orElse(null);

	}
	
	private void sound(String soundName){
        new Thread(() -> {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(
						new BufferedInputStream(
							Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("sounds/" + soundName + ".wav"))
						)
					);
				clip.open(inputStream);
				clip.start();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}

		}).start();
    }

	protected void log(String string){
		area.appendText(string+"\n");
	}
	
}
