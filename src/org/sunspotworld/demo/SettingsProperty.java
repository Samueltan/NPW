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
    private int max_no_of_sensors;
    private String sensor_auto_detect;
    private ArrayList<String> reserved_sensor_list;

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
    
}
