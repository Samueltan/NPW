/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sunspotworld.demo;


import java.awt.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author Xin
 */
public class Window extends JFrame{
    JTextArea jt0,jt1,jt2,jt3;
    JScrollPane js0,js1,js2,js3;
    JScrollBar sb0,sb1,sb2,sb3;
    JLabel jl1,jl2,jl3,jl4;
    int i1=0;
    DateFormat fmt = DateFormat.getTimeInstance();
    DecimalFormat df = new DecimalFormat(".00");
    
    
    public Window(){
        
        this.setLayout(new GridLayout(8,1));
        jl4=new JLabel("The Sensors'");
        jl1=new JLabel("The No.1 Sensor(the address is 0014.4F01.0000.7F6D)");
        jl2=new JLabel("The No.2 Sensor(the address is 0014.4F01.0000.3560D)");
        jl3=new JLabel("The No.3 Sensor(the address is 0014.4F01.0000.465E)");
        js3 = new javax.swing.JScrollPane();
        js0 = new javax.swing.JScrollPane();
        js1 = new javax.swing.JScrollPane();
        js2 = new javax.swing.JScrollPane(); 
        js3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        js0.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        js1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        js2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        jt3 = new javax.swing.JTextArea();
        jt0 = new javax.swing.JTextArea();
        jt1 = new javax.swing.JTextArea();
        jt2 = new javax.swing.JTextArea();       
        jt3.setColumns(20);
        jt3.setEditable(false);
        jt3.setRows(4);
        jt0.setColumns(20);
        jt0.setEditable(false);
        jt0.setRows(4);
        jt1.setColumns(20);
        jt1.setEditable(false);
        jt1.setRows(4);
        jt2.setColumns(20);
        jt2.setEditable(false);
        jt2.setRows(4);
        js3.setViewportView(jt3);
        js0.setViewportView(jt0);
        js1.setViewportView(jt1);
        js2.setViewportView(jt2);
        
        this.add(jl4);
        this.add(js3);
        this.add(jl1);
        this.add(js0);
        this.add(jl2);
        this.add(js1);
        this.add(jl3);
        this.add(js2);
        this.setTitle("The Average Temperature");
        this.setSize(600,700);
        this.setLocation(500,0);
        this.setVisible(true);
    }
    
    public void Window0(double val,String addr,long time){
        jt0.append("Temperature of sensor 1:  "+ df.format(val) + " °„C at  " + fmt.format(new Date(time)) + "\n");
        jt0.setCaretPosition(jt0.getText().length()); 
        int height=10;
        Point p = new Point();
        p.setLocation(0,this.jt0.getLineCount()*height);
        this.js0.getViewport().setViewPosition(p);
        repaint();
    }
    
    public void Window1(double val,String addr,long time){
        jt1.append("Temperature of sensor 2:  "+ df.format(val) + " °„C at  " + fmt.format(new Date(time)) + "\n");
        jt1.setCaretPosition(jt1.getText().length());
        int height=10;
        Point p = new Point();
        p.setLocation(0,this.jt1.getLineCount()*height);
        this.js1.getViewport().setViewPosition(p);
        repaint();
    }
    
    public void Window2(double val,String addr,long time){
        jt2.append("Temperature of sensor 3:  "+ df.format(val) + " °„C at  " + fmt.format(new Date(time)) + "\n");
        jt2.setCaretPosition(jt2.getText().length());
        int height=10;
        Point p = new Point();
        p.setLocation(0,this.jt2.getLineCount()*height);
        this.js2.getViewport().setViewPosition(p);
        repaint();
    }
    public void Window3(double val,long time){
        jt3.append("Temperature of all sensors:  "+ df.format(val) + " °„C at  " + fmt.format(new Date(time)) + "\n");
        jt3.setCaretPosition(jt3.getText().length());        
        int height=10;
        Point p = new Point();
        p.setLocation(0,this.jt3.getLineCount()*height);
        this.js3.getViewport().setViewPosition(p);
        repaint();
    }
}
