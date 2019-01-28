package org.windwant.spring;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.windwant.util.ConfigUtil;

/**
 * Unit test for simple BootClient.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public static void main(String[] args) {
        System.out.println(ConfigUtil.get("wsserver.port"));
    }
}
