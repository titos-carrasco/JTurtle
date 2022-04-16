package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

// tomado de https://benedictxneo.medium.com/koch-snowflake-using-python-turtle-5b9ff5f42572
public class SnowFlake {
    World world;
    Turtle t;

    public SnowFlake() {
        world = new World(new Dimension(640, 480), "Snow Flake", Color.WHITE);
        t = world.createTurtle();
    }

    public void snowflake(double lengthSide, int levels) {
        if (levels == 0) {
            t.forward(lengthSide);
            return;
        }
        lengthSide /= 3.0;
        snowflake(lengthSide, levels - 1);
        t.left(60);
        snowflake(lengthSide, levels - 1);
        t.right(120);
        snowflake(lengthSide, levels - 1);
        t.left(60);
        snowflake(lengthSide, levels - 1);
    }

    public void run() {
        t.setSpeed(10);
        double length = 300.0;

        t.setPenUp();
        t.backward(length / 2.0);

        t.setPenDown();
        for (int i = 0; i < 3; i++) {
            snowflake(length, 4);
            t.right(120);
        }

        world.waitForKey(KeyEvent.VK_ESCAPE);
        world.bye();
    }

    public static void main(String[] args) {
        SnowFlake test = new SnowFlake();
        test.run();
    }

}
