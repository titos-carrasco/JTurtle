package rcr.turtle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Clase base para todas las formas de tortuga
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class TurtleShape {
    private static int SIZE = 21;
    private static int SIZE_2 = SIZE / 2;

    BufferedImage surface;
    Polygon shape;

    TurtleShape() {
        shape = new Polygon();
        surface = World.createTranslucentImage(SIZE, SIZE);
    }

    BufferedImage getShape(double heading, int shapeScale, Color shapeColor) {
        if (shapeScale <= 0)
            shapeScale = 1;

        // trabajamos sobre una copia
        Polygon poly = new Polygon(shape.xpoints, shape.ypoints, shape.npoints);

        // rotamos respecto al centro
        TurtleShape.rotate(poly, heading);

        // escalamos
        for (int i = 0; i < poly.npoints; i++) {
            poly.xpoints[i] = poly.xpoints[i] * shapeScale;
            poly.ypoints[i] = poly.ypoints[i] * shapeScale;
        }

        // si la dimension es diferente creamos una nueva superficie
        int width = SIZE * shapeScale;
        int height = SIZE * shapeScale;
        if (width != surface.getWidth())
            surface = World.createTranslucentImage(width, height);

        // limpiamos la superficie
        Graphics2D g2d = surface.createGraphics();
        g2d.setBackground(new Color(255, 255, 255, 0));
        g2d.clearRect(0, 0, width, height);

        // trazamos la forma de la tortuga
        g2d.setColor(shapeColor);
        g2d.drawPolygon(poly);
        g2d.fillPolygon(poly);

        g2d.dispose();

        return surface;
    }

    static void rotate(Polygon poly, double heading) {
        double angle = Math.toRadians(heading);

        // rotamos cada punto del poligono
        for (int i = 0; i < poly.npoints; i++) {
            Point2D.Double p = new Point2D.Double(poly.xpoints[i], poly.ypoints[i]);
            p = rotate(p, angle);
            poly.xpoints[i] = (int) Math.round(p.x);
            poly.ypoints[i] = (int) Math.round(p.y);
        }
    }

    static Point2D.Double rotate(Point2D.Double p, double angle) {
        angle = -angle;

        double x = SIZE_2 + (p.x - SIZE_2) * Math.cos(-angle) - (p.y - SIZE_2) * Math.sin(angle);
        double y = SIZE_2 + (p.x - SIZE_2) * Math.sin(angle) + (p.y - SIZE_2) * Math.cos(angle);

        return new Point2D.Double(x, y);

    }
}
