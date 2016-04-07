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
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import tollbooth.gatecontroller.GateController;

/**
 * Test cases for the Tollbooth, TollGate class.
 *
 * @version Feb 3, 2016
 */
public class TollboothMasterTest {
    class TestGateController implements GateController {
        private boolean isOpen;
        private Queue<Boolean> openActions;
        private Queue<Boolean> closeActions;
        private Queue<Boolean> resetActions;

        /**
         * Constructor for the test gate controller.
         */
        public TestGateController() {
            isOpen = false;
            openActions = new LinkedList<Boolean>();
            closeActions = new LinkedList<Boolean>();
            resetActions = new LinkedList<Boolean>();
        }

        /**
         * @param action
         *            the action to add
         */
        public void addCloseAction(boolean action) {
            closeActions.add(action);
        }

        /**
         * @param action
         *            the action to add
         */
        public void addOpenAction(boolean action) {
            openActions.add(action);
        }

        /**
         * @param action
         *            the action to add
         */
        public void addResetAction(boolean action) {
            resetActions.add(action);
        }

        /*
         * @see tollbooth.gatecontroller.GateController#close()
         */
        @Override
        public void close() throws TollboothException {
            if (closeActions.peek() != null && !closeActions.remove()) {
                throw new TollboothException("close: malfunction");
            }
            isOpen = false;
        }

        /*
         * @see tollbooth.gatecontroller.GateController#isOpen()
         */
        @Override
        public boolean isOpen() throws TollboothException {
            return isOpen;
        }

        /*
         * @see tollbooth.gatecontroller.GateController#open()
         */
        @Override
        public void open() throws TollboothException {
            if (openActions.peek() != null && !openActions.remove()) {
                throw new TollboothException("open: malfunction");
            }
            isOpen = true;
        }

        /*
         * @see tollbooth.gatecontroller.GateController#reset()
         */
        @Override
        public void reset() throws TollboothException {
            if (resetActions.peek() != null && !resetActions.remove()) {
                throw new TollboothException("reset: malfunction");
            }
            isOpen = false;
        }
    }

    class TestLogger implements SimpleLogger {
        private Queue<LogMessage> queue = new LinkedList<LogMessage>();

        /*
         * @see tollbooth.SimpleLogger#accept(tollbooth.LogMessage)
         */
        @Override
        public void accept(LogMessage message) {
            queue.add(message);
        }

        /*
         * @see tollbooth.SimpleLogger#getNextMessage()
         */
        @Override
        public LogMessage getNextMessage() {
            return queue.peek() == null ? null : queue.remove();
        }

    }

    private GateController controller;
    private TestGateController testController;
    private TollGate gate;
    private SimpleLogger logger;

    @Test
    public void closeAClosedGateDoesNotChangeNumberOfCloses() throws TollboothException {
        gate.open();
        gate.close();
        final int before = gate.getNumberOfCloses();
        gate.close();
        assertEquals(before, gate.getNumberOfCloses());
    }

    @Test
    public void closeAClosedGateLeavesGateClosed() throws TollboothException {
        gate.open();
        gate.close();
        gate.close();
        assertFalse(gate.isOpen());
    }

