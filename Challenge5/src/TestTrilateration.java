/**
 * Created by Samuel on 2014/10/27.
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;


public class TestTrilateration {
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

                        // Draw the sensor track in real-time mode
                        y = Constants.CANVAS_MARGIN_HEIGHT + (int)(40*Math.sin(Math.PI*(x-Constants.CANVAS_MARGIN_WIDTH)/30));
//                        y = Constants.CANVAS_MARGIN_HEIGHT + 3*(x-Constants.CANVAS_MARGIN_WIDTH)/4;
//                        System.out.println(x + ", " + y);
//                        if(!isInScope(x,y)) {
////                            System.out.println("Not in scope!");
//                            g.setColor(Color.RED);
//                            strLocation = "Sensor Location: Out of detection scope!";
//                            if(!outOfScope)
//                                g.clearRect(windowWidth / 2 - 8, windowHeight - 20, clear_block_width, clear_block_height);
//
//                            g.drawString(strLocation, windowWidth / 2 - 100, windowHeight - 10);
//                            outOfScope = true;
//
////                            break;
//                        }else {
////                            System.out.println("In scope!");
//                            // Show the sensor at the location
////                            temp = new Point(x, 60+(int)(40*Math.sin(Math.PI*(x-80)/30)));
//                            temp = new Point(x, y);
////                            g.drawLine(start.x, start.y, end.x, end.y);
//                            cleanCanvas(g);
//                            drawCenteredCircle(g, Color.RED, x, y, 8);
//
//                            // Update location label
//                            g.setColor(Color.BLUE);
//                            strLocation = "Sensor Location: " + x + ", " + y;
//                            g.clearRect(windowWidth / 2 - 8, windowHeight - 20, clear_block_width, clear_block_height);
//                            g.drawString(strLocation, windowWidth / 2 - 100, windowHeight - 10);
//
//                            // Move to next location
//                            start = end;
//                            end = temp;
//                            outOfScope = false;
//                        }

                        // Draw the x, y axis
                        g.setColor(Color.GRAY);
                        drawXYAxis(g);

                        // Show the fixed sensors' locations
                        /**
                         *          C               D
                         *
                         *
                         *          A               B
                         */
                        drawFixedSensors(g);

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

    public void drawXYAxis(Graphics g){

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
    }

    public void drawFixedSensors(Graphics g) {
        Point pointA, pointB, pointC, pointD;
//        int x1 = Constants.CANVAS_MARGIN_WIDTH;
//        int y1 = Constants.CANVAS_MARGIN_HEIGHT + Constants.CANVAS_HEIGHT;
//        int x2 = Constants.CANVAS_MARGIN_WIDTH + Constants.CANVAS_WIDTH;
//        int y2 = Constants.CANVAS_MARGIN_HEIGHT + Constants.CANVAS_HEIGHT;
//        int x3 = Constants.CANVAS_MARGIN_WIDTH;
//        int y3 = Constants.CANVAS_MARGIN_HEIGHT;
//        int x4 = x2;
//        int y4 = y3;

        Config config = new Config();
        double a = config.getSideBC();
        double b = config.getSideAC();
        double c = config.getSideAB();
        Trilateration triABC = new Trilateration(a, b, c);
        pointA = triABC.getVertexA().toPoint();
        pointB = triABC.getVertexB().toPoint();
        pointC = triABC.getVertexC().toPoint();

        a = config.getSideDB();
        b = config.getSideAD();
        c = config.getSideAB();
        triABC = new Trilateration(a, b, c);
        pointD = triABC.getVertexC().toPoint();

        Color color = Color.BLUE;
        // Sensor A
        drawCenteredCircle(g, color, pointA.x, pointA.y, 10);
        g.drawString("Sensor A", pointA.x - 30, pointA.y + 18);

        // Sensor B
        drawCenteredCircle(g, color, pointB.x, pointB.y, 10);
        g.drawString("Sensor B", pointB.x - 30, pointB.y + 18);

        // Sensor C
        drawCenteredCircle(g, color, pointC.x, pointC.y, 10);
        g.drawString("Sensor C", pointC.x - 30, pointC.y - 10);

        // Sensor D
        drawCenteredCircle(g, color, pointD.x, pointD.y, 10);
        g.drawString("Sensor D", pointD.x - 30, pointD.y - 10);
    }

    public void drawCenteredCircle(Graphics gg, Color color, int x, int y, int r) {
        gg.setColor(color);
        x = x-(r/2);
        y = y-(r/2);
        gg.fillOval(x,y,r,r);
    }

    public void cleanCanvas(Graphics gg){
        int cleanX = Constants.CANVAS_MARGIN_WIDTH + 1;
        int cleanY = Constants.CANVAS_MARGIN_HEIGHT - 5;
        gg.clearRect(cleanX, cleanY, Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
    }

    public boolean isInScope(int x, int y){
        return !(x>windowWidth - Constants.CANVAS_MARGIN_WIDTH || x<Constants.CANVAS_MARGIN_WIDTH
                || y>windowHeight - Constants.CANVAS_MARGIN_HEIGHT
                || y<Constants.CANVAS_MARGIN_HEIGHT);
    }
}