package rcr.turtle;

/**
 * Forma tortuga (tomada de turtle python)
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class TurtleShape extends PolygonShape {

    /**
     * Constructor
     */
    public TurtleShape() {
        super();

        path.moveTo(0, 16);
        path.lineTo(-2, 14);
        path.lineTo(-1, 10);
        path.lineTo(-4, 7);
        path.lineTo(-7, 9);
        path.lineTo(-9, 8);
        path.lineTo(-6, 5);
        path.lineTo(-7, 1);
        path.lineTo(-5, -3);
        path.lineTo(-8, -6);
        path.lineTo(-6, -8);
        path.lineTo(-4, -5);
        path.lineTo(0, -7);
        path.lineTo(4, -5);
        path.lineTo(6, -8);
        path.lineTo(8, -6);
        path.lineTo(5, -3);
        path.lineTo(7, 1);
        path.lineTo(6, 5);
        path.lineTo(9, 8);
        path.lineTo(7, 9);
        path.lineTo(4, 7);
        path.lineTo(1, 10);
        path.lineTo(2, 14);
    }
}
