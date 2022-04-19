package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import rcr.turtle.Turtle;
import rcr.turtle.World;

public class LanzamientoBala {
    World world;
    Turtle t;

    public LanzamientoBala() {
        world = new World(new Dimension(800, 600), "Lanzamiento Bala", Color.WHITE);
        t = world.createTurtle();

        String path = world.getRealPath(this, "./resources");
        world.setBgImage(path + "/Grid 800x600.png");
    }

    public void run() {
        t.setVisible(false);
        t.setSpeed(0);

        t.setPosition(-400, -100);
        t.setPenDown();
        t.setPosition(400, -100);

        t.setPenUp();
        t.setPosition(-400, -100);
        t.setColor(Color.BLACK, Color.BLACK);
        t.beginFill();
        t.setPosition(-350, -100);
        t.setPosition(-350, -80);
        t.setPosition(-400, -80);
        t.setPosition(-400, 100);
        t.endFill();

        t.setPenUp();
        t.setPosition(-375, -75);
        t.setColor(Color.BLACK, Color.BLACK);
        t.dot(30, Color.BLACK);

        t.setShape("triangle");
        t.setShapeColor(Color.BLACK);
        // t.setShapeScale(1.8, 0.2);
        t.setPosition(-365, -70);
        t.setHeading(10);
        t.setVisible(true);

        Turtle bala = world.createTurtle();
        bala.setVisible(false);
        bala.setPenUp();

        double tiempo = 0;
        boolean disparando = false;
        boolean running = true;
        while (running) {
            double dt = world.tick(60);

            if (world.isKeyPressed(KeyEvent.VK_ESCAPE))
                running = false;
            else if (!disparando) {
                if (world.isKeyPressed(KeyEvent.VK_UP)) {
                    double angulo = t.getHeading();
                    if (angulo < 55)
                        t.setHeading(angulo + 1);
                } else if (world.isKeyPressed(KeyEvent.VK_DOWN)) {
                    double angulo = t.getHeading();
                    if (angulo > 10)
                        t.setHeading(angulo - 1);
                } else if (world.isKeyPressed(KeyEvent.VK_SPACE)) {
                    disparando = true;
                    tiempo = 0;
                }
            } else {
                double angulo = Math.toRadians(t.getHeading());
                double velocidad = 80;
                double altura = t.getY();
                double g = 9.81;

                double x = t.getX() - 10 + velocidad * Math.cos(angulo) * tiempo;
                double y = altura + velocidad * Math.sin(angulo) * tiempo - (1.0 / 2.0) * (g) * (tiempo * tiempo);
                if (y < -100)
                    disparando = false;
                else {
                    bala.setPosition(x + 10, y);
                    bala.dot(2, Color.RED);
                    tiempo = tiempo + 0.1;
                }
            }

        }

        world.bye();
    }

    public static void main(String[] args) {
        LanzamientoBala test = new LanzamientoBala();
        test.run();
    }

}
