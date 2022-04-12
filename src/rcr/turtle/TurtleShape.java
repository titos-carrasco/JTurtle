package rcr.turtle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * @author roberto
 *
 */
public interface TurtleShape {
    BufferedImage getShape(double heading, int shapeScale, Color shapeColor);

    /**
     * @param poly
     * @param heading
     * @param size
     */
    static void rotate(Polygon poly, double heading, Dimension size) {
        double angle = Math.toRadians(heading);
        for (int i = 0; i < poly.npoints; i++) {
            Point2D.Double p = new Point2D.Double(poly.xpoints[i], poly.ypoints[i]);
            p = rotate(p, angle, size);
            poly.xpoints[i] = (int) Math.round(p.x);
            poly.ypoints[i] = (int) Math.round(p.y);
        }
    }

    /**
     * @param p
     * @param angle
     * @param size
     * @return
     */
    static Point2D.Double rotate(Point2D.Double p, double angle, Dimension size) {
        // ajustamos al cuadrante IV
        double px = p.x;
        double py = -p.y;

        // trasladamos
        double cx = size.width / 2.0;
        px = px - cx;

        double cy = -size.height / 2.0;
        py = py - cy;

        // la hipotenusa
        double h = Math.hypot(px, py);

        // el angulo actual de este punto
        double a = Math.abs(Math.asin(py / h));
        if (px < 0)
            if (py < 0)
                a = Math.toRadians(180) + a;
            else
                a = Math.toRadians(180) - a;
        else if (py < 0)
            angle = Math.toRadians(360) - a;

        // el nuevo angulo
        angle = angle + a;

        // nuevas coordenadas
        px = Math.cos(angle) * h;
        py = Math.sin(angle) * h;

        // traslados a su origen
        px = px + cx;
        py = py + cy;

        // cuadrante I invertido
        return new Point2D.Double(px, -py);
    }
}
