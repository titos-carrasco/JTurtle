package rcr.turtle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

/**
 * @author roberto
 *
 */
public class TriangleShape implements TurtleShape {
    static private final int SIZE = 10;

    BufferedImage surface;
    Dimension size;
    Polygon shape;

    /**
     * Constructor por defecto
     */
    public TriangleShape() {
        size = new Dimension(SIZE, SIZE);
        surface = World.createTranslucentImage(size.width, size.height);
        shape = new Polygon();
        shape.addPoint(3, 1);
        shape.addPoint(3, 8);
        shape.addPoint(6, 5);
        shape.addPoint(6, 4);
        shape.addPoint(3, 1);
    }

    /**
     *
     */
    @Override
    public BufferedImage getShape(double heading, int shapeScale, Color shapeColor) {
        if (shapeScale <= 0)
            shapeScale = 1;

        int w = SIZE * shapeScale;
        int h = SIZE * shapeScale;
        if (w != size.width && h != size.height) {
            size = new Dimension(w, h);
            surface = World.createTranslucentImage(w, h);
        }

        Graphics2D g2d = surface.createGraphics();
        g2d.setBackground(new Color(255, 255, 255, 0));
        g2d.clearRect(0, 0, size.width, size.height);

        Polygon poly = new Polygon(shape.xpoints, shape.ypoints, shape.npoints);
        for (int i = 0; i < poly.npoints; i++) {
            poly.xpoints[i] = poly.xpoints[i] * shapeScale;
            poly.ypoints[i] = poly.ypoints[i] * shapeScale;
        }
        TurtleShape.rotate(poly, heading, size);

        g2d.setColor(shapeColor);
        g2d.drawPolygon(poly);
        g2d.fillPolygon(poly);

        g2d.dispose();

        return surface;
    }

}
