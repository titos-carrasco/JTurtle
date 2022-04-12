package rcr.turtle;

/**
 * Forma de tortuga rectangular
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class RectangleShape extends TurtleShape {

    /**
     * Constructor
     */
    public RectangleShape() {
        super();

        shape.addPoint(4, 4);
        shape.addPoint(16, 4);
        shape.addPoint(16, 16);
        shape.addPoint(4, 16);
        shape.addPoint(4, 4);
    }
}
