package rcr.turtle;

/**
 * Forma de tortuga rectangular
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class SquareShape extends PolygonShape {

    SquareShape() {
        super();

        path.moveTo(-10, 10);
        path.lineTo(10, 10);
        path.lineTo(10, -10);
        path.lineTo(-10, -10);
        path.closePath();
    }
}
