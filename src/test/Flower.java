package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

// tomado de https://trinket.io/python/8c5713a987
public class Flower {
    World world;
    Turtle t;

    public Flower() {
        world = new World(new Dimension(640, 480), "Flower", new Color(249, 249, 249));
        t = world.createTurtle();
    }

    public void regularShape(double length, double sides, double angle) {
        for (int i = 0; i < sides; i++) {
            t.forward(length);
            t.left(angle);
        }
    }

    public void square(double length) {
        regularShape(length, 4, 90);
    }

    public void slowArc(double radius, double segmentAngle, double steps) {
        double sides = steps;
        double angle = 360 / steps;
        double length = 2 * 3.142 * radius / steps;

        regularShape(length, sides, angle);
    }

    public void arc360(double radius, double segmentAngle) {
        slowArc(radius, segmentAngle, segmentAngle);
    }

    public void slowCircle(double radius, double steps) {
        slowArc(radius, 360, steps);
    }

    public void circle360(double radius) {
        slowArc(radius, 360, 360);
    }

    public void rectangle(double base, double height) {
        for (int i = 0; i < 2; i++) {
            t.forward(base);
            t.right(90);
            t.forward(height);
            t.right(90);
        }
    }

    public void leaf(double scale) {
        double length = 0.6 * scale;

        t.left(45);
        t.forward(length);
        t.right(45);
        t.forward(length);
        t.right(135);
        t.forward(length);
        t.right(45);
        t.forward(length);
        t.right(180);
    }

    public void moveAround(double relX, double relY, boolean back) {
        if (back) {
            relX = -1 * relX;
            relY = -1 * relY;
        }
        t.forward(relX);
        t.right(90);
        t.forward(relY);
        t.left(90);
    }

    public void stem(double base, double height, Color col) {
        t.setColor(col, col);
        t.setPenDown();
        t.beginFill();
        rectangle(base, height);
        moveAround(base, height * 0.7, false);
        // leaf(0.5 * height);
        moveAround(base, height * 0.7, true);
        t.endFill();
        t.setPenUp();
    }

    public void filledCircle(double radius, Color col) {
        t.setColor(col, col);
        t.setPenDown();
        t.beginFill();
        t.circle(radius, 360, 0);
        t.endFill();
        t.setPenUp();
    }

    public void petals(double radius, double bloomDiameter, double noOfPetals, Color col) {
        t.setPenUp();
        t.setColor(col, col);
        double petalFromEye = 1.5 * radius;
        double relY = (radius + petalFromEye) / 2;
        double relX = petalFromEye / 2;
        double angle = 360 / noOfPetals;
        moveAround(relX, relY, false);
        for (int i = 0; i < noOfPetals; i++) {
            t.setPenDown();
            t.beginFill();
            t.circle(radius, 360, 0);
            t.endFill();
            t.setPenUp();
            t.left((i + 1) * angle);
            t.forward(petalFromEye);
            t.right((i + 1) * angle);
        }
        moveAround(relX, relY, true);
    }

    public void bloom(double relX, double bloomDiameter, Color colPetal, Color colEye) {
        double smallRadius = bloomDiameter / 5;
        t.setPenUp();
        t.forward(relX);
        petals(smallRadius, bloomDiameter, 6, colPetal);
        filledCircle(smallRadius, colEye);
        t.forward(-relX);
    }

    public void flower(double height) {
        stem(10, 0.6 * height, Color.GREEN);
        bloom(5, 0.8 * height, Color.WHITE, Color.YELLOW);
    }

    public void run() {
        t.setSpeed(500);

        t.setPenUp();
        t.setPosition(-160, 0);
        t.setPenDown();
        t.setColor(Color.BLUE, Color.BLUE);
        square(50);

        t.setPenUp();
        t.setPosition(0, 0);
        t.setPenDown();
        flower(250);

        t.setPenUp();
        t.setPosition(160, 0);
        t.setPenDown();
        t.setColor(Color.MAGENTA, Color.MAGENTA);
        circle360(35);

        t.setColor(Color.RED, Color.RED);
        slowCircle(30, 36);

        t.setColor(Color.ORANGE, Color.ORANGE);
        slowCircle(25, 16);

        t.setColor(Color.BLUE, Color.BLUE);
        slowCircle(20, 8);

        t.setVisible(false);

        world.waitForKey(KeyEvent.VK_ESCAPE);
        world.bye();
    }

    public static void main(String[] args) {
        Flower test = new Flower();
        test.run();
    }

}
