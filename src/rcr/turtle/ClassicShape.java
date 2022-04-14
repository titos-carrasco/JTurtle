package rcr.turtle;

/**
 * Forma clasica de tortuga (tomada de turtle python=
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class ClassicShape extends PolygonShape {

    /**
     * Constructor
     */
    public ClassicShape() {
        super();

        path.moveTo(0, 0);
        path.lineTo(-5, -9);
        path.lineTo(0, -7);
        path.lineTo(5, -9);
        path.closePath();
    }
}
