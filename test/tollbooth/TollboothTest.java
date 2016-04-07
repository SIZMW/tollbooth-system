/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The
 * course was taken at Worcester Polytechnic Institute. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Copyright
 * ©2016 Gary F. Pollice
 *******************************************************************************/

package tollbooth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tollbooth.gatecontroller.AllExceptIsOpenMalfunctionGateController;
import tollbooth.gatecontroller.AlternateMalfunctionGateController;
import tollbooth.gatecontroller.GateController;
import tollbooth.gatecontroller.IsOpenMalfunctionGateController;
import tollbooth.gatecontroller.MalfunctionGateController;
import tollbooth.gatecontroller.PerfectGateController;
import tollbooth.gatecontroller.ResetMalfunctionGateController;
import tollbooth.gatecontroller.TestGateController;

/**
 * Test cases for the Tollbooth, TollGate class.
 *
 * @version Feb 3, 2016
 */
public class TollboothTest {

    /**
     * Creates a new TollGate with a controller but no logger.
     */
    @Test
    public void createTollGateWithController() {
        assertNotNull(new TollGate(new TestGateController(), null));
    }

    /**
     * Creates a new TollGate without a controller.
     */
    @Test
    public void createTollGateWithNoParameters() {
        assertNotNull(new TollGate(null, null));
    }

    /**
     * Creates a new TollGate with a alternating malfunction controller. The
     * gate malfunctions once before closing correctly. The logs and statistics
     * are checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestCloseAfterOneMalfunction() throws TollboothException {
        final GateController controller = new AlternateMalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // We have to do the open and let it retry since the controller we are
        // testing with alternates malfunctions and successes

        // Try to open, malfunction
        gate.open();
        LogMessage message = log.getNextMessage();
        assertEquals("open: malfunction", message.getMessage());

        // Try to open, successful
        message = log.getNextMessage();
        assertEquals("open: successful", message.getMessage());
        assertEquals(gate.getNumberOfOpens(), 1);
        assertEquals(gate.getNumberOfCloses(), 0);

        // Try to close, malfunction
        gate.close();
        message = log.getNextMessage();
        assertEquals("close: malfunction", message.getMessage());

        // Try to close, successful
        message = log.getNextMessage();
        assertEquals("close: successful", message.getMessage());
        assertEquals(gate.getNumberOfOpens(), 1);
        assertEquals(gate.getNumberOfCloses(), 1);
    }

    /**
     * Creates a new TollGate with a perfect controller. The gate is closed
     * again with no malfunction. The log and the number of closes are checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestCloseAlreadyClosed() throws TollboothException {
        final GateController controller = new PerfectGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Close again and check statistics for no change, and no other messages
        gate.close();
        LogMessage message = log.getNextMessage();
        assertTrue(message == null);
        assertEquals(gate.getNumberOfCloses(), 0);
    }

    /**
     * Creates a new TollGate with an always malfunctioning controller. The gate
     * is opened, which completely malfunctions to the unrecoverable state. The
     * toll gate goes into "will not respond" mode. The gate is closed again,
     * which also fails. Logs and number of opens are checked.
     */
    @Test
    public void tollGateTestCloseAlreadyNotResponding() {
        final GateController controller = new MalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log, 2);

        // Get the gate to not respond
        try {
            gate.close();
        } catch (TollboothException e) {
            e.toString();
            // Gate is now not responding
        }

        LogMessage message;

        int i = 0;
        while ((message = log.getNextMessage()) != null) {
            if (i < 3) {
                assertTrue(message.getMessage().equals("close: malfunction"));
            } else {
                assertTrue(message.getMessage().equals("close: unrecoverable malfunction"));
            }
            i++;
        }

        assertEquals(gate.getNumberOfCloses(), 0);

        // Close again after not responding
        try {
            gate.close();
        } catch (TollboothException e) {
            e.toString();
            // Gate is now not responding
        }

