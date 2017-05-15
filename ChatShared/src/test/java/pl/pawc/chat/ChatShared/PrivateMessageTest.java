package pl.pawc.chat.shared;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PrivateMessageTest extends TestCase{

    public PrivateMessageTest(String name){
        super(name);
    }
    
    public static Test suite(){
        return new TestSuite(PrivateMessageTest.class);
    }
    
    public void testPrivateMessage(){
        PrivateMessage message = new PrivateMessage("john", "paul", "hello");
        assertTrue(message instanceof PrivateMessage);
        assertTrue(message.getSender().equals("john"));
        assertTrue(message.getRecipient().equals("paul"));
        assertTrue(message.getMessage().equals("hello"));
    }
    
}