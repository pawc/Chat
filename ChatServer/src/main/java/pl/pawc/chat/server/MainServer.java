package pl.pawc.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawc.chat.server.model.Client;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.X509KeyManager;

public class MainServer {

	public static final Logger logger = LogManager.getLogger(MainServer.class);
	private final static int DEFAULT_PORT = 3000;
	protected static ServerSocket serverSocket = null;
	public static ArrayList<Client> clients = new ArrayList<>();
	public static boolean isRunning = true;
	
	public static void main(String[] args){
	    int port;
		
		try{
			port = Integer.parseInt(args[0]);
		}
		catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
			logger.info("Starting server using default port {}", DEFAULT_PORT);
			port = DEFAULT_PORT;
		}

		try{
			SSLServerSocketFactory sslServerSocketFactory = getSSLServerSocketFactory();
			serverSocket = sslServerSocketFactory.createServerSocket(port);
		}
		catch(Throwable e){
			logger.error("Couldn't start server on port {}", port);
			isRunning = false;
			return;
		}

		new SocketListener(serverSocket).start();

		logger.info("Server listening on port {}. Awaiting connections...", port);
	}

	private static SSLServerSocketFactory getSSLServerSocketFactory() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableKeyException, KeyManagementException {
		System.setProperty("javax.net.debug", "ssl");
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		String password = "zllp8jZVynHjEvLuVgIB";
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jschat-server.p12");
		keyStore.load(inputStream, password.toCharArray());

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
		keyManagerFactory.init(keyStore, password.toCharArray());
		X509KeyManager x509KeyManager = null;
		for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
			if (keyManager instanceof X509KeyManager) {
				x509KeyManager = (X509KeyManager) keyManager;
				break;
			}
		}
		if (x509KeyManager == null) throw new NullPointerException();

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(new KeyManager[]{x509KeyManager}, null, null);

		return sslContext.getServerSocketFactory();

	}
	
}