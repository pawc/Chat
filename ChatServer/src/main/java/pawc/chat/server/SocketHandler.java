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
			if(!nick.contains("-")) client.setNick(nick);
			else{
				client.getDataOutputStream().writeBytes("Choose a different nick\n");
				client.exit();
				Main.clientContainer.remove(this.client);
				this.interrupt();
				}
			
			//sending all nicks to all
			sendNicksToAll();
				
			
			//main loop
			client.getDataOutputStream().writeBytes("Welcome to the chat server. Type quit to exit\n");
			while(((line=client.getBufferedReader().readLine())!=null)&&!line.equals("quit")){
				for(Client client : Main.clientContainer){
					client.getDataOutputStream().writeBytes(this.client.getNick()+": "+line+"\n");
				}
			}
			//exiting
			
			Main.clientContainer.remove(this.client);
			sendNicksToAll();
			client.exit();
			
			
		}
		catch(IOException e){
			Main.log.warning("Error with socket "+client.toString()+" : "+e.toString());
			this.interrupt();
		}
		Main.log.info("Exiting thread");
		interrupt();
	}


	private void sendNicksToAll() throws IOException {
		String nicks = "-";
		
		for(Client client : Main.clientContainer){
			nicks+=client.getNick()+"-";
		}
			
		for(Client client : Main.clientContainer){
			client.getDataOutputStream().writeBytes(nicks+"\n");
		}
	}
	
	
}
