/*
 * DatabaseDemoHostApplication.java
 *
 * Copyright (c) 2008-2009 Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.sunspotworld.demo;

import com.sun.spot.io.j2me.radiogram.*;

import com.sun.spot.peripheral.ota.OTACommandServer;
import com.sun.spot.util.IEEEAddress;
import java.text.DateFormat;
import java.util.ArrayList;
import javax.microedition.io.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * This application is the 'on Desktop' portion of the SendDataDemo. 
 * This host application collects sensor samples sent by the 'on SPOT'
 * portion running on neighboring SPOTs and graphs them in a window. 
 *   
 * @author Vipul Gupta
 * modified Ron Goldman
 */
public class SendDataDemoGuiHostApplication {
    // Broadcast port on which we listen for sensor samples
    private static final int HOST_PORT = 68;
    final double REAL1 = 23.1;
    final double REAL2 = 2.9;
    final double TEST1_S1 = 16.4;
    final double TEST2_S1 = 4.3;
    final double TEST1_S2 = 19.4;
    final double TEST2_S2 = 8;
    final double TEST1_S3 = 17.9;
    final double TEST2_S3 = 7.8;
    int sensor1 = 0;
    int sensor2 = 0;
    int sensor3 = 0;
    
    private JTextArea status;
    private long[] addresses = new long[8];
    private DataWindow[] plots = new DataWindow[8];
    DateFormat fmt = DateFormat.getTimeInstance();
    double[] a0=new double[3];
    double[] a1=new double[3];
    double[] a2=new double[3]; 
    double[] Average=new double[4];
    int[] state=new int[3];
    Window w1=new Window();
    //Thread t=new Thread(w1);
    
