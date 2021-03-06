package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

// tomado de https://compucademy.net/python-turtle-graphics-demos/
public class YingYang {
    World world;
    Turtle t;

    public YingYang() {
        world = new World(new Dimension(800, 600), "Ying Yang", Color.WHITE);
        t = world.createTurtle();

        String path = world.getRealPath(this, "./resources");
        world.setBgImage(path + "/Grid 800x600.png");
    }

    public void yin(double radius, Color color1, Color color2) {
        t.setPenSize(3);
        t.setColor(Color.BLACK, color1);
        t.beginFill();
        t.circle(radius / 2., 180, 0);
        t.circle(radius, 180, 0);
        t.left(180);
        t.circle(-radius / 2., 180, 0);
        t.endFill();
        t.left(90);
        t.setPenUp();
        t.forward(radius * 0.35);
        t.right(90);
        t.setPenDown();
        t.setColor(color1, color2);
        t.beginFill();
        t.circle(radius * 0.15, 360, 0);
        t.endFill();
        t.left(90);
        t.setPenUp();
        t.backward(radius * 0.35);
        t.setPenDown();
        t.left(90);
    }

    public void run() {
        yin(200, Color.BLACK, Color.WHITE);
        yin(200, Color.WHITE, Color.BLACK);
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
        YingYang test = new YingYang();
        test.run();
    }
}
