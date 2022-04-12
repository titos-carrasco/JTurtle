package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

public class Test01 {
    World world;
    Turtle t;

    public Test01() {
        world = new World(new Dimension(640, 480), "Test 01", Color.WHITE);
        // world.setBgImage("src/test/fondo.jpeg");

        t = world.createTurtle();
    }

    private void hilbert(double level, double angle, double step) {
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
        t.setShape("Triangle");
        t.setVisible(false);
        t.setShapeScale(1);
        t.setShapeColor(Color.BLUE);
        t.setHeading(0);
        t.setPosition(0, 0);
        t.setSpeed(0);

        t.setPenUp();
        t.setPenColor(Color.RED);
        t.setPenSize(5);

        t.left(135);
        t.forward(240);
        t.right(135);
        t.setPenDown();
        hilbert(4, 90, 20);

        t.setPenColor(Color.GREEN);
        t.dot(21);

        t.setPenUp();
        t.setHeading(0);

        while( !world.isKeyPressed( KeyEvent.VK_ESCAPE ) )
            world.delay(10);
        world.bye();
    }

    public static void main(String[] args) {
        Test01 test = new Test01();
        test.run();
    }

}
