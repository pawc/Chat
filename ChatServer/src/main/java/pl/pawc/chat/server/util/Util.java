package pl.pawc.chat.server.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import pl.pawc.chat.server.MainServer;
import pl.pawc.chat.server.model.Client;
import pl.pawc.chat.shared.Data;

public class Util {

	public static void broadcast(Data data){
		MainServer.clients.forEach(client -> {
			try{
				client.out.writeObject(data);
				client.out.flush();
			}
			catch(IOException e){
				MainServer.logger.error("Couldn't send {} to {}: {}", data.getCommand(), client.getNick(), e);
			}
		});
	}

	public static void sendMessage(String message){
		broadcast(new Data("message", message));
	}
	
	public static void sendMessageToNicks(String nick1, String nick2, Data data) throws IOException {
		MainServer.clients.stream()
			.filter(client -> client.getNick().equals(nick1)||client.getNick().equals(nick2))
			.forEach(client -> {
				try {
					client.out.writeObject(data);
					client.out.flush();
				}
				catch (IOException e) {
					MainServer.logger.error("Couldn't send message from {} to {}: {}", nick1, nick2, e);
				}
			});
	}
	
	public static void sendNicksToAll(){
		List<String> list = MainServer.clients.stream().map(Client::getNick).collect(Collectors.toList());
	    Data data = new Data("nicks", list);
		broadcast(data);
    }

	public static boolean checkIfNickAlreadyInUse(String nick) {
		return MainServer.clients.stream().anyMatch(client -> client.getNick().equals(nick));
	}

}