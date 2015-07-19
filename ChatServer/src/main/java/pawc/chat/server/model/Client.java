package pawc.chat.server.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import pawc.chat.server.Main;

public class Client {
	
	private String nick;
	private BufferedReader bfr;
	private DataOutputStream out;
	private Socket socket;

	
	public Client(Socket socket){
		nick = "";
		this.socket=socket;
		
		try{
			Main.log.info("Constructing streams for incoming client connection");
			bfr = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new DataOutputStream(this.socket.getOutputStream());
		}
		catch(IOException e){
			Main.log.warning("Error constructing streams");
			System.exit(0);
		}
		Main.log.info("Streams constructed successfully");
	}
	
	//constructor for tests only
	public Client(BufferedReader bfr, DataOutputStream out, Socket socket){
		this.bfr = bfr;
		this.out = out;
		this.socket = socket;
		nick = "";
	}
	
	public boolean exit(){
		try{
			Main.log.info("Client "+toString()+" exited. Closing streams...");
			this.getBufferedReader().close();
			this.getDataOutputStream().close();
			this.socket.close();
			
			Main.log.info("Streams closed");
			
			return true;
		}
		catch(IOException e){
			Main.log.warning("Error closing streams "+e.toString());
			return false;
		}
	}
	
	public BufferedReader getBufferedReader(){
		return bfr;
	}
	
	public DataOutputStream getDataOutputStream(){
		return out;
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
		return nick+" ["+getSocket().getInetAddress().toString()+"]";
	}
	

}
