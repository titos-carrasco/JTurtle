package rcr.turtle;

import java.awt.Polygon;

/**
 * Forma de tortuga triangular
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class TriangleShape extends TurtleShape {

    /**
     * Constructor
     */
    public TriangleShape() {
        super();

        shape = new Polygon();
        shape.addPoint(4, 4);
        shape.addPoint(15, 10);
        shape.addPoint(4, 16);
        shape.addPoint(4, 4);
    }

}
