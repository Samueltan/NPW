/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld.demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 *
 * @author samueltango
 */
public class SettingsProperty {
    private int host_port;
    private int max_no_of_sensors;
    private String sensor_auto_detect;
    private ArrayList<String> reserved_sensor_list;
    
    private double real1;
    private double real2;
    private ArrayList<Double> test1;
    private ArrayList<Double> test2;

    public SettingsProperty(){
        Properties prop = new Properties();
	InputStream input = null;
 
	try {
            input = new FileInputStream("settings.properties");

            // load the settings.properties file
            prop.load(input);

            // get the property value and print it out
            max_no_of_sensors = Integer.parseInt(prop.getProperty("max_no_of_sensors"));
            sensor_auto_detect = prop.getProperty("sensor_auto_detect");
            String sensorList = prop.getProperty("reserved_sensor_list");
            reserved_sensor_list = (ArrayList<String>) Arrays.asList(sensorList.split("\\s*,\\s*"));
 
            real1 = Double.parseDouble(prop.getProperty("calibration.real1"));
            real2 = Double.parseDouble(prop.getProperty("calibration.real2"));
            
            for(int ii=1; ii<=max_no_of_sensors; ++ii){
                test1.add(Double.parseDouble(prop.getProperty("calibration.test1.s" + ii)));
                test2.add(Double.parseDouble(prop.getProperty("calibration.test2.s" + ii)));
            }
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    }
    
    public int getMax_no_of_sensors() {
        return max_no_of_sensors;
    }

    public void setMax_no_of_sensors(int max_no_of_sensors) {
        this.max_no_of_sensors = max_no_of_sensors;
    }

    public String getSensor_auto_detect() {
        return sensor_auto_detect;
    }

    public void setSensor_auto_detect(String sensor_auto_detect) {
        this.sensor_auto_detect = sensor_auto_detect;
    }

    public ArrayList<String> getReserved_sensor_list() {
        return reserved_sensor_list;
    }

    public void setReserved_sensor_list(ArrayList<String> reserved_sensor_list) {
        this.reserved_sensor_list = reserved_sensor_list;
    }
    
    public int getHost_port() {
        return host_port;
    }

    public void setHost_port(int host_port) {
        this.host_port = host_port;
    }

    public double getReal1() {
        return real1;
    }

    public void setReal1(double real1) {
        this.real1 = real1;
    }

    public double getReal2() {
        return real2;
    }

    public void setReal2(double real2) {
        this.real2 = real2;
    }

    public ArrayList<Double> getTest1() {
        return test1;
    }

    public void setTest1(ArrayList<Double> test1) {
        this.test1 = test1;
    }

    public ArrayList<Double> getTest2() {
        return test2;
    }

    public void setTest2(ArrayList<Double> test2) {
        this.test2 = test2;
    }

}
