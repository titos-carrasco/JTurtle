package rcr.turtle;

/**
 * Forma triangular de tortuga (tomada de turtle python=
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class TriangleShape extends PolygonShape {

    /**
     * Constructor
     */
    public TriangleShape() {
        super();
        path.moveTo(10, -5);
        path.lineTo(0, 11);
        path.lineTo(-10, -5);
        path.closePath();
    }
}