        message = log.getNextMessage();
        assertEquals(message.getMessage(), "close: will not respond");
        assertEquals(gate.getNumberOfCloses(), 0);
    }

    /**
     * Creates a new TollGate with a controller and checks if the gate is
     * closed.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestCloseWithoutStatisticsAndLog() throws TollboothException {
        final GateController controller = new TestGateController();
        final TollGate gate = new TollGate(controller, null);

        // Gate is closed
        assertFalse(gate.isOpen());
    }

    /**
     * Creates a new TollGate with an always malfunctioning controller. The gate
     * is closed, which completely malfunctions to the unrecoverable state. The
     * toll gate goes into "will not respond" mode. The gate is called to return
     * the isOpen state, which also fails. Logs and number of closes are
     * checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestIsOpenAlreadyNotResponding() throws TollboothException {
        final GateController controller = new AllExceptIsOpenMalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log, 2);

        assertTrue(!gate.isOpen());

        // Get the gate to not respond
        try {
            gate.close();
        } catch (TollboothException e) {
            e.toString();
            // Gate is now not responding
        }

        LogMessage message;

        int i = 0;
        while ((message = log.getNextMessage()) != null) {
            if (i < 3) {
                assertTrue(message.getMessage().equals("close: malfunction"));
            } else {
                assertTrue(message.getMessage().equals("close: unrecoverable malfunction"));
            }
            i++;
        }

        assertEquals(gate.getNumberOfCloses(), 0);

        try {
            gate.isOpen();
        } catch (TollboothException e) {
            assertEquals(e.getMessage(), "isOpen: will not respond");
        }
    }

    /**
     * Creates a new TollGate with a controller that malfunctions on the
     * isOpen() command. The gate is opened with no malfunction. The gate is
     * closed with no malfunction. The gate is opened again with no malfunction.
     * The gate is reset with no malfunction. After each call, the state of
     * isOpen is checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestIsOpenWithMalfunctionController() throws TollboothException {
        final GateController controller = new IsOpenMalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Open, but should fail
        try {
            gate.open();
        } catch (TollboothException e) {
            assertEquals(e.getMessage(), IsOpenMalfunctionGateController.class.getName());
        }

        try {
            gate.isOpen();
        } catch (TollboothException e) {
            assertEquals(e.getMessage(),
                    "tollbooth.gatecontroller.IsOpenMalfunctionGateController");
        }

        // Close, but should fail
        try {
            gate.close();
        } catch (TollboothException e) {
            assertEquals(e.getMessage(), "close: will not respond");
        }

        // Reset
        try {
            gate.reset();
        } catch (TollboothException e) {
            e.toString();
            // This should not run
        }
    }

    /**
     * Creates a new TollGate with a malfunction controller. The gate is given a
     * null logger, and the behavior is checked.
     */
    @Test
    public void tollGateTestNullLogger() {
        final GateController controller = new AlternateMalfunctionGateController();
        final TollGate gate = new TollGate(controller, null);

        try {
            gate.open();
        } catch (TollboothException e) {
            assertEquals(e.getMessage(), "Null logger found.");
        }
    }

    /**
     * Creates a new TollGate with a alternating malfunction controller. The
     * gate malfunctions once before opening correctly. The logs and statistics
     * are checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestOpenAfterOneMalfunction() throws TollboothException {
        final GateController controller = new AlternateMalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Try to open, malfunction
        gate.open();
        LogMessage message = log.getNextMessage();
        assertEquals("open: malfunction", message.getMessage());
        assertEquals(AlternateMalfunctionGateController.class.getName(),
                message.getCause().getMessage());

        // Retry happens and gate opens successfully
        message = log.getNextMessage();
        assertEquals("open: successful", message.getMessage());
        assertEquals(gate.getNumberOfOpens(), 1);
    }

    /**
     * Creates a new TollGate with an always malfunctioning controller. The gate
     * is opened, which completely malfunctions to the unrecoverable state. The
     * toll gate goes into "will not respond" mode. The gate is opened again,
     * which also fails. Logs and number of opens are checked.
     */
    @Test
    public void tollGateTestOpenAlreadyNotResponding() {
        final GateController controller = new MalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log, 2);

        // Get the gate to not respond
        try {
            gate.open();
        } catch (TollboothException e) {
            e.toString();
            // Gate is now not responding
        }

        LogMessage message;

        int i = 0;
        while ((message = log.getNextMessage()) != null) {
            if (i < 3) {
                assertTrue(message.getMessage().equals("open: malfunction"));
                assertEquals(message.getCause().getMessage(),
                        MalfunctionGateController.class.getName());
            } else {
                assertTrue(message.getMessage().equals("open: unrecoverable malfunction"));
                assertEquals(message.getCause().getMessage(),
                        MalfunctionGateController.class.getName());
            }
            i++;
        }

        assertEquals(gate.getNumberOfOpens(), 0);

        // Open again after not responding
        try {
            gate.open();
        } catch (TollboothException e) {
            e.toString();
            // Gate is now not responding
        }

        message = log.getNextMessage();
        assertEquals(message.getMessage(), "open: will not respond");
        assertEquals(gate.getNumberOfOpens(), 0);
    }

    /**
     * Creates a new TollGate with a perfect controller and opens the gate. The
     * gate is opened again with no malfunction. The log and the number of opens
     * are checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestOpenAlreadyOpened() throws TollboothException {
        final GateController controller = new PerfectGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Open gate before starting
        gate.open();
        LogMessage message = log.getNextMessage();
        assertEquals(null, message);
        assertEquals(gate.getNumberOfOpens(), 1);

        // Open again and check statistics for no change, and no other messages
        gate.open();
        message = log.getNextMessage();
        assertTrue(message == null);
        assertEquals(gate.getNumberOfOpens(), 1);
    }

    /**
     * Creates a new TollGate with a perfect controller. Opens and closes the
     * gate. Checks logs and statistics.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestOpenAndClose() throws TollboothException {
        final GateController controller = new PerfectGateController();
        final TollboothLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Open the gate
        gate.open();
        assertTrue(gate.isOpen());

        // Close the gate
        gate.close();
        assertTrue(!gate.isOpen());

        LogMessage message = log.getNextMessage();
        assertEquals(null, message);
        assertEquals(gate.getNumberOfOpens(), 1);

        message = log.getNextMessage();
        assertEquals(null, message);
        assertEquals(gate.getNumberOfCloses(), 1);
    }

    /**
     * Creates a new TollGate with a perfect controller. Opens and resets the
     * gate. Checks logs and statistics.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestOpenAndReset() throws TollboothException {
        final GateController controller = new PerfectGateController();
        final TollboothLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Open the gate
        gate.open();
        assertTrue(gate.isOpen());

        // Close the gate
        gate.reset();
        assertTrue(!gate.isOpen());

        LogMessage message = log.getNextMessage();
        assertEquals(message.getMessage(), "reset: successful");
        assertEquals(gate.getNumberOfOpens(), 1);

        message = log.getNextMessage();
        assertEquals(null, message);
        assertEquals(gate.getNumberOfCloses(), 0);
    }

    /**
     * Creates a new TollGate with a controller and checks if the gate is open,
     * which the default state is closed.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestOpenWithoutStatisticsAndLog() throws TollboothException {
        final GateController controller = new TestGateController();
        final TollGate gate = new TollGate(controller, null);

        // Open the gate, no logger created
        gate.open();
        assertTrue(gate.isOpen());
    }

    /**
     * Creates a new TollGate with a perfect controller and the gate is opened
     * with no malfunction. The log and the number of opens are checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestOpenWithStatisticsAndLog() throws TollboothException {
        final GateController controller = new PerfectGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Should not have any open counts
        assertEquals(gate.getNumberOfOpens(), 0);

        // Open gate first time
        gate.open();
        LogMessage message = log.getNextMessage();
        assertEquals(null, message);
        assertEquals(gate.getNumberOfOpens(), 1);
    }

    /**
     * Creates a new TollGate with a alternating malfunction controller. The
     * gate malfunctions once before closing correctly. The logs and statistics
     * are checked.
     *
     * @throws TollboothException
     */
    @Test
    public void tollGateTestResetAfterOneMalfunction() throws TollboothException {
        final GateController controller = new AlternateMalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // We have to do the open and let it retry since the controller we are
        // testing with alternates malfunctions and successes

        // Try to open, malfunction
        gate.open();
        LogMessage message = log.getNextMessage();
        assertEquals("open: malfunction", message.getMessage());

        // Try to open, successful
        message = log.getNextMessage();
        assertEquals("open: successful", message.getMessage());
        assertEquals(gate.getNumberOfOpens(), 1);
        assertEquals(gate.getNumberOfCloses(), 0);

        // Try to reset, malfunction
        gate.reset();
        message = log.getNextMessage();
        assertEquals("reset: malfunction", message.getMessage());

        // Try to reset, successful
        message = log.getNextMessage();
        assertEquals("reset: successful", message.getMessage());
        assertEquals(gate.getNumberOfOpens(), 1);
        assertEquals(gate.getNumberOfCloses(), 0);
    }

    /**
     * Creates a new TollGate with a perfect controller. The gate is reset with
     * no malfunction. The log and the number of closes are checked.
     */
    @Test
    public void tollGateTestResetAlreadyClosed() {
        final GateController controller = new PerfectGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Reset and check statistics for no change, and no other messages
        try {
            gate.reset();
        } catch (TollboothException e) {
            e.toString();
            // This should not run
        }

        LogMessage message = log.getNextMessage();
        assertEquals(message.getMessage(), "reset: successful");
        assertEquals(gate.getNumberOfCloses(), 0);
    }

    /**
     * Creates a new TolLGate with a controller that malfunctions on the close()
     * command. The gate is opened with no malfunction. Then it is reset, which
     * will fail, become unrecoverable, and throw an exception which we check
     * for the message and cause.
     */
    @Test
    public void tollGateTestResetWithMalfunctionController() {
        final GateController controller = new ResetMalfunctionGateController();
        final SimpleLogger log = new TollboothLogger();
        final TollGate gate = new TollGate(controller, log);

        // Get the gate to open
        try {
            gate.open();
        } catch (TollboothException e) {
            e.toString();
            // This should not run
        }

        LogMessage message;
        assertEquals(gate.getNumberOfOpens(), 1);

        // Try to reset, but will throw exception
        try {
            gate.reset();
        } catch (TollboothException e) {
            // Reset fails and we check logs and exception
            int i = 0;
            while ((message = log.getNextMessage()) != null) {
                // First three are malfunctions
                if (i < 3) {
                    assertEquals(message.getMessage(), "reset: malfunction");
                    assertEquals(message.getCause().getMessage(),
                            "tollbooth.gatecontroller.ResetMalfunctionGateController");
                } else {
                    assertEquals(message.getMessage(), "reset: unrecoverable malfunction");
                    assertEquals(message.getCause().getMessage(),
                            "tollbooth.gatecontroller.ResetMalfunctionGateController");
                }
                i++;
            }

            // Check exception thrown inside of TollGate
            assertEquals(e.getMessage(), ResetMalfunctionGateController.class.getName());
            assertEquals(e.getCause().getMessage(),
                    "tollbooth.gatecontroller.ResetMalfunctionGateController");
        }
    }
}
