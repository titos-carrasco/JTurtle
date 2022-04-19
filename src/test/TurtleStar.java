package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

public class TurtleStar {
    World world;
    Turtle t;

    public TurtleStar() {
        world = new World(new Dimension(800, 600), "Turtle Star", Color.WHITE);
        t = world.createTurtle();

        String path = world.getRealPath(this, "./resources");
        world.setBgImage(path + "/Grid 800x600.png");
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
        t.setPenUp();

        t.setPosition(-110, -220);
        t.setPenColor(Color.BLACK);
        t.write("Presione 'ESC' para finalizar", "Arial", 20, Font.PLAIN);

        t.setPosition(0, -250);
        t.setHeading(90);

        world.waitForKey(KeyEvent.VK_ESCAPE);
        world.bye();
    }

    public static void main(String[] args) {
        TurtleStar test = new TurtleStar();
        test.run();
    }

}