    @Test
    public void closeGateThreeMalfunctionsFourLogMessages() throws TollboothException {
        testController.addCloseAction(false); // malfunction
        testController.addCloseAction(false);
        testController.addCloseAction(false);
        gate.open();
        try {
            gate.close();
            System.out.println(
                    "GRADER: closeGateThreeMalfunctionsFourLogMessages forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior
        }
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^close: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^close: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^close: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^close: +unrecoverable malfunction$"));
    }

    @Test
    public void closeGateThreeMalfunctionsGateWillNotRespond() throws TollboothException {
        testController.addCloseAction(false); // malfunction
        testController.addCloseAction(false);
        testController.addCloseAction(false);
        gate.open();
        try {
            gate.close();
            System.out.println(
                    "GRADER: closeGateThreeMalfunctionsGateWillNotRespond forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior
        }
        logger.getNextMessage();
        logger.getNextMessage();
        logger.getNextMessage();
        logger.getNextMessage();
        gate.close();
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^close: will not respond$"));
    }

    @Test
    public void closeGateThreeMalfunctionsNoChangeInNumberOfCloses() throws TollboothException {
        testController.addCloseAction(false); // malfunction
        testController.addCloseAction(false);
        testController.addCloseAction(false);
        gate.open();
        final int before = gate.getNumberOfCloses();
        try {
            gate.close();
            System.out.println(
                    "GRADER: closeGateThreeMalfunctionsNoChangeInNumberOfCloses forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior
        }
        assertEquals(before, gate.getNumberOfCloses());
    }

    @Test
    public void closeGateTwoMalfunctionNumberOfClosesIncrements() throws TollboothException {
        testController.addCloseAction(false);
        testController.addCloseAction(false);
        gate.open();
        final int before = gate.getNumberOfCloses();
        gate.close();
        assertEquals(before + 1, gate.getNumberOfCloses());
    }

    @Test
    public void closeGateTwoMalfunctionsGateIsClosed() throws TollboothException {
        testController.addCloseAction(false);
        testController.addCloseAction(false);
        gate.open();
        gate.close();
        assertFalse(gate.isOpen());
    }

    @Test
    public void closeGateTwoMalfunctionsThreeLogMessages() throws TollboothException {
        testController.addCloseAction(false); // malfunction
        testController.addCloseAction(false);
        gate.open();
        gate.close();
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^close: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^close: +malfunction$"));
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^close: +successful$"));
    }

    @Test
    public void closeOpenGateIncrementsNumberOfCloses() throws TollboothException {
        gate.open();
        final int before = gate.getNumberOfCloses();
        gate.close();
        assertEquals(before + 1, gate.getNumberOfCloses());
    }

    @Test
    public void closeOpenGateMakesGateClosed() throws TollboothException {
        gate.open();
        gate.close();
        assertFalse(gate.isOpen());
    }

    @Test
    public void gateControllerIsOpenAfterOpenMessage() throws TollboothException {
        gate.open();
        assertTrue(gate.isOpen());
    }

    @Test
    public void newGateControllerIsClosed() throws TollboothException {
        assertFalse(gate.isOpen());
    }

    @Test
    public void openAnOpenGateDoesNotChangeNumberOfOpens() throws TollboothException {
        gate.open();
        final int before = gate.getNumberOfOpens();
        gate.open();
        assertEquals(before, gate.getNumberOfOpens());
    }

    @Test
    public void openAnOpenGateLeavesGateOpen() throws TollboothException {
        gate.open();
        gate.open();
        assertTrue(gate.isOpen());
    }

    @Test
    public void openGateOneMalfunctionGateIsOpen() throws TollboothException {
        testController.addOpenAction(false); // malfunction
        gate.open();
        assertTrue(gate.isOpen());
    }

    @Test
    public void openGateOneMalfunctionNumberOfOpensIncrements() throws TollboothException {
        final int before = gate.getNumberOfOpens();
        testController.addOpenAction(false); // malfunction
        testController.addOpenAction(true); // good open
        gate.open();
        assertEquals(before + 1, gate.getNumberOfOpens());
    }

    @Test
    public void openGateOneMalfunctionTwoLogMessages() throws TollboothException {
        testController.addOpenAction(false); // malfunction
        testController.addOpenAction(true); // good open
        gate.open();
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^open: +malfunction$"));
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^open: +successful$"));
    }

    @Test
    public void openGateThreeMalfunctionsFourLogMessages() throws TollboothException {
        testController.addOpenAction(false); // malfunction
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        try {
            gate.open();
            System.out.println(
                    "GRADER: openGateThreeMalfunctionsFourLogMessages forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior
        }
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^open: +malfunction$"));
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^open: +malfunction$"));
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^open: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^open: +unrecoverable malfunction$"));
    }

    @Test
    public void openGateThreeMalfunctionsGateWillNotRespond() throws TollboothException {
        testController.addOpenAction(false); // malfunction
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        try {
            gate.open();
            System.out.println(
                    "GRADER: openGateThreeMalfunctionsGateWillNotRespond forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior
        }
        logger.getNextMessage();
        logger.getNextMessage();
        logger.getNextMessage();
        logger.getNextMessage();
        gate.open();
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^open: will not respond$"));
    }

    @Test
    public void openGateThreeMalfunctionsNoChangeInNumberOfOpens() throws TollboothException {
        final int before = gate.getNumberOfOpens();
        testController.addOpenAction(false); // malfunction
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        try {
            gate.open();
            System.out.println(
                    "GRADER: openGateThreeMalfunctionsNoChangeInNumberOfOpens forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior
        }
        assertEquals(before, gate.getNumberOfOpens());
    }

    @Test
    public void openInWillNotRepondStateNoChangeInNumberOfOpens() throws TollboothException {
        testController.addOpenAction(false); // malfunction
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        try {
            gate.open();
            System.out.println(
                    "GRADER: openInWillNotRepondStateNoChangeInNumberOfOpens forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior
        }
        final int before = gate.getNumberOfOpens();
        gate.open();
        assertEquals(before, gate.getNumberOfOpens());
    }

    @Test
    public void resetClosedGateOneLogMessage() throws TollboothException {
        gate.reset();
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^reset: +successful$"));
    }

    @Test
    public void resetClosedTollgateStateIsClosed() throws TollboothException {
        gate.reset();
        assertFalse(gate.isOpen());
    }

    @Test
    public void resetClosedTollgateStatisticsDoNotChange() throws TollboothException {
        gate.open();
        gate.close();
        final int beforeOpens = gate.getNumberOfOpens();
        final int beforeCloses = gate.getNumberOfCloses();
        gate.reset();
        assertEquals(beforeOpens, gate.getNumberOfOpens());
        assertEquals(beforeCloses, gate.getNumberOfCloses());
    }

    @Test
    public void resetCloseTollgateStatisticsDoNotChange() throws TollboothException {
        gate.open();
        gate.close();
        gate.open();
        final int beforeOpens = gate.getNumberOfOpens();
        final int beforeCloses = gate.getNumberOfCloses();
        gate.reset();
        assertEquals(beforeOpens, gate.getNumberOfOpens());
        assertEquals(beforeCloses, gate.getNumberOfCloses());
    }

    @Test
    public void resetFails() throws TollboothException {
        testController.addResetAction(false);
        testController.addResetAction(false);
        testController.addResetAction(false);
        try {
            gate.reset();
            System.out.println("GRADER: resetFails forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior.
        }

        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^reset: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^reset: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^reset: +malfunction$"));
        assertTrue(logger.getNextMessage().getMessage().toLowerCase()
                .matches("^reset: +unrecoverable malfunction$"));
    }

    @Test
    public void resetOpenGateOneLogMessage() throws TollboothException {
        gate.open();
        gate.reset();
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^reset: +successful$"));
    }

    @Test
    public void resetOpenTollgateStateIsClosed() throws TollboothException {
        gate.open();
        gate.reset();
        assertFalse(gate.isOpen());
    }

    @Test
    public void resetWillNotRespondTollgateOneLogMessage() throws TollboothException {
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        try {
            gate.open();
            System.out.println(
                    "GRADER: resetWillNotRespondTollgateOneLogMessage forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior.
        }
        logger.getNextMessage();
        logger.getNextMessage();
        logger.getNextMessage();
        logger.getNextMessage();
        gate.reset();
        assertTrue(
                logger.getNextMessage().getMessage().toLowerCase().matches("^reset: +successful$"));
    }

    @Test
    public void resetWillNotRespondTollgateStateIsClosed() throws TollboothException {
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        try {
            gate.open();
            System.out.println(
                    "GRADER: resetWillNotRespondTollgateStateIsClosed forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior.
        }
        gate.reset();
        assertFalse(gate.isOpen()); // could be in will not respond!!
    }

    @Test
    public void resetWillNotRespondTollgateStatisticsDoNotChange() throws TollboothException {
        testController.addOpenAction(true); // first open() succeeds
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        testController.addOpenAction(false);
        gate.open(); // succeeds
        gate.close();
        final int beforeOpens = gate.getNumberOfOpens();
        final int beforeCloses = gate.getNumberOfCloses();
        try {
            gate.open(); // fails
            System.out.println(
                    "GRADER: resetWillNotRespondTollgateStatisticsDoNotChange forgot to throw exception");
        } catch (Exception e) {
            // Do nothing. Correct behavior.
        }
        gate.reset();
        assertFalse(gate.isOpen());
        assertEquals(beforeOpens, gate.getNumberOfOpens());
        assertEquals(beforeCloses, gate.getNumberOfCloses());
    }

    @Before
    public void setup() {
        controller = testController = new TestGateController();
        logger = new TestLogger();
        gate = new TollGate(controller, logger);
    }

    @Test
    public void successfulOpenIncreasesOpenCount() throws TollboothException {
        final int before = gate.getNumberOfOpens();
        gate.open();
        assertEquals(before + 1, gate.getNumberOfOpens());
    }
}
