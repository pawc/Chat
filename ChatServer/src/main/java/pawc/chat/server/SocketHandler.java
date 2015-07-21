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
				Main.clientThreadsContainer.remove(this);
				this.interrupt();
				}
			sendNicksToAll();
			//main loop
			client.getDataOutputStream().writeBytes("Welcome to the chat server. Type quit to exit\n");
			while(((line=client.getBufferedReader().readLine())!=null)&&!line.equals("quit")){
				for(SocketHandler socketHandler : Main.clientThreadsContainer){
					socketHandler.getClient().getDataOutputStream().writeBytes(this.client.getNick()+": "+line+"\n");
					//client.getDataOutputStream().writeBytes(this.client.getNick()+": "+line+"\n");
				}
			}
			//exiting
			
			Main.clientThreadsContainer.remove(this);
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


	public void sendNicksToAll(){
		String nicks = "-";
		for(SocketHandler socketHandler : Main.clientThreadsContainer){
			nicks+=socketHandler.getClient().getNick()+"-";
		}
		for(SocketHandler socketHandler : Main.clientThreadsContainer){
			try{
				socketHandler.getClient().getDataOutputStream().writeBytes(nicks+"\n");
			}
			catch(IOException e){
				Main.log.warning("Couldn't send nicks to "+socketHandler.getClient().getSocket().getInetAddress());
				continue;
			}
		}
	}
	
	public Client getClient(){
		return client;
	}
	
}
