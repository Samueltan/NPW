/**
 * Created by Samuel on 2014/10/27.
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;


public class TestGraphics {
    public static void main(String[] args) {
        new DrawLine();
    }
}

class DrawLine extends JFrame {
    Point start;
    Point end;
    Container p;
    public DrawLine() {
        p = getContentPane();
        int window_X, window_Y;
        
        window_X = (Constants.SCREEN_WIDTH - Constants.WINDOW_WIDTH) / 2;
        window_Y = (Constants.SCREEN_HEIGHT - Constants.WINDOW_HEIGHT) / 2;
        setBounds(window_X, window_Y, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setVisible(true);
        setLayout(null);
        paintComponents(this.getGraphics());
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void paintComponents(Graphics gg) {
//        gg.drawLine(10, 100, 200, 400);
        final Graphics g = gg;
        start = new Point(0, 80);
        end = new Point(0, 80);
        g.setColor(Color.red);
        Runnable run = new Runnable() {
            Point temp = null;
            int x = 0;
            int y = 200;
            public void run() {
                int d = 1;
                while(true) {
                    try {
//                        temp = new Point(x, 80+(int)(40*Math.sin(Math.PI*(x-80)/30)));
                        temp = new Point(x, y);
                        g.drawLine(start.x, start.y, end.x, end.y);
                        g.clearRect(192, Constants.WINDOW_HEIGHT - 20, 50, 12);
                        g.drawString("Sensor Location: " + x + ", " + y, 100, Constants.WINDOW_HEIGHT - 10);
                        start = end;
                        end = temp;
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(x>800 || x<0) {
                        d = -d;
                        Color c = g.getColor();
                        if(c == Color.RED) {
                            g.setColor(Color.BLUE);
                        } else {
                            g.setColor(Color.RED);
                        }
                    }
                    x += d;
                    y += d;
                }
            }
        };
        new Thread(run).start();
    }

    class Point {
        int x, y;
        public Point(int _x, int _y) {
            x = _x;
            y = _y;
        }
    }
}