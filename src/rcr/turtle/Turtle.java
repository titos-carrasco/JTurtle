package rcr.turtle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Turtle {
    private World world;
    private Point2D.Double position;
    private double heading = 0;
    private boolean visible = true;
    private int speed = 5;

    private boolean penDown = false;
    private float penSize = 1;
    private Color penColor = Color.BLACK;

    private int shapeScaleX = 1;
    private int shapeScaleY = 1;
    private Color shapeColor = Color.BLACK;;
    private PolygonShape shape;

    private static HashMap<String, Class<? extends PolygonShape>> shapes;

    static {
        shapes = new HashMap<String, Class<? extends PolygonShape>>();
        registerShape("square", SquareShape.class);
        registerShape("triangle", TriangleShape.class);
        registerShape("classic", ClassicShape.class);
    }

    Turtle(World world) {
        this.world = world;
        position = new Point2D.Double(0, 0);
        setShape("classic");
        setPosition(0, 0);
    }

    public void setPosition(double x, double y) {
        if (penDown) {
            BufferedImage screen = world.getScreen();
            Point2D.Double p1 = world.toScreenCoordinates(position.x, position.y);
            Point2D.Double p2 = world.toScreenCoordinates(x, y);
            Graphics2D g2d = screen.createGraphics();
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(penSize));
            g2d.setColor(penColor);
            g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            g2d.setStroke(oldStroke);
            g2d.dispose();

            position.x = x;
            position.y = y;

            world.repaint();

            if (speed != 0)
                world.delay(200 / speed);
        } else {
            position.x = x;
            position.y = y;

            world.repaint();
        }
    }

    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y);
    }

    public void goHome() {
        setPosition(0, 0);
        setHeading(0);
    }

    public void setX(double x) {
        setPosition(x, position.y);
    }

    public double getX() {
        return position.x;
    }

    public void setY(double y) {
        setPosition(position.x, y);
    }

    public double getY() {
        return position.y;
    }

    public void setHeading(double angle) {
        angle = angle % 360;
        if (angle < 0)
            angle = 360 + angle;
        heading = angle;
        world.repaint();
    }

    public double getHeading() {
        return heading;
    }

    public double getDistance(double x, double y) {
        return position.distance(x, y);
    }

    public void forward(double steps) {
        double rad = Math.toRadians(heading);
        double x = steps * Math.cos(rad) + position.x;
        double y = steps * Math.sin(rad) + position.y;
        setPosition(x, y);
    }

    public void backward(double steps) {
        forward(-steps);
    }

    public void right(double angle) {
        setHeading(heading - angle);
        world.repaint();
    }

    public void left(double angle) {
        setHeading(heading + angle);
        world.repaint();
    }

    public void circle(double radius) {
        circle(radius, 360);
    }

    public void circle(double radius, double angle) { // FALTA lo del angulo
        double frac = Math.abs(angle) / 360;
        double steps = 1 + (int) (Math.min(11 + Math.abs(radius) / 6.0, 59.0) * frac);
        double w = 1.0 * angle / steps;
        double w2 = 0.5 * w;
        double l = 2.0 * radius * Math.sin(Math.toRadians(w2) * 1);
        for (double i = 0; i < steps; i++) {
            forward(l);
            left(w);
        }
    }

    public void dot(int diameter) {
        BufferedImage screen = world.getScreen();
        Point2D.Double p1 = world.toScreenCoordinates(position.x, position.y);
        Ellipse2D.Double dot = new Ellipse2D.Double(p1.x - diameter / 2, p1.y - diameter / 2, diameter, diameter);
        Graphics2D g2d = screen.createGraphics();
        g2d.setColor(penColor);
        g2d.fill(dot);
        g2d.dispose();
        world.repaint();
    }

    public void setPenDown() {
        penDown = true;
    }

    public void setPenUp() {
        penDown = false;
    }

    public void setPenSize(int size) {
        penSize = size;
    }

    public void setPenColor(Color color) {
        penColor = color;
    }

    public boolean isPenDown() {
        return penDown;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isvisible() {
        return visible;
    }

    public void setSpeed(int speed) {
        if (speed < 0)
            this.speed = 0;
        else if (speed > 10)
            this.speed = 10;
        else
            this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setShape(String name) {
        shape = createShape( name );
    }

    public void setShapeScale(int scaleX, int scaleY) {
        this.shapeScaleX = scaleX <= 0 ? 1 : scaleX;
        this.shapeScaleY = scaleY <= 0 ? 1 : scaleY;
    }

    public void setShapeColor(Color color) {
        shapeColor = color;
    }

    void drawShape(Graphics2D g2d) {
        if (!visible)
            return;

        // escalamos, rotamos y trasladamos
        AffineTransform at = new AffineTransform();
        at.scale(shapeScaleX, shapeScaleY);
        at.rotate(Math.toRadians(-90 + heading));
        Path2D.Double cpath = (Path2D.Double) at.createTransformedShape(shape.path);

        // a coordenadas de pantalla
        Polygon poly = new Polygon();
        PathIterator pi = cpath.getPathIterator(null);
        while (!pi.isDone()) {
            double[] xy = new double[2];
            pi.currentSegment(xy);
            Point2D.Double p = world.toScreenCoordinates(xy[0], xy[1]);
            poly.addPoint((int) (position.x + p.x), (int) (p.y - position.y));
            pi.next();
        }

        // trazamos la forma de la tortuga
        g2d.setColor(shapeColor);
        g2d.draw(poly);
        g2d.fill(poly);
    }

    public static void registerShape(String name, Class<? extends PolygonShape> shapeClass) {
        shapes.put(name, shapeClass);
    }

    static PolygonShape createShape(String name) {
        Class<? extends PolygonShape> polygonShapeClass = shapes.get(name);
        PolygonShape polygonShape = null;

        try {
            polygonShape = polygonShapeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return polygonShape;
    }

}
