/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * This class tests the TollboothLogger class.
 *
 * @version March 19 2016
 */
public class TollboothLoggerTest {

    /**
     * Tests creating a TollboothLogger, adding a message to the logger and
     * removing the message.
     */
    @Test
    public void addMessageToLogger() {
        final TollboothLogger log = new TollboothLogger();
        final LogMessage msg1 = new LogMessage("Failure");
        log.accept(msg1);

        final LogMessage getMsg = log.getNextMessage();
        assertNotNull(getMsg);
        assertEquals(msg1, getMsg);
    }

    /**
     * Tests creating a new TollboothLogger with no messages.
     */
    @Test
    public void createNewEmptyLogger() {
        final TollboothLogger log = new TollboothLogger();
        assertNotNull(log);
    }

    /**
     * Tests adding a null message to the TollBoothLogger.
     */
    @Test
    public void testAddNullMessage() {
        final ExpectedException thrown = ExpectedException.none();
        final TollboothLogger log = new TollboothLogger();
        log.accept(null);
        thrown.expect(NullPointerException.class);
        assertNotNull(log);
    }

    /**
     * Tests adding messages to the TollboothLogger, and retrieving them to
     * assert they were stored in the order they were added.
     */
    @Test
    public void testMessageOrderInLogger() {
        final TollboothLogger log = new TollboothLogger();
        final int MESSAGES = 5;

        // Add messages with message of the for loop counter
        for (int i = 0; i < MESSAGES; i++) {
            log.accept(new LogMessage(i + ""));
        }

        // Compare the messages for each message to the next one to see if the
        // numbers are increasing in sequential order, as they were added.
        LogMessage prev = null;
        for (int i = 0; i < MESSAGES; i++) {
            LogMessage msg = log.getNextMessage();
            assertNotNull(msg);

            // Skip first since there is no previous message
            if (prev != null) {
                try {
                    assertEquals(Integer.parseInt(prev.getMessage()) + 1,
                            Integer.parseInt(msg.getMessage()));
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Shouldn't happen, but if it does let
                                         // us see what did happen
                }
            }

            // Set new previous
            prev = msg;
        }
    }
}
