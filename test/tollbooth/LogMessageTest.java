/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the LogMessage class.
 *
 * @version March 19 2016
 */
public class LogMessageTest {

    /**
     * Tests the complete {@link LogMessage} constructor.
     */
    @Test
    public void logMessageTestCompleteConstructor() {
        LogMessage msg = new LogMessage("Hi", new Exception("Hi"));
        assertNotNull(msg);

    }

    /**
     * Tests getting the throwable cause from the {@link LogMessage} class.
     */
    @Test
    public void logMessageTestGetCause() {
        LogMessage msg = new LogMessage("Hi", new Exception("Hi"));
        assertEquals("Hi", msg.getCause().getMessage());
        assertTrue(msg.hasCause());
    }

    /**
     * Tests getting the message from the {@link LogMessage} class.
     */
    @Test
    public void logMessageTestGetMessage() {
        LogMessage msg = new LogMessage("Hi", new Exception("Hi"));
        assertEquals("Hi", msg.getMessage());
    }

    /**
     * Tests the {@link LogMessage} constructor.
     */
    @Test
    public void logMessageTestMessageConstructor() {
        LogMessage msg = new LogMessage("Hi");
        assertNotNull(msg);
        assertFalse(msg.hasCause());
    }
}
