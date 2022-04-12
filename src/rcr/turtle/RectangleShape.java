package rcr.turtle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author roberto
 *
 */
public class RectangleShape implements TurtleShape {
    static private final int SIZE = 10;

    BufferedImage surface;
    int size;

    /**
     * Constructor por defecto
     */
    public RectangleShape() {
        size = SIZE * 1;
        surface = World.createOpaqueImage(size, size);
    }

    /**
     *
     */
    @Override
    public BufferedImage getShape(double heading, int shapeScale, Color shapeColor) {
        if (shapeScale <= 0)
            shapeScale = 1;

        int s = SIZE * shapeScale;
        if (size != s) {
            size = s;
            surface = World.createOpaqueImage(size, size);
        }

        Graphics2D g2d = surface.createGraphics();
        g2d.setColor(shapeColor);
        g2d.fillRect(0, 0, size, size);
        g2d.dispose();

        return surface;
    }

}
