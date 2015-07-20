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
			String nick = client.getBufferedReader().readLine();
			if(!nick.startsWith("*")) client.setNick(nick);
			else{
				client.getDataOutputStream().writeBytes("Choose a different nick\n");
				client.exit();
				Main.clientContainer.remove(this.client);
				this.interrupt();
				}
			
			//sending all nicks to all
			String nicks = "*";
			
			for(Client client : Main.clientContainer){
				nicks+=client.getNick()+"*";
			}
				
			for(Client client : Main.clientContainer){
				client.getDataOutputStream().writeBytes(nicks+"\n");
			}
				
			
			//main loop
			client.getDataOutputStream().writeBytes("Welcome to the echo server. Type quit to exit\n");
			while(((line=client.getBufferedReader().readLine())!=null)&&!line.equals("quit")){
				for(Client client : Main.clientContainer){
					client.getDataOutputStream().writeBytes(this.client.getNick()+": "+line+"\n");
				}
			}
			//exiting
			
			for(Client client : Main.clientContainer){
				client.getDataOutputStream().writeBytes("**"+this.client.getNick()+"\n");
			}
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
