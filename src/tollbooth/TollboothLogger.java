/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * The TollboothLogger implements a FIFO queue to store and retrieve log
 * messages that occur during the Tollbooth application.
 *
 * @version March 19 2016
 */
public class TollboothLogger implements SimpleLogger {
    private final LinkedList<LogMessage> list;

    /**
     * Creates a TollboothLogger instance.
     */
    public TollboothLogger() {
        list = new LinkedList<LogMessage>();
    }

    /**
     * (non-Javadoc)
     *
     * @see tollbooth.SimpleLogger#accept(tollbooth.LogMessage)
     * @throws NullPointerException
     *             If the message is null.
     */
    @Override
    public void accept(LogMessage message) throws NullPointerException {
        list.addLast(message); // Add to the end
    }

    /**
     * (non-Javadoc)
     *
     * @see tollbooth.SimpleLogger#getNextMessage()
     */
    @Override
    public LogMessage getNextMessage() {
        try {
            return list.removeFirst();
        } catch (NoSuchElementException e) {
            return null; // List is empty
        }
    }

}
