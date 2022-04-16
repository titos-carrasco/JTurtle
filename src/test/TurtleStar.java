package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

public class TurtleStar {
    World world;
    Turtle t;

    public TurtleStar() {
        world = new World(new Dimension(640, 480), "Turtle Star", Color.WHITE);
        t = world.createTurtle();
    }

    public void run() {
        t.setPenColor(Color.red);
        t.setFillColor(Color.YELLOW);
        t.setPenDown();
        t.beginFill();
        while (true) {
            t.forward(200);
            t.left(170);
            if (t.getDistance(0, 0) < 1)
                break;
        }
        t.endFill();

        world.waitForKey(KeyEvent.VK_ESCAPE);
        world.bye();
    }

    public static void main(String[] args) {
        TurtleStar test = new TurtleStar();
        test.run();
    }

}
