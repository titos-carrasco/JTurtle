package test;

import java.awt.Color;
import java.awt.Dimension;

import rcr.turtle.Turtle;
import rcr.turtle.World;

public class Test01 {
    World world;
    Turtle t;

    public Test01() {
        world = new World(new Dimension(640, 480), "Test 01", Color.WHITE);
        //world.setBgImage("src/test/fondo.jpeg");

        t = world.createTurtle();
        t.setShape("Triangle");
        t.setShapeScale(2);
        t.setShapeColor( Color.BLUE);
        t.setHeading(0);
        t.setPosition(0, 0);
        t.setSpeed(1);

        t.setPenUp();
        t.setPenColor(Color.RED);
        t.setPenSize(5);

        t.left(135);
        t.forward(240);
        t.right(135);
        t.setPenDown();
        hilbert(4, 90, 20);

        t.setPenUp();
        //t.setPosition(0, 0);
        t.setHeading(0);
    }

    public void hilbert(double level, double angle, double step) {
        if (level == 0)
            return;
        t.right(angle);
        hilbert(level - 1, 360 - angle, step);

        t.forward(step);
        t.left(angle);
        hilbert(level - 1, angle, step);

        t.forward(step);
        hilbert(level - 1, angle, step);

        t.left(angle);
        t.forward(step);
        hilbert(level - 1, 360 - angle, step);
        t.right(angle);
    }

    public void run() {
        world.mainLoop(60);
    }

    public static void main(String[] args) {
        Test01 test = new Test01();
        test.run();
    }

}
