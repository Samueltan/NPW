/**
 * Created by Samuel on 2014/10/27.
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;


public class TestGraphics {
    public static void main(String[] args) {
        new DrawLocation();
    }
}

class DrawLocation extends JFrame {
    Point start;
    Point end;
    Container p;

    int windowWidth = Constants.CANVAS_WIDTH + 2 * Constants.CANVAS_MARGIN_WIDTH;       // 800 + 2 * 20 = 840
    int windowHeight = Constants.CANVAS_HEIGHT + 2 * Constants.CANVAS_MARGIN_HEIGHT + Constants.CANVAS_MARGIN_HEIGHT_OFFSET;    // 600 + 2 * 20 + 30 = 670
    public DrawLocation() {
        p = getContentPane();
        int window_X, window_Y;

        window_X = (Constants.SCREEN_WIDTH - windowWidth) / 2;      // (1600 - 840)/2 = 380
        window_Y = (Constants.SCREEN_HEIGHT - windowHeight) / 2;    // (900 - 670)/2 = 115
        setBounds(window_X, window_Y, windowWidth, windowHeight);   // (380, 110, 840, 670)
        setTitle("EC544 Challenge 5 - Group #2");
        setVisible(true);
        setLayout(null);
        paintComponents(this.getGraphics());
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void paintComponents(Graphics gg) {
//        gg.DrawLocation(10, 100, 200, 400);
        final Graphics g = gg;

        // Repainting area that needs to be cleared to display new location value
        final int clear_block_width = 200;
        final int clear_block_height = 12;
        start = new Point(0, 80);
        end = new Point(0, 80);
        g.setColor(Color.blue);
        Runnable run = new Runnable() {
            Point temp = null;
            int x = Constants.CANVAS_MARGIN_WIDTH;
            int y = 200;
            String strLocation;
            boolean outOfScope = false;

            public void run() {
                int d = 1;

                while(true) {
                    try {

                        g.setColor(Color.GRAY);
                        g.drawRect(Constants.CANVAS_MARGIN_WIDTH, Constants.CANVAS_MARGIN_HEIGHT + Constants.CANVAS_MARGIN_HEIGHT_OFFSET,
                            Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);       // (20, 20, 800, 600)

                        if(x>windowWidth - Constants.CANVAS_MARGIN_WIDTH || x<Constants.CANVAS_MARGIN_WIDTH
                                || y>windowHeight - Constants.CANVAS_MARGIN_HEIGHT
                                || y<Constants.CANVAS_MARGIN_HEIGHT) {
                            System.out.println(x + ", " + y);
                            g.setColor(Color.RED);
                            strLocation = "Sensor Location: Out of detection scope!";
                            if(!outOfScope)
                                g.clearRect(windowWidth / 2 - 8, windowHeight - 20, clear_block_width, clear_block_height);

                            g.drawString(strLocation, windowWidth / 2 - 100, windowHeight - 10);
                            outOfScope = true;

                            break;
                        }else {
                            g.setColor(Color.BLUE);
                            // Show the sensor at the location
//                            temp = new Point(x, 80+(int)(40*Math.sin(Math.PI*(x-80)/30)));
                            temp = new Point(x, y);
                            g.drawLine(start.x, start.y, end.x, end.y);

                            // Update location label
                            strLocation = "Sensor Location: " + x + ", " + y;
                            g.clearRect(windowWidth / 2 - 8, windowHeight - 20, clear_block_width, clear_block_height);
                            g.drawString(strLocation, windowWidth / 2 - 100, windowHeight - 10);

                            // Move to next location
                            start = end;
                            end = temp;
                            outOfScope = false;
                        }
                        Thread.sleep(10);
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