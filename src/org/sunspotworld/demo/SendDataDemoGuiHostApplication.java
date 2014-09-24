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
import static dao.generated.tables.Temperature.TEMPERATURE;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import javax.microedition.io.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;


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
    private final int host_port;
    
    // Sensor related
    private int numberOfSensors;
    private boolean useReservedSensors;
    private ArrayList<String> reservedSensorList;
    private static final int MAX_DEGREE = 1000;
    private static final int GENERIC_SENSOR_ID = 9999;
            
    // Calibration parameters
    private double real1;
    private double real2;
    private ArrayList<Double> testList1;
    private ArrayList<Double> testList2;
    
//    int sensor1 = 0;
//    int senssensor1or2 = 0;
//    int sensor3 = 0;
    
    private JTextArea status;
    private long[] addresses = new long[8];
    private DataWindow[] plots = new DataWindow[8];
    DateFormat fmt = DateFormat.getTimeInstance();
    double[] a0=new double[3];
    double[] a1=new double[3];
    double[] a2=new double[3]; 
    double[] average=new double[4];
    int[] state=new int[3];
//    DisplayWindow w1;
    //Thread t=new Thread(w1);
    
    public SendDataDemoGuiHostApplication(){
        SettingsProperty property = new SettingsProperty();
        host_port =  property.getHost_port();
        numberOfSensors = property.getMax_no_of_sensors();
        String useReserved = property.getUse_reserved_sensors();
        if(useReserved.equalsIgnoreCase("yes")){
            useReservedSensors = true;
            reservedSensorList = new ArrayList(property.getReserved_sensor_list());
        }else if(useReserved.equalsIgnoreCase("no")){
            useReservedSensors = false;
            reservedSensorList = null;
        }
        
//        w1=new DisplayWindow();
    }
    
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
                if(addr==GENERIC_SENSOR_ID){
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
        a0[0]=MAX_DEGREE;
        a0[1]=MAX_DEGREE;
        a0[2]=MAX_DEGREE;
        a1[0]=MAX_DEGREE;
        a1[1]=MAX_DEGREE;
        a1[2]=MAX_DEGREE;
        a2[0]=MAX_DEGREE;
        a2[1]=MAX_DEGREE;
        a2[2]=MAX_DEGREE;
        state[0]=0;
        state[1]=0;
        state[2]=0;
        try {
            // Open up a server-side broadcast radiogram connection
            // to listen for sensor readings being sent by different SPOTs
            //t.start();
            rCon = (RadiogramConnection) Connector.open("radiogram://:" + host_port);
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
                String addr = dg.getAddress().substring(15); // Get the last 4 digits
                long time = dg.readLong();      // read time of the reading
                double val = dg.readDouble();         // read the sensor value
                System.out.println("***** " + addr + "\t" + time + "\t" + val);
                
                // Save into database
                saveToDB(addr, (int)time, (float)val);
                 
//                this.getAverage(val, addr, time);
                if(addr.equals("7F6D")){
                    DataWindow dw = findPlot(dg.getAddressAsLong(),val,time);
//                    dw.addData(time, average[0]);
                    dw.addData(time, val);
                }
                if(addr.equals("3560")){
                    DataWindow dw = findPlot(dg.getAddressAsLong(),val,time);
//                    dw.addData(time, average[1]);
                    dw.addData(time, val);
                }
                if(addr.equals("465E")){
                    DataWindow dw = findPlot(dg.getAddressAsLong(),val,time);
//                    dw.addData(time, average[2]);
                    dw.addData(time, val);
                }
                
//                DataWindow dwGeneric = findPlot(GENERIC_SENSOR_ID,val,time); 
//                dwGeneric.addData(time, average[3]);
//                dwGeneric.addData(time, val);
                
            } catch (Exception e) {
                System.err.println("Caught " + e +  " while reading sensor samples.");
                throw e;
            }
        }
    }
    
    public void saveToDB(String address, int timestamp, float value){
        java.sql.Connection conn = null;
        try {
            // For Sqlite
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:library.db");

            DSLContext create = DSL.using(conn, SQLDialect.SQLITE);

            create.insertInto(TEMPERATURE, TEMPERATURE.ADDR, TEMPERATURE.TIME, TEMPERATURE.TEMPERATURE_)
                    .values(address, timestamp, value).execute();

//                    Result<Record> result = create.select().from(TEMPERATURE).fetch();
//                    System.out.println("Read from db:");
//                    for (Record r : result) {
//                        String add = r.getValue(TEMPERATURE.ADDR);
//                        int tm = r.getValue(TEMPERATURE.TIME);
//                        float vl = r.getValue(TEMPERATURE.TEMPERATURE_);
//
//                        System.out.println("address: " + add + " \ttime: " + tm + " \tvalue: " + vl);
//                    }
        } catch (Exception e) {
            // For the sake of this tutorial, let's keep exception handling simple
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
    
    public double calibration(double input, double real1, double real2, double test1, double test2){
        double calibra1, calibra2, result;
        if((test1!=test2)){
            calibra1= (real1-real2)/(test1-test2);
            calibra2 = (test1*real2 - test2*real1)/(test1-test2);
            result = (calibra1 * input) + calibra2;
            //System.out.println("a1="+calibra1);
            //System.out.println("a2="+calibra2);
        }   
        else{
            result = input;
        }
        return result;
    }
        
//    public void getAverage(double val,String addr,long time){
//        if(addr.equals("7F6D")){
//            a0[1]=a0[0];
//            a0[2]=a0[1];
//            a0[0]=val;
//            if(a0[2]!=MAX_DEGREE){
//                average[0]=(a0[0]+a0[1]+a0[2])/3;
//            }else{
//                if(a0[1]!=MAX_DEGREE){
//                    average[0]=(a0[0]+a0[1])/2;
//                }else{
//                    average[0]=val;
//                }
//            }
//            double test1 = testList1.get(0);
//            double test2 = testList2.get(0);
//            average[0]=calibration(average[0], real1, real2, test1, test2);
////            w1.Window0(average[0], addr, time);
//            state[0]=1;
////            sensor1 = 1;
//        }  
//        if(addr.equals("3560")){
//            a1[1]=a1[0];
//            a1[2]=a1[1];
//            a1[0]=val;
//            if(a1[2]!=MAX_DEGREE){
//                average[1]=(a1[0]+a1[1]+a1[2])/3;
//            }else{
//                if(a1[1]!=MAX_DEGREE){
//                    average[1]=(a1[0]+a1[1])/2;
//                }else{
//                    average[1]=val;
//                }
//            }            
//            double test1 = testList1.get(1);
//            double test2 = testList2.get(1);
//            average[1]=calibration(average[1], real1, real2, test1, test2);
////            w1.Window1(average[1], addr, time);
//            state[1]=1;            
////            sensor2 = 1;
//        }  
//        if(addr.equals("465E")){
//            a2[1]=a2[0];
//            a2[2]=a2[1];
//            a2[0]=val;
//            if(a2[2]!=MAX_DEGREE){
//                average[2]=(a2[0]+a2[1]+a2[2])/3;
//            }   
//            else{
//                if(a2[1]!=MAX_DEGREE){
//                    average[2]=(a2[0]+a2[1])/2;
//                }else{
//                    average[2]=val;
//                }
//            }   
//            double test1 = testList1.get(2);
//            double test2 = testList2.get(3);
//            average[2]=calibration(average[2], real1, real2, test1, test2);
////            w1.Window2(average[2], addr, time);
//            state[2]=1;            
////            sensor3 = 1;
//        }
////        if((sensor1+sensor2+sensor3)==3)
////        {
////            average[3]=(average[0] + average[1] + average[2])/(sensor1+sensor2+sensor3);
////            w1.Window3(average[3],time);
////        }
//    }
    
     public void getAverage(double val,String addr,long time){
//        if((sensor1+sensor2+sensor3)==3)
//        {
//            average[3]=(average[0] + average[1] + average[2])/(sensor1+sensor2+sensor3);
//            w1.Window3(average[3],time);
//        }
    }
     
    /**
     * Start up the host application.
     *
     * @param args any command line arguments
     */
    public static void main(String[] args) throws Exception {
        try{
            // register the application's name with the OTA Command server & start OTA running
            OTACommandServer.start("SendDataDemo-GUI");

            SendDataDemoGuiHostApplication app = new SendDataDemoGuiHostApplication();
            app.setup();
            app.run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