    private void setup() {
        JFrame fr = new JFrame("Send Data Host App");
        status = new JTextArea();
        JScrollPane sp = new JScrollPane(status);
        fr.add(sp);
        fr.setSize(360, 200);
        fr.validate();
        fr.setVisible(true);
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = 0;
            plots[i] = null;
        }
    }
    
    private DataWindow findPlot(long addr,double val,long time) {
        String ieee;
        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i] == 0) {
                if(addr==100){
                ieee = "The All Sensors' Average Temputerature";  
                }
                else{
                ieee = IEEEAddress.toDottedHex(addr);
                }
                status.append("The sensor's address is " + addr + " and the tempture is" + val + "\n");
                addresses[i] = addr;
                plots[i] = new DataWindow(ieee);
                final int ii = i;
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        plots[ii].setVisible(true);
                    }
                });
                return plots[i];
            }
            if (addresses[i] == addr) {
                return plots[i];
            }
        }
        return plots[0];
    }
    
    private void run() throws Exception {
        RadiogramConnection rCon;
        Radiogram dg;
        a0[0]=200;
        a0[1]=200;
        a0[2]=200;
        a1[0]=200;
        a1[1]=200;
        a1[2]=200;
        a2[0]=200;
        a2[1]=200;
        a2[2]=200;
        state[0]=0;
        state[1]=0;
        state[2]=0;
        try {
            // Open up a server-side broadcast radiogram connection
            // to listen for sensor readings being sent by different SPOTs
            //t.start();
            rCon = (RadiogramConnection) Connector.open("radiogram://:" + HOST_PORT);
            dg = (Radiogram)rCon.newDatagram(rCon.getMaximumLength());
        } catch (Exception e) {
             System.err.println("setUp caught " + e.getMessage());
             throw e;
        }

        status.append("Listening...\n");        

        // Main data collection loop
        while (true) {
            try {
                // Read sensor sample received over the radio
                rCon.receive(dg); 
                String addr = dg.getAddress(); 
                long addr1 = 100;
                long time = dg.readLong();      // read time of the reading
                double val = dg.readDouble();         // read the sensor value
                  
                DataWindow dw1 = findPlot(addr1,val,time);  
                this.Average(val, addr, time);
                if(addr.equals("0014.4F01.0000.7F6D")){
                    DataWindow dw = findPlot(dg.getAddressAsLong(),val,time);
                    dw.addData(time, Average[0]);
                }
                if(addr.equals("0014.4F01.0000.3560")){
                    DataWindow dw = findPlot(dg.getAddressAsLong(),val,time);
                    dw.addData(time, Average[1]);
                }
                if(addr.equals("0014.4F01.0000.465E")){
                    DataWindow dw = findPlot(dg.getAddressAsLong(),val,time);
                    dw.addData(time, Average[2]);
                }
                dw1.addData(time, Average[3]);
                
            } catch (Exception e) {
                System.err.println("Caught " + e +  " while reading sensor samples.");
                throw e;
            }
        }
    }
    
    public double Calibration(double input, double real1, double real2, double test1, double test2){
        double Calibra1, Calibra2, result;
        if((test1!=test2)){
            Calibra1= (real1-real2)/(test1-test2);
            Calibra2 = (test1*real2 - test2*real1)/(test1-test2);
            result = (Calibra1 * input) + Calibra2;
            //System.out.println("a1="+Calibra1);
            //System.out.println("a2="+Calibra2);
        }   
        else{
            result = input;
        }
        return result;
    }
        
    public void Average(double val,String addr,long time){
        if(addr.equals("0014.4F01.0000.7F6D")){
            a0[1]=a0[0];
            a0[2]=a0[1];
            a0[0]=val;
            if(a0[2]!=200){
                Average[0]=(a0[0]+a0[1]+a0[2])/3;
            }else{
                if(a0[1]!=200){
                    Average[0]=(a0[0]+a0[1])/2;
                }else{
                    Average[0]=val;
                }
            }    
            Average[0]=Calibration(Average[0],REAL1,REAL2,TEST1_S1,TEST2_S1);
            w1.Window0(Average[0], addr, time);
            state[0]=1;
            sensor1 = 1;
        }  
        if(addr.equals("0014.4F01.0000.3560")){
            a1[1]=a1[0];
            a1[2]=a1[1];
            a1[0]=val;
            if(a1[2]!=200){
                Average[1]=(a1[0]+a1[1]+a1[2])/3;
            }else{
                if(a1[1]!=200){
                    Average[1]=(a1[0]+a1[1])/2;
                }else{
                    Average[1]=val;
                }
            }            
            Average[1]=Calibration(Average[1],REAL1,REAL2,TEST1_S2,TEST2_S2);
            w1.Window1(Average[1], addr, time);
            state[1]=1;            
            sensor2 = 1;
        }  
        if(addr.equals("0014.4F01.0000.465E")){
            a2[1]=a2[0];
            a2[2]=a2[1];
            a2[0]=val;
            if(a2[2]!=200){
                Average[2]=(a2[0]+a2[1]+a2[2])/3;
            }   
            else{
                if(a2[1]!=200){
                    Average[2]=(a2[0]+a2[1])/2;
                }else{
                    Average[2]=val;
                }
            }   
            Average[2]=Calibration(Average[2],REAL1,REAL2,TEST1_S3,TEST2_S3);
            w1.Window2(Average[2], addr, time);
            state[2]=1;            
            sensor3 = 1;
        }
        if((sensor1+sensor2+sensor3)==3)
        {
            Average[3]=(Average[0] + Average[1] + Average[2])/(sensor1+sensor2+sensor3);
            w1.Window3(Average[3],time);
        }
    }
    
    /**
     * Start up the host application.
     *
     * @param args any command line arguments
     */
    public static void main(String[] args) throws Exception {
        // register the application's name with the OTA Command server & start OTA running
//        OTACommandServer.start("SendDataDemo-GUI");
//
//        SendDataDemoGuiHostApplication app = new SendDataDemoGuiHostApplication();
//        app.setup();
//        app.run();
//        
//        SettingsProperty property = new SettingsProperty();
//        int max = property.getMax_no_of_sensors();
//        String auto = property.getSensor_auto_detect();
//        ArrayList<String> list = property.getReserved_sensor_list();
    }
}
