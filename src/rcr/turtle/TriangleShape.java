package rcr.turtle;

/**
 * Forma de tortuga triangular
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class TriangleShape extends PolygonShape {

    TriangleShape() {
        super();
        path.moveTo(10, -5);
        path.lineTo(0, 11);
        path.lineTo(-10, -5);
        path.closePath();
    }
}
