/**
 * This file was created for the Tollbooth project implementation for CS4233.
 *
 * @author Aditya Nivarthi
 */
package tollbooth.gatecontroller;

/**
 * The PerfectGateController represents the controller that always functions
 * correctly.
 *
 * @version March 19 2016
 */
public class PerfectGateController implements GateController {
    private boolean isOpen;

    /**
     * Creates a PerfectGateController instance;
     */
    public PerfectGateController() {
        isOpen = false;
    }

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
    public void reset() {
        isOpen = false;
    }
}
