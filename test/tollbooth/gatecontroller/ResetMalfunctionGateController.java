/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth.gatecontroller;

import tollbooth.TollboothException;

/**
 * The ResetMalfunctionGateController is a gate controller that reports a
 * malfunction all the time only on the reset() command. All other commands work
 * properly all of the time.
 *
 * @version March 21 2016
 */
public class ResetMalfunctionGateController implements GateController {
    private boolean isOpen = false;

    /*
     * @see tollbooth.gatecontroller.GateController#close()
     */
    @Override
    public void close() {
        isOpen = false;
    }

    /*
     * @see tollbooth.gatecontroller.GateController#isOpen()
     */
    @Override
    public boolean isOpen() {
        return isOpen;
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
    public void reset() throws TollboothException {
        throw new TollboothException(ResetMalfunctionGateController.class.getName());
    }
}
