package rcr.turtle;

import java.awt.geom.Path2D;

/**
 * Clase base para todas las formas de tortuga basadas en poligonos
 *
 * @author Roberto Carrasco (titos.carrasco@gmail.com)
 *
 */
public class PolygonShape {
    Path2D.Double path;

    /**
     * Constructor
     */
    public PolygonShape() {
        path = new Path2D.Double();
    }

}
