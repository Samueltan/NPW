/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author samueltango
 */
public class Config {
    private int sideAB;
    private int sideAC;
    private int sideBC;
    private int sideAD;
    private int sideDB;

    public Config(){
        Properties prop = new Properties();
        InputStream input = null;

        try {
                input = new FileInputStream("config.properties");

                // load the settings.properties file
                prop.load(input);

                // get the property value and print it out
                sideAB = Integer.parseInt(prop.getProperty("AB"));
                sideAC = Integer.parseInt(prop.getProperty("AC"));
                sideBC = Integer.parseInt(prop.getProperty("BC"));
                sideAD = Integer.parseInt(prop.getProperty("AD"));
                sideDB = Integer.parseInt(prop.getProperty("DB"));

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

    public int getSideAB() {
        return sideAB;
    }

    public int getSideAC() {
        return sideAC;
    }

    public int getSideBC() {
        return sideBC;
    }

    public int getSideAD() {
        return sideAD;
    }

    public int getSideDB() {
        return sideDB;
    }
}
