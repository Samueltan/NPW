/*
 * SunSpotApplication.java
 *
 * Created on 1 Oct, 2013 9:32:01 PM;
 */

package org.sunspotworld;


import com.sun.spot.io.j2me.radiogram.*;

import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ISwitch;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.LEDColor;
import com.sun.spot.service.BootloaderListenerService;
import com.sun.spot.util.IEEEAddress;
import com.sun.spot.util.Utils;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * The startApp method of this class is called by the VM to start the
 * application.
 * 
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 */
public class SunSpotApplication extends MIDlet {
    
    private static final int HOST_PORT = 68;

    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);

    protected void startApp() throws MIDletStateChangeException {
        System.out.println("Begin");
        BootloaderListenerService.getInstance().start();   // monitor the USB (if connected) and recognize commands from host

        long ourAddr = RadioFactory.getRadioPolicyManager().getIEEEAddress();
        System.out.println("Our radio address = " + IEEEAddress.toDottedHex(ourAddr));
        String ourAddress = System.getProperty("IEEE_ADDRESS");
        
        RadiogramConnection rCon = null;
        Datagram dg = null;        
        
        int status = 0;
        
        try 
        {
            rCon = (RadiogramConnection) Connector.open("radiogram://:" + 50);  					// Open up a server-side broadcast radiogram connection
            dg = rCon.newDatagram(rCon.getMaximumLength());
        } 
        catch (Exception e) 
        {
             System.err.println("setUp caught " + e.getMessage());
             System.exit(-1);
        }
        leds.setOff();
        while (true) 
        {
            System.out.println("Entered while\n");
            try 
            {
                System.out.println("Entered try\n");
            
                rCon.receive(dg);
                
                //led_no = dg.readDouble();
                status = dg.readInt();
                //color = dg.readUTF();
                
                System.out.println("status:  " + status);
            
             //   led.setRGB(255, 255, 255);
             //   led.setOn();
                
                
            } 
            catch (Exception e) 
            {
                
                System.err.println("Caught " + e +  " while reading sensor samples.");
                System.exit(-1);
            }

            if(status == 1)
            {
                for (int i = 0; i < leds.size(); i++) {
                    leds.getLED(i).setColor(LEDColor.BLUE);
                    leds.getLED(i).setOn();
                    Utils.sleep(50);                
                }
            }else{
                for (int i = 0; i < leds.size(); i++) {
                    leds.getLED(i).setOff();
                    Utils.sleep(50);                
                }
            }            
        }
        
    }

    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system.
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true the MIDlet must cleanup and release all resources.
     * @throws javax.microedition.midlet.MIDletStateChangeException
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }
}
