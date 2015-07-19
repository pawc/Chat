package pawc.chat.server;

import java.io.IOException;
import pawc.chat.server.model.Client;

public class SocketHandler extends Thread{

	private Client client;
	
	public SocketHandler(Client client){
		this.client=client;
	}
	
	
	public void run(){
		String line = "";
		try{
			//retreiving nick
			client.setNick(client.getBufferedReader().readLine());
			
			//main loop
			client.getDataOutputStream().writeBytes("Welcome to the echo server. Type quit to exit\n");
			while(((line=client.getBufferedReader().readLine())!=null)&&!line.equals("quit")){
				for(Client client : Main.clientContainer){
					client.getDataOutputStream().writeBytes(this.client.getNick()+": "+line+"\n");
				}
			}
			//exiting
			
			client.exit();
			Main.clientContainer.remove(this.client);
			
		}
		catch(IOException e){
			Main.log.warning("Error with socket "+client.toString()+" : "+e.toString());
			this.interrupt();
		}
		Main.log.info("Exiting thread");
		interrupt();
	}
	
}
