import static java.lang.Math.sqrt;

/**
 * Created by Samuel on 2014/10/27.
 * A, B, C represent 3 sensors that are put in fixed location
 */
public class Trilateration {
    // The 3 sides of the triangle aligned by location-fixed sensors (A, B, C)
    // BC = a, AC = b, AB = c;
    private double a, b, c;

    // The coordinates of the 3 points of the triangle
    // For simplicity purpose, it is assumed that the 3 points' coordinates will be taken as below:
    // Point A (x1, y1) = (0, 0)
    // Point B (x2, y2) = (c, 0)
    // Point C (x3, y3) = (b * cos(CAB), b * sin(CAB))
    private double x1, y1, x2, y2, x3, y3;

    // The distance between the to-be-test sensor and each other location-fixed sensors
    private double r1, r2, r3;

    // Given the 3 sides of the triangle, the location of each vertex will be calculated automated
    public Trilateration(double a, double b, double c){
        this.a = a;     // a = BC
        this.b = b;     // b = AC
        this.c = c;     // c = AB

        x1 = y1 = 0;
        x2 = c;
        y2 = 0;
        x3 = (b*b + c*c - a*a) / (2*c);
        y3 = sqrt(b*b - x3 * x3);
    }

    // Get the location of the vertex A
    public Location getVertexA(){
        return new Location(x1, y1);
    }

    // Get the location of the vertex B
    public Location getVertexB(){
        return new Location(x2, y2);
    }

    // Get the location of the vertex C
    public Location getVertexC(){
        return new Location(x3, y3);
    }

    // Get the location of the unkonwn position based on the distance to each fixed sensor (A, B, C)
    public Location getLocationFromDistance(double r1, double r2, double r3){
        double x, y;
        x = (r1 * r1 - r2 * r2 + c*c) / (2*c);
        y = (r1 * r1 - r3 * r3 + b*b - 2 * x * x3) / (2 * y3);
        return new Location(x, y);
    }

    class Location{
        double x, y;
        public Location(double x, double y){
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public Point toPoint(){
            return new Point((int) x, (int)y);
        }
    }
}
