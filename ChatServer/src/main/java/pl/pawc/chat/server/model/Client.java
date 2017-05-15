package pl.pawc.chat.server.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pl.pawc.chat.server.Main;

public class Client {
	
	private String nick;
	private Socket socket;
	public ObjectOutputStream out;
	public ObjectInputStream in;

	
	public Client(Socket socket){
		nick = "";
		this.socket=socket;
		Main.log.info("Initializing streams for client "+socket.getInetAddress().getHostAddress().toString());
		try{
		    out = new ObjectOutputStream(socket.getOutputStream());
		    out.flush();
		    in = new ObjectInputStream(socket.getInputStream());
		}
		catch(IOException e){
		    Main.log.warning("Error initializing streams");
		    out = null;
		    in = null;
		}
		Main.log.info("Streams initialized.");
	}
	
    public boolean exit(){
		try{
			Main.log.info("Client "+toString()+" exited. Closing socket.");
			this.socket.close();
            Main.log.info("Socket closed");
			return true;
		}
		catch(IOException e){
			Main.log.warning("Error closing socket "+e.toString());
			return false;
		}
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	
	public String getNick(){
		return nick;
	}
	
	public void setNick(String nick){
		this.nick=nick;
	}
	
	public String toString(){
		return nick+" ["+getSocket().getInetAddress().toString();
	}
	

}
