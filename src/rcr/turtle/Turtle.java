package rcr.turtle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Una tortuga dentro del mundo de JTurtle
 *
 * En cada moviento la tortuga deja un trazo dependiendo si su lapiz se
 * encuentra habilitado o no
 *
 * La tortuga se encuentra en un plano cartesiano con la posicion (0,0) al
 * centro de la pantalla y los giros son realizados en sentido contrario al
 * reloj (los 0 grados se encuentran hacia la derecha en el exe X)
 *
 * @author Roberto Carrasco (titos.carrasco@gmail.com)
 *
 */
public class Turtle {
    private World world;
    private Point2D.Double position;
    private double heading = 0;
    private boolean visible = true;
    private int speed = 10;

    private boolean penDown = false;
    private float penSize = 1;
    private Color penColor = Color.BLACK;

    private Path2D.Double fillShape;
    private Color fillColor = Color.WHITE;
    private boolean filling = false;

    private double shapeScaleX = 1;
    private double shapeScaleY = 1;
    private Color shapeColor = Color.BLACK;;
    private PolygonShape shape;

    private static HashMap<String, Class<? extends PolygonShape>> shapes;

    static {
        shapes = new HashMap<String, Class<? extends PolygonShape>>();
        registerShape("square", SquareShape.class);
        registerShape("triangle", TriangleShape.class);
        registerShape("classic", ClassicShape.class);
        registerShape("circle", CircleShape.class);
        registerShape("turtle", TurtleShape.class);
    }

    /**
     * Crea una tortuga en las coordenadas (0,0) orientada en cero grados
     *
     * @param world El mundo al cual pertenece
     */
    Turtle(World world) {
        this.world = world;
        position = new Point2D.Double(0, 0);
        setShape("classic");
        setPosition(0, 0);
    }

    // --- Movimiento y trazados ---

    /**
     * Posiciona la tortuga en las coordenadas especificadas
     *
     * @param x La coordenada X para la nueva posicion
     * @param y La coordenada Y para la nueva posicion
     */
    public void setPosition(double x, double y) {
        if (penDown) {
            BufferedImage screen = world.getScreen();
            Point2D.Double p1 = world.toScreenCoordinates(position.x, position.y);
            Point2D.Double p2 = world.toScreenCoordinates(x, y);
            Graphics2D g2d = screen.createGraphics();
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(penSize));
            g2d.setColor(penColor);
            g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            g2d.setStroke(oldStroke);
            g2d.dispose();

            position.x = x;
            position.y = y;

            if (speed != 0)
                world.delay(200 - speed * 20 + 16);
        } else {
            position.x = x;
            position.y = y;
        }

