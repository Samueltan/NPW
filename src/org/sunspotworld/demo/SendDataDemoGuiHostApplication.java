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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private int maxNumberOfSensors;
    private boolean useReservedSensors;
    private ArrayList<String> reservedSensorList;
    private static final int MAX_DEGREE = 1000;
    private static final int GENERIC_SENSOR_ID = 9999;
            
    // Calibration parameters
    private double real1;
    private double real2;
    private ArrayList<Double> test1;
    private ArrayList<Double> test2;
    private ArrayList<String> sensors;
    
    private JTextArea status;
    private long[] addresses = new long[8];
    private DataWindow[] plots = new DataWindow[8];
    DateFormat fmt = DateFormat.getTimeInstance();
    double[] average=new double[4];
//    DisplayWindow w1;
    //Thread t=new Thread(w1);
    
    public SendDataDemoGuiHostApplication(){
        SettingsProperty property = new SettingsProperty();
        host_port =  property.getHost_port();
        maxNumberOfSensors = property.getMax_no_of_sensors();
        String useReserved = property.getUse_reserved_sensors();
        if(useReserved.equalsIgnoreCase("yes")){
            useReservedSensors = true;
            reservedSensorList = new ArrayList(property.getReserved_sensor_list());
        }else if(useReserved.equalsIgnoreCase("no")){
            useReservedSensors = false;
            reservedSensorList = null;
        }
        
        real1 = property.getReal1();
        real2 = property.getReal2();
        test1 = property.getTest1();
        test2 = property.getTest2();
        
        sensors = new ArrayList<String>();
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

        long time;
        // Main data collection loop
        while (true) {
            try {
                // Read sensor sample received over the radio
                rCon.receive(dg); 
                String addr = dg.getAddress().substring(15); // Get the last 4 digits
    
                time = dg.readLong();      // read time of the reading
//                time = System.currentTimeMillis() / 1000;
                
                double val = dg.readDouble();         // read the sensor value
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strTime = fmt.format(new Date(time)).toString();
                DecimalFormat df = new DecimalFormat(".0");
                System.out.println("***** " + addr + "\t" + strTime + "\t" + df.format(val));
                
                // Save into database
                saveToDB(addr, (int)time, (float)val);
                 
                DataWindow dw = null;
                // If use reserved sensors, compare the detected sensor with the reserved list
                // Only generate graph for the reserved sensors
                if(useReservedSensors){
//                    System.out.println("use reserved sensors");
                    if(reservedSensorList.contains(addr)){
                        dw = findPlot(dg.getAddressAsLong(),val,time);
                        dw.addData(time, val);
                        sensors.add(addr);
                    }
                }else{
//                    System.out.println("do not use reserved sensors");
                    // If donot use reserved sensors, all the detected will be accepted 
                    // (limited by the max_no_of_sensors)
                    if(sensors.contains(addr)){
                        System.out.println("*** contained:");
                        dw = findPlot(dg.getAddressAsLong(),val,time);
                        dw.addData(time, val);
                    }else{
                        System.out.println("*** not contained: size = " + sensors.size() + ", max = " + maxNumberOfSensors);
                        if(sensors.size() < maxNumberOfSensors){
                            dw = findPlot(dg.getAddressAsLong(),val,time);
                            dw.addData(time, val);
                            sensors.add(addr);
                        }
                    }
                }
                
                float avg = getAverage(time);
                dw = findPlot(GENERIC_SENSOR_ID, avg,time);
                dw.addData(time, avg);
                
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
            conn = DriverManager.getConnection("jdbc:sqlite:challenges.db");

            DSLContext create = DSL.using(conn, SQLDialect.SQLITE);

            create.insertInto(TEMPERATURE, TEMPERATURE.ADDR, TEMPERATURE.TIME, TEMPERATURE.TEMPERATURE_)
                    .values(address, timestamp, value).execute();

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
    
    public float getAverage(long time){
        java.sql.Connection conn = null;
        float rtn = 0;
        try {
            // For Sqlite
            Class.forName("org.sqlite.JDBC").newInstance();
            conn = DriverManager.getConnection("jdbc:sqlite:challenges.db");
//            System.out.println("***** time = " + time);
//            System.out.println("***** conn = " + conn);
            DSLContext create = DSL.using(conn, SQLDialect.SQLITE);
            Result<Record> result = create.select().from(TEMPERATURE).where(TEMPERATURE.TIME.equal(time)).fetch();
            System.out.println("Read from db:");

            float sum = 0;
            for (Record r : result) {
                String add = r.getValue(TEMPERATURE.ADDR);
                float vl = r.getValue(TEMPERATURE.TEMPERATURE_);
                sum += vl;
                System.out.println("address: " + add + " \tvalue: " + vl);
            }
            rtn = sum / result.size();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }

        return rtn;
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
