package pawc.chat.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

import org.mockito.Mockito;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pawc.chat.server.model.Client;

public class ClientTest 
    extends TestCase
{
	/*
    public ClientTest( String testName )
    {
        super( testName );
    }


    public static Test suite()
    {
        return new TestSuite( ClientTest.class );
    }
    */

    public void testClient()
    {
    	BufferedReader bfr = Mockito.mock(BufferedReader.class);
    	DataOutputStream out = Mockito.mock(DataOutputStream.class);
    	Socket socket = Mockito.mock(Socket.class);
    	Client client = new Client(bfr, out, socket);
    	assertTrue(client instanceof Client);
    	assertTrue(client.getBufferedReader() instanceof BufferedReader);
    	assertTrue(client.getDataOutputStream() instanceof DataOutputStream);
    	assertTrue(client.getSocket() instanceof Socket);
    	client.setNick("pawc");
    	assertEquals("pawc", client.getNick());
    	
    	
    }
}
