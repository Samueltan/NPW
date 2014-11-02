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

    int windowWidth = Constants.CANVAS_WIDTH + 2 * Constants.CANVAS_MARGIN_WIDTH;
    int windowHeight = Constants.CANVAS_HEIGHT + 2 * Constants.CANVAS_MARGIN_HEIGHT;
    public DrawLocation() {
        p = getContentPane();
        int window_X, window_Y;

        window_X = (Constants.SCREEN_WIDTH - windowWidth) / 2;
        window_Y = (Constants.SCREEN_HEIGHT - windowHeight) / 2;
        setBounds(window_X, window_Y, windowWidth, windowHeight);
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
        start = new Point(Constants.CANVAS_MARGIN_WIDTH, Constants.CANVAS_MARGIN_HEIGHT);
        end = new Point(Constants.CANVAS_MARGIN_WIDTH, Constants.CANVAS_MARGIN_HEIGHT);
        g.setColor(Color.blue);
        Runnable run = new Runnable() {
            Point temp = null;
            int x = Constants.CANVAS_MARGIN_WIDTH;
            int y;
            String strLocation;
            boolean outOfScope = false;

            public void run() {
                int d = 1;

                while(true) {
                    try {
                        g.setColor(Color.GRAY);
                        // Canvas area is defined to be a 800 * 600 area
//                        g.drawRect(Constants.CANVAS_MARGIN_WIDTH, Constants.CANVAS_MARGIN_HEIGHT,
//                            Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);       // (20, 50, 800, 600)

                        // Define the x, y axis
                        // x axis: (x0, yn) ~ (xn, yn)
                        // y axis: (x0, y0) ~ (x0, yn)
                        int x0 = Constants.CANVAS_MARGIN_WIDTH;
                        int y0 = Constants.CANVAS_MARGIN_HEIGHT;
                        int xn = x0 + Constants.CANVAS_WIDTH;
                        int yn = y0 + Constants.CANVAS_HEIGHT;
                        g.drawLine(x0, yn, xn, yn);     // draw x axis
                        g.drawLine(x0, y0, x0, yn);     // draw y axis
                        int tickInt = Constants.CANVAS_WIDTH / 10;
                        for (int xt = x0 + tickInt; xt < xn; xt += tickInt) {
                            g.drawLine(xt, yn + 5, xt, yn - 5);
                            int min = (xt - x0) / 1;
                            g.drawString(Integer.toString(min), xt - (min < 10 ? 3 : 7) , yn + 20);
                        }

                        tickInt = Constants.CANVAS_HEIGHT / 10;
                        for (int yt = y0 + tickInt; yt < yn; yt += tickInt) {
                            g.drawLine(x0 - 5, yt, x0 + 5, yt);
                            int min = (yt - y0) / 1;
                            g.drawString(Integer.toString(min), x0 - 32 , yt + 5);
                        }

                        // Show the fixed sensors' locations
                        Color blue = Color.BLUE;
                        drawCenteredCircle(g, blue, x0, yn, 10);
                        g.drawString("Sensor A", x0 - 30 , yn + 18);
                        drawCenteredCircle(g, blue, xn, yn, 10);
                        g.drawString("Sensor B", xn - 30 , yn + 18);
                        drawCenteredCircle(g, blue, x0, y0, 10);
                        g.drawString("Sensor C", x0 - 30 , y0 - 10);
                        drawCenteredCircle(g, blue, xn, y0, 10);
                        g.drawString("Sensor D", xn - 30 , y0 - 10);


                        // Draw the sensor track in real-time mode
//                        y = Constants.CANVAS_MARGIN_HEIGHT + (int)(40*Math.sin(Math.PI*(x-Constants.CANVAS_MARGIN_WIDTH)/30));
                        y = Constants.CANVAS_MARGIN_HEIGHT + 3*(x-Constants.CANVAS_MARGIN_WIDTH)/4;
//                        System.out.println(x + ", " + y);
                        if(!isInScope(x,y)) {
//                            System.out.println("Not in scope!");
                            g.setColor(Color.RED);
                            strLocation = "Sensor Location: Out of detection scope!";
                            if(!outOfScope)
                                g.clearRect(windowWidth / 2 - 8, windowHeight - 20, clear_block_width, clear_block_height);

                            g.drawString(strLocation, windowWidth / 2 - 100, windowHeight - 10);
                            outOfScope = true;

//                            break;
                        }else {
//                            System.out.println("In scope!");
                            g.setColor(Color.BLUE);
                            // Show the sensor at the location
//                            temp = new Point(x, 60+(int)(40*Math.sin(Math.PI*(x-80)/30)));
                            temp = new Point(x, y);
//                            g.drawLine(start.x, start.y, end.x, end.y);
                            g.clearRect(x-5, y-5, 10, 10);
                            drawCenteredCircle(g, Color.RED, x, y, 8);

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
//                    y += d;
                }
            }
        };
        new Thread(run).start();
    }

    public void drawCenteredCircle(Graphics gg, Color color, int x, int y, int r) {
        gg.setColor(color);
        x = x-(r/2);
        y = y-(r/2);
        gg.fillOval(x,y,r,r);
    }

    public boolean isInScope(int x, int y){
        return !(x>windowWidth - Constants.CANVAS_MARGIN_WIDTH || x<Constants.CANVAS_MARGIN_WIDTH
                || y>windowHeight - Constants.CANVAS_MARGIN_HEIGHT
                || y<Constants.CANVAS_MARGIN_HEIGHT);
    }

    class Point {
        int x, y;
        public Point(int _x, int _y) {
            x = _x;
            y = _y;
        }
    }
}