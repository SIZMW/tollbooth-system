/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth.gatecontroller;

import tollbooth.TollboothException;

/**
 * The MalfunctionGateController class is a gate controller that always fails to
 * do whatever method is called. It will always return an exception.
 *
 * @version March 19 2016
 */
public class MalfunctionGateController implements GateController {

    /*
     * @see tollbooth.gatecontroller.GateController#close()
     */
    @Override
    public void close() throws TollboothException {
        throw new TollboothException(MalfunctionGateController.class.getName());
    }

    /*
     * @see tollbooth.gatecontroller.GateController#isOpen()
     */
    @Override
    public boolean isOpen() throws TollboothException {
        throw new TollboothException(MalfunctionGateController.class.getName());
    }

    /*
     * @see tollbooth.gatecontroller.GateController#open()
     */
    @Override
    public void open() throws TollboothException {
        throw new TollboothException(MalfunctionGateController.class.getName());
    }

    /*
     * @see tollbooth.gatecontroller.GateController#reset()
     */
    @Override
    public void reset() throws TollboothException {
        throw new TollboothException(MalfunctionGateController.class.getName());
    }
}
