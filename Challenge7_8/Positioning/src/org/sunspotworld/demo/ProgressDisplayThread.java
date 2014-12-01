/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld.demo;

import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.util.Utils;

/**
 *
 * @author Samuel
 */
class ProgressDisplayThread extends Thread {
    private boolean done = false;

    private ITriColorLEDArray myLEDs =
            (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);

    public void run() {
        while (!done) {
            for (int i = 0; !done && i < myLEDs.size(); i++) {
                myLEDs.getLED(i).setRGB(0, 0, 0);
                myLEDs.getLED(i).setOff();
            }
            for (int i = 0; !done && i < myLEDs.size(); i++) {
                myLEDs.getLED(i).setRGB(50, 50, 50);
                myLEDs.getLED(i).setOn();
                Utils.sleep(500);
            }
        }
    }

    public void markDone(boolean status) {
        done = true;
        for (int i = 0; i < myLEDs.size(); i++) {
            if (status) { // post was successful
                myLEDs.getLED(i).setRGB(0, 50, 0);
            } else { // post failed
                myLEDs.getLED(i).setRGB(50, 0, 0);
            }
            myLEDs.getLED(i).setOn();
        }
        Utils.sleep(1000);
    }
}