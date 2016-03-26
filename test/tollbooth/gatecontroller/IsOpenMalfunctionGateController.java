/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth.gatecontroller;

import tollbooth.TollboothException;

/**
 * The ResetMalfunctionGateController is a gate controller that reports a
 * malfunction all the time only on the isOpen() command. All other commands
 * work properly all of the time.
 *
 * @version March 22 2016
 */
public class IsOpenMalfunctionGateController implements GateController {
    private boolean isOpen;

    /**
     * Creates a PerfectGateController instance;
     */
    public IsOpenMalfunctionGateController() {
        isOpen = false;
    }

    /*
     * @see tollbooth.gatecontroller.GateController#close()
     */
    @Override
    public void close() {
        isOpen = false;
    }

    /**
     * Returns true if the gate is open, false otherwise
     *
     * @return a boolean
     */
    public boolean isCurrentlyOpen() {
        return isOpen;
    }

    /*
     * @see tollbooth.gatecontroller.GateController#isOpen()
     */
    @Override
    public boolean isOpen() throws TollboothException {
        throw new TollboothException(IsOpenMalfunctionGateController.class.getName());
    }

    /*
     * @see tollbooth.gatecontroller.GateController#open()
     */
    @Override
    public void open() {
        isOpen = true;
    }

    /*
     * @see tollbooth.gatecontroller.GateController#reset()
     */
    @Override
    public void reset() {
        isOpen = false;
    }
}
