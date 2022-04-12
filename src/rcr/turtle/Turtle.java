package rcr.turtle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Turtle {
    private World world;
    private Point2D.Double position;
    private double heading;
    private boolean visible;
    private int speed;

    private boolean penDown;
    private float penSize;
    private Color penColor;

    private Color fillColor;
    private boolean fill;

    private TurtleShape shape;
    private int shapeScale;
    private Color shapeColor;

    Turtle(World world) {
        this.world = world;
        position = new Point2D.Double(0, 0);
        heading = 0;
        visible = true;
        speed = 5;

        penDown = false;
        penSize = 1;
        penColor = Color.BLACK;

        fillColor = Color.BLACK;
        fill = false;

        shape = new TriangleShape();
        shapeScale = 1;
        shapeColor = Color.BLACK;

        setPosition(0, 0);
    }

    public void dot( int diameter ) {
        BufferedImage screen = world.getScreen();
        Point2D.Double p1 = world.toScreenCoordinates(position.x, position.y);
        Ellipse2D.Double  dot = new Ellipse2D.Double(p1.x-diameter/2, p1.y-diameter/2, diameter, diameter);
        Graphics2D g2d = screen.createGraphics();
        g2d.setColor(penColor);
        g2d.fill(dot);
        g2d.dispose();
        world.repaint();
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
                world.delay(40 / speed);
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

    public double getAngle(double x, double y) {
        return 0;
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

    public void home() {
        setPosition(0, 0);
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

    public void setFillColor(Color color) {
        fillColor = color;
    }

    public void beginFill() {
        fill = true;
    }

    public void endFill() {
        fill = false;
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
        if (name.equals("Rectangle"))
            shape = new RectangleShape();
        else if (name.equals("Triangle"))
            shape = new TriangleShape();
        else
            System.out.printf("\n Shape '%s' no existe\n", name);
    }

    BufferedImage getShape() {
        return visible ? shape.getShape(heading, shapeScale, shapeColor) : null;
    }

    public void setShapeScale(int shapeScale) {
        this.shapeScale = shapeScale;
    }

    public void setShapeColor(Color color) {
        shapeColor = color;
    }

    public void setOnclick() {
    }

    public void setOnRelease() {
    }

    public void setOnDrag() {
    }

}
