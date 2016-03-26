/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth.gatecontroller;

import tollbooth.TollboothException;

/**
 * The AlternateMalfunctionGateController is a gate controller that reports a
 * malfunction every other call to open(), close(), and reset(). These methods
 * malfunction alternatively and independently of each other. When it does not
 * malfunction, the controller takes the appropriate action for open(), close(),
 * and reset(). The exception here is the isOpen() function, which always works.
 *
 * @version March 19 2016
 */
public class AlternateMalfunctionGateController implements GateController {
    private boolean isOpen;
    private boolean openMF = true;
    private boolean closeMF = true;
    private boolean resetMF = true;

    /**
     * Creates a PerfectGateController instance;
     */
    public AlternateMalfunctionGateController() {
        isOpen = false;
    }

    /*
     * @see tollbooth.gatecontroller.GateController#close()
     */
    @Override
    public void close() throws TollboothException {
        if (closeMF) {
            closeMF = !closeMF;
            throw new TollboothException(AlternateMalfunctionGateController.class.getName());
        } else {
            isOpen = false;
            closeMF = !closeMF;
        }
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
    public void open() throws TollboothException {
        if (openMF) {
            openMF = !openMF;
            throw new TollboothException(AlternateMalfunctionGateController.class.getName());
        } else {
            isOpen = true;
            openMF = !openMF;
        }
    }

    /*
     * @see tollbooth.gatecontroller.GateController#reset()
     */
    @Override
    public void reset() throws TollboothException {
        if (resetMF) {
            resetMF = !resetMF;
            throw new TollboothException(AlternateMalfunctionGateController.class.getName());
        } else {
            isOpen = false;
            resetMF = !resetMF;
        }
    }
}
