package pl.pawc.chat.shared;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DataTest 
    extends TestCase
{
    public DataTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( DataTest.class );
    }
    
    public void testData()
    {
        List list = new ArrayList<String>();
        list.add("first nick");
        list.add("second nick");
        Data data = new Data("nicks", list);
        assertTrue(data instanceof Data);
        assertTrue(data.getCommand().equals("nicks"));
        assertTrue(data.getArguments() instanceof List);
        List readList = (List) data.getArguments();
        assertTrue(readList.get(0).equals("first nick"));
        assertTrue(readList.get(1).equals("second nick"));
        
    }
}
