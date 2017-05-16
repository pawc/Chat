package pl.pawc.chat.shared;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CryptoTest extends TestCase{
	
    public CryptoTest(String testName){
        super(testName);
    }

    public static Test suite(){
        return new TestSuite(CryptoTest.class);
    }
    
    public void testCrypto(){
    	Crypto crypto = new Crypto("E1BB465D57CAE7ACDBBE8091F9CE83DF");
    	String input = "text to process";
    	String encoded = crypto.encrypt(input);
    	String decoded = crypto.decrypt(encoded);
    	assertEquals(input, decoded);    	
    }
    
}