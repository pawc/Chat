package pl.pawc.chat.server.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import lombok.Getter;
import pl.pawc.chat.server.MainServer;

@Getter
public class Client {
	
	private String nick;
	private final Socket socket;
	public ObjectOutputStream out;
	public ObjectInputStream in;

	public Client(Socket socket){
		this.socket = socket;
		MainServer.logger.info("Initializing streams for client {} ", socket.getInetAddress().getHostAddress());
		try{
		    out = new ObjectOutputStream(socket.getOutputStream());
		    out.flush();
		    in = new ObjectInputStream(socket.getInputStream());
		    MainServer.logger.info("Streams initialized.");
		}
		catch(IOException e){
		    MainServer.logger.error("Error initializing streams: {}", e.toString());
			e.printStackTrace();
		}
	}
	
    public void exit(){
		MainServer.logger.info("Client {} exited. Closing socket.", this.nick);
		try {
			this.socket.close();
		} catch (IOException e) {
			MainServer.logger.error(e);
		}
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String toString(){
		return nick+" ["+getSocket().getInetAddress().toString();
	}	

}