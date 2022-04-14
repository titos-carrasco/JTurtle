package rcr.turtle;

/**
 * Forma rectangular de tortuga (tomada de turtle python=
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class SquareShape extends PolygonShape {

    /**
     * Constructor
     */
    public SquareShape() {
        super();

        path.moveTo(-10, 10);
        path.lineTo(10, 10);
        path.lineTo(10, -10);
        path.lineTo(-10, -10);
        path.closePath();
    }
}
