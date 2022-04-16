package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

// tomado de https://www.geeksforgeeks.org/turtle-programming-python/
public class RainbowBenzene {
    World world;
    Turtle t;

    public RainbowBenzene() {
        world = new World(new Dimension(640, 480), "Rainbow Benzene", Color.BLACK);
        t = world.createTurtle();
    }

    public void run() {
        Color[] colors = {Color.RED, Color.MAGENTA, Color.BLUE, Color.GREEN, Color.ORANGE, Color.yellow };
        t.setPenDown();
        for(  int x = 0; x <360; x++ ) {
            t.setPenColor(colors[x%6]);
            t.setPenSize( x/100 + 1);
            t.forward(x);
            t.left(59);
        }

        world.waitForKey(KeyEvent.VK_ESCAPE);
        world.bye();

    }

    public static void main(String[] args) {
        RainbowBenzene test = new RainbowBenzene();
        test.run();
    }
}
