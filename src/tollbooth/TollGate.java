/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The
 * course was taken at Worcester Polytechnic Institute. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Copyright
 * ©2016 Gary F. Pollice
 ******************************************************************************
 * @author Aditya Nivarthi
 */

package tollbooth;

import tollbooth.gatecontroller.GateController;

/**
 * The TollGate contains everything about a tollgate in a tollbooth. It handles
 * using the gate controller and maintaining statistics about the gate closing
 * and opening.
 *
 * @version March 23 2016
 */
public class TollGate {
    public static final int RETRY_COUNT = 2;
    private final GateController controller;
    private final SimpleLogger logger;
    private int numberOfOpens = 0;
    private int numberOfCloses = 0;
    private int retryCount;
    private boolean willNotRespond = false;

    /**
     * Creates a TollGate instance with the specified gate controller and
     * logger. Sets the retry count to 5 by default.
     *
     * @param controller
     *            The {@link GateController} object.
     * @param logger
     *            The {@link SimpleLogger} object.
     */
    public TollGate(GateController controller, SimpleLogger logger) {
        this.controller = controller;
        this.logger = logger;
        retryCount = RETRY_COUNT; // Default retry count
    }

    /**
     * Creates a TollGate instance with the specified gate controller, logger
     * and retry count.
     *
     * @param controller
     *            The {@link GateController} object.
     * @param logger
     *            The {@link SimpleLogger} object.
     * @param retryCount
     *            The number of times to retry after a malfunction.
     */
    public TollGate(GateController controller, SimpleLogger logger, int retryCount) {
        this.controller = controller;
        this.logger = logger;
        this.retryCount = retryCount;
    }

    /**
     * Closes the gate. If the gate fails to close, it will retry as many times
     * as specified when this object was built, or 2 as a default if no retry
     * count was given. If the gate is not responsive, the log will be updated
     * and nothing will occur. This function will recursively retry closing the
     * gate.
     *
     * @throws TollboothException
     *             If it is in the "will not respond" state and the close
     *             request is made, or it goes into the "will not respond"
     *             state.
     */
    public void close() throws TollboothException {
        close(retryCount + 1, "close", false);
    }

    /**
     * Closes the gate. See {@link TollGate#close()}
     *
     * @param remainingRetryCount
     *            The number of tries to retry the open action.
     * @param logName
     *            The name to output in all the log messages as the caller
     *            function.
     * @param isRetry
     *            The state of this function call for whether it is a retry call
     *            or not.
     * @throws TollboothException
     *             If it is in the "will not respond" state and the close
     *             request is made, or it goes into the "will not respond"
     *             state.
     */
    private void close(int remainingRetryCount, String logName, boolean isRetry)
            throws TollboothException {
        // Gate is not responding
        if (willNotRespond) {
            logMessage(logName + ": will not respond", null);
            throw new TollboothException(logName + ": will not respond", null);
        }

        // All other cases
        try {
            boolean wasOpen = isOpen();
            controller.close();

            // Increase the number of closes and log it
            if (wasOpen) {
                numberOfCloses++;

                // Log success only if it is retrying
                if (isRetry) {
                    logMessage(logName + ": successful", null);
                }
            }
        } catch (TollboothException e) {
            // Determine whether to retry or set not responsive state
            if (remainingRetryCount <= 0) {
                willNotRespond = true;
                logMessage(logName + ": unrecoverable malfunction", e);
                throw new TollboothException(e.getMessage(), e);
            } else {
                logMessage(logName + ": malfunction", e);
                close(remainingRetryCount - 1, logName, true);
            }
        }
    }

    /**
     * Returns the number of times the gate was closed.
     *
     * @return The number of times that the gate has been closed (that is, the
     *         close method has successfully been executed) since the object was
     *         created.
     */
    public int getNumberOfCloses() {
        return numberOfCloses;
    }

    /**
     * Returns the number of times the gate was opened.
     *
     * @return The number of times that the gate has been opened (that is, the
     *         open method has successfully been executed) since the object was
     *         created.
     */
    public int getNumberOfOpens() {
        return numberOfOpens;
    }

