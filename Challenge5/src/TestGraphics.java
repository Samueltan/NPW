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
        final int clear_block_width = 200;       // Repainting area that needs to be cleared to display new location value
        final int clear_block_height = 12;      // Repainting area that needs to be cleared to display new location value
        start = new Point(0, 80);
        end = new Point(0, 80);
        g.setColor(Color.blue);
        Runnable run = new Runnable() {
            Point temp = null;
            int x = 0;
            int y = 200;
            String strLocation;
            boolean outOfScope = false;

            public void run() {
                int d = 1;
                while(true) {
                    try {
                        System.out.println(x + ", " + y);
                        if(y>10000){
                            x = 150;
                            y = 230;
                        }
                        if(x>800 || x<0 || y>600 || y<0) {
                            g.setColor(Color.RED);
                            strLocation = "Sensor Location: Out of detection scope!";
                            if(!outOfScope)
                                g.clearRect(Constants.WINDOW_WIDTH / 2 - 8, Constants.WINDOW_HEIGHT - 20, clear_block_width, clear_block_height);

                            g.drawString(strLocation, Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT - 10);
                            outOfScope = true;

                        }else {
                            g.setColor(Color.BLUE);
                            // Show the sensor at the location
//                            temp = new Point(x, 80+(int)(40*Math.sin(Math.PI*(x-80)/30)));
                            temp = new Point(x, y);
                            g.drawLine(start.x, start.y, end.x, end.y);

                            // Update location label
                            strLocation = "Sensor Location: " + x + ", " + y;
                            g.clearRect(Constants.WINDOW_WIDTH / 2 - 8, Constants.WINDOW_HEIGHT - 20, clear_block_width, clear_block_height);
                            g.drawString(strLocation, Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT - 10);

                            // Move to next location
                            start = end;
                            end = temp;
                            outOfScope = false;
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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