        if (filling)
            fillShape.lineTo(position.x, position.y);
    }

    /**
     * Posiciona la tortuga en la coordenada X especificada conservando la
     * coordenada Y
     *
     * @param x La coordenada X para la nueva posicion
     */
    public void setX(double x) {
        setPosition(x, position.y);
    }

    /**
     * Posiciona la tortuga en la coordenada Y especificada conservando la
     * coordenada X
     *
     * @param y La coordenada Y para la nueva posicion
     */
    public void setY(double y) {
        setPosition(position.x, y);
    }

    /**
     * Posiciona la tortuga en las coordenadas (0,0) orientada en cero grados
     */
    public void goHome() {
        setPosition(0, 0);
        setHeading(0);
    }

    /**
     * Desplaza la tortuga hacia adelante la distancia especificada. El movimiento
     * se realiza segun el angulo de rotacion que tenga en ese momento
     *
     * @param distancia La distancia a desplazar
     */
    public void forward(double distancia) {
        double rad = Math.toRadians(heading);
        double x = distancia * Math.cos(rad) + position.x;
        double y = distancia * Math.sin(rad) + position.y;
        setPosition(x, y);
    }

    /**
     * Desplaza la tortuga hacia atras la distancia especificada. El movimiento se
     * realiza segun el angulo de rotacion que tenga en ese momento
     *
     * @param distancia La distancia a desplazar
     */
    public void backward(double distancia) {
        forward(-distancia);
    }

    /**
     * Traza un circulo circunscrito a un poligono de N pasos
     *
     * @param radius El radio
     * @param extent El angulo del arco. Utilizar 360 para un circulo completo
     * @param steps  Numero de pasos a utilizar. Utilizar 0 para la mayor cantidad
     *               de pasos
     */
    public void circle(double radius, double extent, double steps) {
        if (extent > 360 || extent < -360)
            extent = extent % 360;
        if (extent < 0)
            extent = 360 + extent;

        if (steps <= 0) {
            double frac = Math.abs(extent) / 360;
            steps = 1 + (int) (Math.min(11 + Math.abs(radius) / 6.0, 59.0) * frac);
        }

        double w = 1.0 * extent / steps;
        double w2 = 0.5 * w;
        double l = 2.0 * radius * Math.sin(Math.toRadians(w2) * 1);
        if (radius < 0) {
            w = -w;
            w2 = -w2;
            l = -l;
        }

        left(w2);
        for (double i = 0; i < steps; i++) {
            forward(l);
            left(w);
        }
        left(-w2);
    }

    /**
     * Traza un punto de radio dado
     *
     * @param diameter El diametro del punto a trazar
     * @param color    El color del punto a trazar
     */
    public void dot(int diameter, Color color) {
        BufferedImage screen = world.getScreen();
        Point2D.Double p1 = world.toScreenCoordinates(position.x, position.y);
        Ellipse2D.Double dot = new Ellipse2D.Double(p1.x - diameter / 2, p1.y - diameter / 2, diameter, diameter);
        Graphics2D g2d = screen.createGraphics();
        g2d.setColor(color);
        g2d.fill(dot);
        g2d.dispose();
    }

    /**
     * Da inicio a que todas las operaciones de trazado de linea se inscriban en un
     * poligono que sera rellenado con el color de relleno
     */
    public void beginFill() {
        fillShape = new Path2D.Double();
        fillShape.moveTo(position.x, position.y);
        filling = true;
    }

    /**
     * Establece el fin de las operaciones de relleno de las operaciones de trazado
     * de linea y realiza el relleno del poligono
     */
    public void endFill() {
        filling = false;

        BufferedImage screen = world.getScreen();
        Graphics2D g2d = screen.createGraphics();

        // a coordenadas de pantalla
        Polygon poly = new Polygon();
        PathIterator pi = fillShape.getPathIterator(null);
        while (!pi.isDone()) {
            double[] xy = new double[2];
            pi.currentSegment(xy);
            Point2D.Double p = world.toScreenCoordinates(xy[0], xy[1]);
            poly.addPoint((int) (p.x), (int) (p.y));
            pi.next();
        }

        // rellenamos el poligono y volvenos a trazar su contorno
        g2d.setColor(fillColor);
        g2d.fill(poly);
        g2d.setColor(penColor);
        g2d.draw(poly);

        g2d.dispose();
    }

    // --- Giros ---

    /**
     * Gira la tortuga hacia la derecha desde su angulo de rotacion actual
     *
     * @param angle El angulo a rotar
     */
    public void right(double angle) {
        setHeading(heading - angle);
    }

    /**
     * Gira la tortuga hacia la izquierda desde su angulo de rotacion actual
     *
     * @param angle El angulo a rotar
     */
    public void left(double angle) {
        setHeading(heading + angle);
    }

    /**
     * Establece el angulo de rotacion
     *
     * @param angle El angulo de rotacion
     */
    public void setHeading(double angle) {
        if (angle > 360 || angle < -360)
            angle = angle % 360;
        if (angle < 0)
            angle = 360 + angle;
        heading = angle;
    }

    // --- Estado de la tortuga ---

    /**
     * Retorna la posicion
     *
     * @return La posicion
     */
    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y);
    }

    /**
     * Retorna la coordenada X actual de su posicion
     *
     * @return La coordenada X
     */
    public double getX() {
        return position.x;
    }

    /**
     * Retorna la coordenada Y actual de su posicion
     *
     * @return La coordenada Y
     */
    public double getY() {
        return position.y;
    }

    /**
     * Retorna el angulo de rotacion actual
     *
     * @return El angulo de rotacion
     */
    public double getHeading() {
        return heading;
    }

    /**
     * Retorna la distancia desde la posicion actual hacia las coordenada dadas
     *
     * @param x La coordenada X del punto a medir
     * @param y La coordenada Y del punto a medir
     *
     * @return La distancia entre ambas posiciones
     */
    public double getDistance(double x, double y) {
        return position.distance(x, y);
    }

    /**
     * Establece la velocidad de la tortuga entre cada trazo que dibuje:
     *
     * 0: No se realizan pausas entre cada trazo 1: velocidad mas baja 10: velocidad
     * mas alta
     *
     * @param speed La velocidad
     */
    public void setSpeed(int speed) {
        if (speed < 0)
            this.speed = 0;
        else if (speed > 10)
            this.speed = 10;
        else
            this.speed = speed;
    }

    /**
     * Establece la visibilidad de la tortuga
     *
     * @param visible Si es verdadero la tortuga se hace visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Retorna la visibilidad de la tortuga
     *
     * @return Verdadero si se encuentra visible
     */
    public boolean isvisible() {
        return visible;
    }

    /**
     * Retorna la velocidad entre trazos a dibujar
     *
     * @return La velocidad
     */
    public int getSpeed() {
        return speed;
    }

    // --- Lapiz ---

    /**
     * Activa el lapiz. Desde este momento todos los desplazamientos generan un
     * trazo en su mundo
     */
    public void setPenDown() {
        penDown = true;
    }

    /**
     * Seactiva el lapiz. Desde este momento todos los desplazamientos no generan un
     * trazo en su mundo
     */
    public void setPenUp() {
        penDown = false;
    }

    /**
     * Establece el grosor del lapiz
     *
     * @param size El grosor
     */
    public void setPenSize(int size) {
        penSize = size;
    }

    /**
     * Establece el color para el lapiz. Los trazos se dibujaran con este color
     *
     * @param color El color del lapiz
     */
    public void setPenColor(Color color) {
        penColor = color;
    }

    /**
     * Establece el color de relleno para cuando est?? habilitado
     *
     * @param color El color de relleno
     */
    public void setFillColor(Color color) {
        fillColor = color;
    }

    /**
     * establece simultaneamente el color del lapiz y el color de relleno
     *
     * @param penColor  Color para el lapiz
     * @param fillColor Color para el relleno
     */
    public void setColor(Color penColor, Color fillColor) {
        this.penColor = penColor;
        this.fillColor = fillColor;
    }

    /**
     * Retorna el estado de activacion del lapiz
     *
     * @return Verdadero si el lapiz se encuentra activo
     */
    public boolean isPenDown() {
        return penDown;
    }

    // -- texto --

    public void write(String text, String fontName, int fontSize, int fontStyle) {
        Font font = new Font(fontName, fontStyle, fontSize);

        BufferedImage screen = world.getScreen();
        Graphics2D g2d = screen.createGraphics();
        Point2D.Double p = world.toScreenCoordinates(position.x, position.y);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(penColor);
        g2d.setFont(font);
        g2d.drawString(text, (int) p.x, (int) p.y);
        g2d.dispose();
    }

    // -- Formas de tortuga ---

    /**
     * Establece la forma para esta tortuga
     *
     * @param name El nombre de la forma de tortuga a utilizar
     */
    public void setShape(String name) {
        shape = createShape(name);
    }

    /**
     * Retorna las formas de tortuga actualmente disponibles
     *
     * @return Las formas disponibles
     */
    public String[] getShapesNames() {

        String[] names = shapes.keySet().toArray(new String[shapes.size()]);
        return names;
    }

    /**
     * Establece el factor de amplificacion de la forma de esta tortuga
     *
     * @param scaleX El factor en X
     * @param scaleY El factor en Y
     */
    public void setShapeScale(double scaleX, double scaleY) {
        this.shapeScaleX = scaleX <= 0 ? 1 : scaleX;
        this.shapeScaleY = scaleY <= 0 ? 1 : scaleY;
    }

    /**
     * Establece el color de esta forma de tortuga
     *
     * @param color El color para la forma
     */
    public void setShapeColor(Color color) {
        shapeColor = color;
    }

    /**
     * Registra una nueva forma de tortuga.
     *
     * La clase que define la forma debe extender a PolygonShape y utilizar el
     * objeto 'path' (Path2D.Double) de dicha clase
     *
     * <pre>
     * public class SquareShape extends PolygonShape {
     *  public SquareShape() {
     *      super();
     *
     *      path.moveTo(-10, 10);
     *      path.lineTo(10, 10);
     *      path.lineTo(10, -10);
     *      path.lineTo(-10, -10);
     *  }
     * }
     * </pre>
     *
     * @param name       El nombre a asignar
     * @param shapeClass La clase que la define
     */
    public static void registerShape(String name, Class<? extends PolygonShape> shapeClass) {
        shapes.put(name, shapeClass);
    }

    /**
     * Crea una instancia de la forma de tortuga especificada
     *
     * @param name El nombre de la forma a utilizar
     *
     * @return La instancia de la clase que define esta forma
     */
    static PolygonShape createShape(String name) {
        Class<? extends PolygonShape> polygonShapeClass = shapes.get(name);
        PolygonShape polygonShape = null;

        try {
            polygonShape = polygonShapeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return polygonShape;
    }

    /**
     * Traza la forma de tortuga en la superficie dad
     *
     * @param g2d La superficie en la cual trazar la forma
     */
    void drawShape(Graphics2D g2d) {
        if (!visible)
            return;

        // escalamos, rotamos y trasladamos
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(-90 + heading));
        at.scale(shapeScaleX, shapeScaleY);
        Path2D.Double cpath = (Path2D.Double) at.createTransformedShape(shape.path);

        // a coordenadas de pantalla
        Polygon poly = new Polygon();
        PathIterator pi = cpath.getPathIterator(null);
        while (!pi.isDone()) {
            double[] xy = new double[2];
            pi.currentSegment(xy);
            Point2D.Double p = world.toScreenCoordinates(xy[0], xy[1]);
            poly.addPoint((int) (position.x + p.x), (int) (p.y - position.y));
            pi.next();
        }

        // trazamos la forma de la tortuga
        g2d.setColor(shapeColor);
        g2d.draw(poly);
        g2d.fill(poly);
    }

}