    /**
     * Returns if the gate is open. Tries to check if the controller gate is
     * open. If it gets a result, returns that result. If it fails to get a
     * state from the controller, it returns the last known state of the gate.
     *
     * @return true if the gate is open, false otherwise
     * @throws TollboothException
     *             If it is in the "will not respond" state and the isOpen
     *             request is made.
     */
    public boolean isOpen() throws TollboothException {
        if (willNotRespond) {
            throw new TollboothException("isOpen: will not respond");
        }

        boolean state = controller.isOpen();
        return state;
    }

    /**
     * Internal logging function for logging the specified message and
     * exception. If the logger is null, a system error is printed instead.
     *
     * @param message
     *            The message to log.
     * @param e
     *            The exception to log as the cause.
     * @throws TollboothException
     *             If the logger is null.
     */
    private void logMessage(String message, Exception ex) throws TollboothException {
        try {
            logger.accept(new LogMessage(message, ex));
        } catch (NullPointerException e) {
            throw new TollboothException("Null logger found.", e);
        }
    }

    /**
     * Opens the gate. If the gate fails to open, it will retry as many times as
     * specified when this object was built, or 5 as a default if no retry count
     * was given. If the gate is not responsive, the log will be updated and
     * nothing will occur. This function will recursively retry opening the
     * gate.
     *
     * @throws TollboothException
     *             If it is in the "will not respond" state and the open request
     *             is made, or it goes into the "will not respond" state.
     */
    public void open() throws TollboothException {
        open(retryCount + 1, "open", false);
    }

    /**
     * Opens the gate. See {@link TollGate#open()}
     *
     * @param remainingRetryCount
     *            The number of tries to retry the open action.
     * @param logName
     *            The name to output in all the log messages as the caller
     *            function.
     * @param isRetry
     *            The state of this function call for whether it is a retry call
     *            or not.
     * @throws TollboothException
     *             If it is in the "will not respond" state and the open request
     *             is made, or it goes into the "will not respond" state.
     */
    private void open(int remainingRetryCount, String logName, boolean isRetry)
            throws TollboothException {
        // Gate is not responding
        if (willNotRespond) {
            logMessage(logName + ": will not respond", null);
            throw new TollboothException(logName + ": will not respond", null);
        }

        // All other cases
        try {
            boolean wasOpen = isOpen();
            controller.open();

            // Increase number of opens and log it
            if (!wasOpen) {
                numberOfOpens++;
                if (isRetry) {
                    logMessage(logName + ": successful", null);
                }
            }
        } catch (TollboothException e) {
            // Determine whether to retry or set not responsive state
            if (remainingRetryCount <= 0) {
                willNotRespond = true;
                logMessage(logName + ": unrecoverable malfunction", e);
                throw new TollboothException(e.getMessage(), e);
            } else {
                logMessage(logName + ": malfunction", e);
                open(remainingRetryCount - 1, logName, true);
            }
        }
    }

    /**
     * Reset the gate to the state it was in when created with the exception of
     * the statistics.
     *
     * @throws TollboothException
     *             If it goes into the "will not respond" state.
     */
    public void reset() throws TollboothException {
        reset(retryCount + 1, "reset", false);
    }

    /**
     * Resets the gate. See {@link TollGate#reset()}
     *
     * @param remainingRetryCount
     *            The number of tries to retry the open action.
     * @param logName
     *            The name to output in all the log messages as the caller
     *            function.
     * @param isRetry
     *            The state of this function call for whether it is a retry call
     *            or not.
     * @throws TollboothException
     *             If it goes into the "will not respond" state.
     */
    private void reset(int remainingRetryCount, String logName, boolean isRetry)
            throws TollboothException {
        // If gate is closed, we are done
        try {
            if (!isOpen()) {
                logMessage(logName + ": successful", null);
                return;
            }
        } catch (TollboothException e) {
            e.toString();
            // Do nothing since we could not check the state, continue on
        }

        // All other cases
        try {
            // The understanding is that reset() will take care of setting the
            // gate to the original state, in other words, closing the gate, so
            // we just reset it
            controller.reset();
            logMessage(logName + ": successful", null);
        } catch (TollboothException e) {
            // Determine whether to retry or set not responsive state
            if (remainingRetryCount <= 0) {
                willNotRespond = true;
                logMessage(logName + ": unrecoverable malfunction", e);
                throw new TollboothException(e.getMessage(), e);
            } else {
                logMessage(logName + ": malfunction", e);
                reset(remainingRetryCount - 1, logName, true);
            }
        }
    }
}
