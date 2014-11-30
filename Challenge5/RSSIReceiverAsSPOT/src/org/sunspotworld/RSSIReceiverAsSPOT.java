/*
 * RSSIReceiverOnSPOT.java
 * 
 * Example RSSI Receiver code intended to be run on a free-range SPOT.
 * This code listens for beacon transmissions on the specified port, then forwards
 * the RSSI and Address information to a basestation (or other SPOTs) on a
 * different port.
 * 
 * @author: Aaron Heuckroth <a.heuckroth@gmail.com>
 * Created on Oct 28, 2014
 */
package org.sunspotworld;

import javax.microedition.io.Connector;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.peripheral.Spot;
import com.sun.spot.peripheral.radio.IRadioPolicyManager;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.LEDColor;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import java.io.IOException;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.service.BootloaderListenerService;
import com.sun.spot.util.IEEEAddress;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class RSSIReceiverAsSPOT extends MIDlet {    
    //LEDs for status indicator blinking
    private static final ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);

    private static final String RECEIVE_PORT = "59";

    //MUST be the same as the CONFIRM_BYTE from RSSIBeacon.java
    private static final byte RX_CONFIRM_BYTE = 43;
    private static final int BROADCAST_CHANNEL = 15;
    //static radio connection objects
    private static RadiogramConnection rxConnection = null;
    private static Radiogram rxg = null;

    private static final int RSSI_MIN = 0;
    private static final int RSSI_MAX = 40;
    private static final String SENSOR_1_ADDR = "79B0";
    private static final String SENSOR_2_ADDR = "78FB";
    private static final String SENSOR_3_ADDR = "80F5";
    private static final String SENSOR_4_ADDR = "45BB";
    private static final String SENSOR_5_ADDR = "7DF3";
    private static final String SENSOR_6_ADDR = "358F";
    private static final double K_1 = 33.58;
    private static final double B_1 = -289.66;
    private static final double K_2 = 55.14;
    private static final double B_2 = -924.26;
    private static final double K_3 = 22.69;
    private static final double B_3 = -235.32;
    private static final double K_4 = 30.6;
    private static final double B_4 = -708.58;
    private static final double K_5 = 34.35;
    private static final double B_5 = -251.66;
    private static final double K_6 = 25.67;
    private static final double B_6 = -579.14;
    SensorInfo D_first = new SensorInfo(null, 0);
    SensorInfo D_second = new SensorInfo(null, 0);
    SensorInfo D_third = new SensorInfo(null, 0);
    
    /* Helper method for blinking LEDs the specified color. */
    private static void blinkLEDs(LEDColor color) {
        leds.setColor(color);
        leds.setOn();
        leds.setOff();
    }

    /* Establish RadiogramConnections on the specified ports. */
    private static void setupConnection() {
        IRadioPolicyManager rpm = Spot.getInstance().getRadioPolicyManager();
        rpm.setOutputPower(16 - 32);
        rpm.setChannelNumber(BROADCAST_CHANNEL);
        try {
            long ourAddr = RadioFactory.getRadioPolicyManager().getIEEEAddress();
            System.out.println("Our radio address = " + IEEEAddress.toDottedHex(ourAddr));

            // Connection used for receiving beacon transmissions
            rxConnection = (RadiogramConnection) Connector.open("radiogram://:" + RECEIVE_PORT);
            rxg = (Radiogram) rxConnection.newDatagram(rxConnection.getMaximumLength());
        } catch (IOException ex) {
            //blink red upon failure. :(
            blinkLEDs(LEDColor.RED);
            System.err.println("Could not open radiogram broadcast connection!");
            System.err.println(ex);
        }
    }
    /* Wait for beacon transmissions, then forward them to the basestation. */

    private void broadcastLoop() {
        while (true) {
            try {
                //reset radiograms to clear transmission data
                rxg.reset();              

                //waits for a new transmission on RECEIVE_PORT
                rxConnection.receive(rxg);
                
                //read confirmation byte data from the radiogram
                byte checkByte = rxg.readByte();
                
                //check to see if radiogram is the right type
                if (checkByte == RX_CONFIRM_BYTE) {
                    int rssiVal = - rxg.readInt();
                    String spotAddress = rxg.readUTF().substring(15);
                    
                    if(1 == ifValid(rssiVal)){
                        D_first.set(D_second.getAddr(), D_second.getDistance());
                        D_second.set(D_third.getAddr(), D_third.getDistance());
                        D_third.set(spotAddress, distance(spotAddress, rssiVal));
                        System.out.println(spotAddress + " :  " + rssiVal);
                    }                                        
                } else {
                    //blink red upon failure. :(
                    blinkLEDs(LEDColor.RED);                    
                    System.out.println("Unrecognized radiogram type! Expected: " + RX_CONFIRM_BYTE + ", Saw: " + checkByte);
                }

            } catch (Exception e) {
                //blinkr ed upon failure. :(
                blinkLEDs(LEDColor.RED);
                System.err.println("No datagram received!");
            }
        }
    }

    protected void startApp() throws MIDletStateChangeException {
        // Listen for downloads/commands over USB connection
        BootloaderListenerService.getInstance().start();

        setupConnection();
        broadcastLoop();
    }

    protected void pauseApp() {

    }

    /**
     * Called if the MIDlet is terminated by the system. I.e. if startApp throws
     * any exception other than MIDletStateChangeException, if the isolate
     * running the MIDlet is killed with Isolate.exit(), or if VM.stopVM() is
     * called.
     *
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     * cleanup and release all resources. If false the MIDlet may throw
     * MIDletStateChangeException to indicate it does not want to be destroyed
     * at this time.
     * @throws javax.microedition.midlet.MIDletStateChangeException
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        leds.setOff();
    }

    private int ifValid(int rssi){
        if((rssi > RSSI_MIN) && (rssi < RSSI_MAX)){
            return 1;
        }else{
            return 0;
        }
    }
   
    private double distance(String addr, int rssi){
        if(addr.equals(SENSOR_1_ADDR)){
            return rssi * K_1 + B_1;
        }
        if(addr.equals(SENSOR_2_ADDR)){
            return rssi * K_2 + B_2;
        }
        if(addr.equals(SENSOR_3_ADDR)){
            return rssi * K_3 + B_3;
        }
        if(addr.equals(SENSOR_4_ADDR)){
            return rssi * K_4 + B_4;
        }
        if(addr.equals(SENSOR_5_ADDR)){
            return rssi * K_5 + B_5;
        }
        if(addr.equals(SENSOR_6_ADDR)){
            return rssi * K_6 + B_6;
        }
        return 100;
    }
           
}
