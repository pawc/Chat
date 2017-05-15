package pawc.chat.client.controller;

import org.mockito.Mockito;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pl.pawc.chat.client.controller.Connection;
import pl.pawc.chat.client.controller.Controller;

/**
 * Unit test for simple App.
 */
public class ConnectionTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConnectionTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ConnectionTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testConnection()
    {
    	Controller controller = Mockito.mock(Controller.class);
    	Connection connection = new Connection(controller);
    	assertTrue(connection instanceof Connection);
    	
    	
    }
}
