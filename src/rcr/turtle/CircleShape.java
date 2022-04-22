package rcr.turtle;

/**
 * Forma tortuga (tomada de turtle python)
 *
 * @author Roberto Carrasco (titos.carrasco@gmail-com)
 *
 */
public class CircleShape extends PolygonShape {

    /**
     * Constructor
     */
    public CircleShape() {
        super();

        path.moveTo(10, 0);
        path.lineTo(9.51, 3.09);
        path.lineTo(8.09, 5.88);
        path.lineTo(5.88, 8.09);
        path.lineTo(3.09, 9.51);
        path.lineTo(0, 10);
        path.lineTo(-3.09, 9.51);
        path.lineTo(-5.88, 8.09);
        path.lineTo(-8.09, 5.88);
        path.lineTo(-9.51, 3.09);
        path.lineTo(-10, 0);
        path.lineTo(-9.51, -3.09);
        path.lineTo(-8.09, -5.88);
        path.lineTo(-5.88, -8.09);
        path.lineTo(-3.09, -9.51);
        path.lineTo(-0.00, -10.00);
        path.lineTo(3.09, -9.51);
        path.lineTo(5.88, -8.09);
        path.lineTo(8.09, -5.88);
        path.lineTo(9.51, -3.09);
    }
}
