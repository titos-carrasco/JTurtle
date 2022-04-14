package rcr.turtle;

/**
 * Forma de tortuga clasica
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class ClassicShape extends PolygonShape {

    ClassicShape() {
        super();

        path.moveTo(0, 0);
        path.lineTo(-5, -9);
        path.lineTo(0, -7);
        path.lineTo(5, -9);
        path.closePath();
    }
}
