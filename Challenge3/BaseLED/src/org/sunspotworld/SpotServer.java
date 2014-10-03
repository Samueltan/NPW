/*
 * SpotServer.java
 *
 * Created on 29 Sep, 2013 7:06:22 PM;
 */

package org.sunspotworld;

import com.sun.spot.resources.Resources;

import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.peripheral.radio.IRadioPolicyManager;
import com.sun.spot.io.j2me.radiostream.*;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.util.IEEEAddress;
import com.sun.spot.util.Utils;

import java.io.*;

import javax.microedition.io.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Rui Li
 */
public class SpotServer {

    private final int HOST_PORT = 68;
    private int status = 0;
    
    public void run() throws Exception {
        RadiogramConnection rCon = null;
        Datagram dg = null;       
        long ourAddr = RadioFactory.getRadioPolicyManager().getIEEEAddress();
        System.out.println("Our radio address = " + IEEEAddress.toDottedHex(ourAddr));
        
        String ourAddress = System.getProperty("IEEE_ADDRESS");
        
        try 
        {
            // Open up a broadcast connection to the host port
            // where the 'on Desktop' portion of this demo is listening
            rCon = (RadiogramConnection) Connector.open("radiogram://broadcast:" + HOST_PORT);
            dg = rCon.newDatagram(50);  // only sending 12 bytes of data
        }
        
        catch (Exception e) 
        {
            System.err.println("Caught " + e + " in connection initialization.");
            System.exit(-1);
        }
        
        Properties prop = new Properties();
	InputStream input = null;
        while(true)
        {
            //System.out.println("Try to open file");      
            //double led = 9999;
            //String color = "";            
            //dg.writeDouble(led);
            
            try {
                input = new FileInputStream("/Users/samueltango/Downloads/Dev/apache-tomcat-8.0.8/webapps/ec544/cmd.properties");
                // load the LED.properties file
                prop.load(input);
                // get the property value and print it out
                status = Integer.parseInt(prop.getProperty("cmd"));                       
            } catch (IOException ex) {
            } finally {
		if (input != null) {
                    try {
			input.close();
                    } catch (IOException e) {
                    }
		}
            }
            dg.reset();
            dg.writeInt(status);
            dg.writeLong(35790);//dg.writeUTF(color);
            
            
            rCon.send(dg);
            //System.out.println(dg.getAddress());
            System.out.println("status: " + dg.readInt());
            Utils.sleep(500); 
            dg.reset();           
        }
    }
    

    /**
     * Start up the host application.
     *
     * @param args any command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception{
        SpotServer app = new SpotServer();
        app.run();
    }
}
