/*
 * StartApplication.java
 *
 * Created on Oct 29, 2010 5:06:23 PM;
 */

package org.sunspotworld.demo;

import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ILightSensor;
import com.sun.spot.resources.transducers.ISwitch;
import com.sun.spot.resources.transducers.ITemperatureInput;
import com.sun.spot.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * The startApp method of this class is called by the VM to start the
 * application.
 *
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 */
public class HTTPDemo extends MIDlet {
    private static final int INACTIVE = 0;
    private static final int CONNECTING = 1;
    private static final int COMPLETED = 2;
    private static final int IOERROR = 3;
    private static final int PROTOCOLERROR = 4;

    private static int POSTstatus = INACTIVE;
    
//    private ISwitch sw = null;
    private String postURL = getAppProperty("POST-URL");
    int lightVal = 0;
    int tempVal = 0;
    private ILightSensor lightSensor = (ILightSensor) Resources.lookup(ILightSensor.class);
    private ITemperatureInput tempSensor = (ITemperatureInput) Resources.lookup(ITemperatureInput.class);
    private long ourAddr = RadioFactory.getRadioPolicyManager().getIEEEAddress();
        
    protected void startApp() throws MIDletStateChangeException {
//        // The resource lookup API supports tags ... this gets us Switch 2.
//        sw = (ISwitch) Resources.lookup(ISwitch.class, "SW2");
//        // Define & bind an anonymous switchlistener to change color on switch press
//        sw.addISwitchListener(new MySwitchListener(postURL));
    
        try {
            while(true){
                lightVal = lightSensor.getValue();
                tempVal = (int) (tempSensor.getCelsius() * 100);
                float temp = (float) (tempVal * 1.0 / 100);
                String msg = IEEEAddress.toDottedHex(ourAddr) +
                        ": Light reading is " + lightVal +
                        " and temperature is " + temp +
                        " C.";
                postMessage(postURL, msg, lightVal, temp);
                Thread.sleep(1000);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        while (true) {
            Utils.sleep(100);
        }

        //notifyDestroyed();                      // cause the MIDlet to exit
    }

    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system.
     * I.e. if startApp throws any exception other than MIDletStateChangeException,
     * if the isolate running the MIDlet is killed with Isolate.exit(), or
     * if VM.stopVM() is called.
     *
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     *    cleanup and release all resources. If false the MIDlet may throw
     *    MIDletStateChangeException  to indicate it does not want to be destroyed
     *    at this time.
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }
    
    public static void postMessage(String postURL, String msg, int light, float temperature) {
        HttpConnection conn = null;
        OutputStream out = null;
        InputStream in = null;
        long starttime = 0;
        String resp = null;
        ProgressDisplayThread displayProg = null;

        System.out.println("Posting: <" + msg + "> to " + postURL);

        try {
            POSTstatus = CONNECTING;
            displayProg = new ProgressDisplayThread();
            displayProg.start();
            starttime = System.currentTimeMillis();
            conn = (HttpConnection) Connector.open(postURL);
            conn.setRequestMethod(HttpConnection.POST);
            conn.setRequestProperty("light", String.valueOf(light));
            conn.setRequestProperty("temperature", String.valueOf(temperature));

            out = conn.openOutputStream();
            out.write((msg + "'\n").getBytes());
            out.flush();

            in = conn.openInputStream();
            resp = conn.getResponseMessage();
            if (resp.equalsIgnoreCase("OK") || resp.equalsIgnoreCase("CREATED")) {
                POSTstatus = COMPLETED;
            } else {
                POSTstatus = PROTOCOLERROR;
            }
        } catch (Exception ex) {
            POSTstatus = IOERROR;
            resp = ex.getMessage();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (IOException ex) {
                resp = ex.getMessage();
            }
        }

        displayProg.markDone(POSTstatus == COMPLETED);
        if (POSTstatus != COMPLETED) {
            System.out.println("Posting failed: " + resp);
        } else {
            System.out.println("Total time to post " +
                "(including connection set up): " +
                (System.currentTimeMillis() - starttime) + " ms");
        }
        System.out.flush();
    }
}

