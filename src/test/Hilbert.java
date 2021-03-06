package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

public class Hilbert {
    World world;
    Turtle t;

    public Hilbert() {
        world = new World(new Dimension(800, 600), "Hilbert Curve", Color.WHITE);
        t = world.createTurtle();

        String path = world.getRealPath(this, "./resources");
        world.setBgImage(path + "/Grid 800x600.png");
    }

    public void run() {
        t.setShape("turtle");
        t.setPenUp();
        t.setVisible(true);
        t.setShapeScale(1, 1);
        t.setShapeColor(Color.BLUE);
        t.setHeading(0);
        t.setPosition(0, 0);
        t.setSpeed(8);

        t.setPenColor(Color.RED);
        t.setPenSize(5);

        t.left(135);
        t.forward(240);
        t.right(135);
        t.setPenDown();
        hilbert(4, 90, 20);
        t.setPenUp();

        t.setPosition(-110, -220);
        t.setPenColor(Color.BLACK);
        t.write("Presione 'ESC' para finalizar", "Arial", 20, Font.PLAIN);

        t.setPosition(0, -250);
        t.setHeading(90);

        world.waitForKey(KeyEvent.VK_ESCAPE);
        world.bye();
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

    public static void main(String[] args) {
        Hilbert test = new Hilbert();
        test.run();
    }

}
