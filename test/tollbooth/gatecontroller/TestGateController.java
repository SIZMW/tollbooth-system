/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The
 * course was taken at Worcester Polytechnic Institute. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Copyright
 * ©2016 Gary F. Pollice
 *******************************************************************************/

package tollbooth.gatecontroller;

/**
 * Description
 *
 * @version Feb 15, 2016
 */
public class TestGateController implements GateController {
    boolean isOpen;

    /**
     * Constructor for the test gate controller.
     */
    public TestGateController() {
        isOpen = false;
    }

    /*
     * @see tollbooth.gatecontroller.GateController#close()
     */
    @Override
    public void close() {
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
    }
}
