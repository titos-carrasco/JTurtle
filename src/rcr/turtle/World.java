package rcr.turtle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Un mundo de tortugas en un sistema cartesiano al estilo Turtle Python
 *
 * @author Roberto Carrasco (titos.carrasco@gmail.com)
 *
 */
public class World extends JPanel implements KeyListener, WindowListener, ActionListener {
    private static final long serialVersionUID = -1056589405033868183L;

    private ArrayList<Turtle> turtles;
    private BufferedImage background;
    private BufferedImage bgImage = null;
    private BufferedImage screen;
    private BufferedImage shapes;
    private JFrame win;
    private Color bgColor;

    private HashMap<Integer, Integer> keysPressed;
    private long tickPrev = 0;

    private Timer repaintTimer;

    /**
     * Constructor del mundo de tortugas
     *
     * @param winSize Dimension de la ventana (pantalla)
     * @param title   Titulo de la ventana
     * @param bgColor Color de fondo de a ventana
     */
    public World(Dimension winSize, String title, Color bgColor) {
        turtles = new ArrayList<Turtle>();
        this.bgColor = bgColor;
        keysPressed = new HashMap<Integer, Integer>();

        background = World.createOpaqueImage(winSize.width, winSize.height);
        shapes = World.createTranslucentImage(winSize.width, winSize.height);
        screen = World.createTranslucentImage(winSize.width, winSize.height);
        clearScreen();

        addKeyListener(this);
        setFocusable(true);
        setSize(winSize);
        setPreferredSize(winSize);

        win = new JFrame();
        win.addWindowListener(this);
        win.setTitle(title);
        win.add(this);
        win.pack();
        win.setResizable(false);
        win.setVisible(true);

        repaintTimer = new Timer(1000 / 60, this);
        repaintTimer.start();
    }

    // --- Tortugas ---

    /**
     * Crea una tortuga dentro de este mundo
     *
     * @return La tortuga creada
     */
    public Turtle createTurtle() {
        Turtle turtle = new Turtle(this);
        turtles.add(turtle);
        return turtle;
    }

    // --- Entorno grafico ---

    /**
     * Cambia el color de fondo
     *
     * @param color El color del fondo
     */
    public void setBgColor(Color color) {
        bgColor = color;
    }

    /**
     * Establece una imagen de fondo
     *
     * @param fname El nombre del archivo de imagen a utilizar
     */
    public void setBgImage(String fname) {
        bgImage = fname == null ? null : readImage(fname);
    }

    /**
     * Limpia todo lo que las tortugas han trazado en la ventana
     */
    public void clearScreen() {
        Graphics2D g2d = screen.createGraphics();
        g2d.setBackground(new Color(0, 0, 0, 0));
        g2d.clearRect(0, 0, screen.getWidth(), screen.getHeight());
        g2d.dispose();
    }

    /**
     * Cierra el entorno grafico de este mundo
     */
    public void bye() {
        repaintTimer.stop();
        win.dispose();
    }

    /**
     * Retorna la superficie en donde las tortugas pueden dibujar
     *
     * @return La superficie en donde dibujar
     */
    BufferedImage getScreen() {
        return screen;
    }

    /**
     * Convierte una coordenada desde el sistema cartesiano al sistema de pantalla
     * (origen en la parte superior izqueirda)
     *
     * @param p2d Las coordenadas en el sistema cartesiano
     *
     * @return las coordenadas en el sistema de pantalla
     */
    Point2D.Double toScreenCoordinates(Point2D.Double p2d) {
        return toScreenCoordinates(p2d.x, p2d.y);
    }

    /**
     * Convierte una coordenada desde el sistema cartesiano al sistema de pantalla
     * (origen en la parte superior izqueirda)
     *
     * @param x La coordenada X en el sistema cartesiano
     * @param y La coordenada Y en el sistema cartesiano
     *
     * @return las coordenadas en el sistema de pantalla
     */
    Point2D.Double toScreenCoordinates(double x, double y) {
        double x0 = screen.getWidth() / 2;
        double y0 = screen.getHeight() / 2;

        x = x0 + x;
        y = y0 + y;
        y = screen.getHeight() - y;

        return new Point2D.Double(x, y);
    }

    // --- Teclado ---

    /**
     * Detecta si una tecla se encuentra presionada
     *
     * @param keyCode La tecla a detectar (java.awt.event.KeyEvent)
     *
     * @return Verdadero si la tecla se encuentra presionada
     */
    public boolean isKeyPressed(int keyCode) {
        int keyPressed = keysPressed.getOrDefault(keyCode, 0);
        return keyPressed == 1;
    }

    /**
     * Espera hasta que la tecla especificada es presionada
     *
     * @param keyCode La tecla por la cual esperar
     */
    public void waitForKey(int keyCode) {
        while (!isKeyPressed(keyCode)) {
            delay(16);
        }
    }

    // --- Utils ---

    /**
     * Hace una pausa en la ejecucion
     *
     * @param ms Los milisegundos de pausa a realizar
     */
    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Hace una pausa para completar 1/fps (frames per seconds)
     *
     * @param fps Los FPS esperados
     *
     * @return El tiempo, en segundos, transcurridos desde la ultima invocacion
     */
    public double tick(int fps) {
        if (tickPrev == 0)
            tickPrev = System.currentTimeMillis();

        long tickExpected = (long) (1000.0 / fps);
        long tickElapsed = System.currentTimeMillis() - tickPrev;
        if (tickElapsed < tickExpected)
            try {
                Thread.sleep(tickExpected - tickElapsed);
            } catch (InterruptedException e) {
            }

        long now = System.currentTimeMillis();
        double dt = (now - tickPrev) / 1000.0;
        tickPrev = now;

        return dt;
    }

    /**
     * Crea una imagen sin soporte de transparencia
     *
     * @param width  El ancho de a imagen
     * @param height El alto de la imagen
     * @return La imagen creada
     */
    static BufferedImage createOpaqueImage(int width, int height) {
        GraphicsConfiguration gconfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        return gconfig.createCompatibleImage(width, height, Transparency.OPAQUE);
    }

    /**
     * Crea una imagen con soporte de transparencia
     *
     * @param width  El ancho de a imagen
     * @param height El alto de la imagen
     * @return La imagen creada
     */
    static BufferedImage createTranslucentImage(int width, int height) {
        GraphicsConfiguration gconfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        return gconfig.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    /**
     * Construye una imagen a partir de un archivo
     *
     * @param fname El archivo con la imagen
     *
     * @return La imagen construida
     */
    static BufferedImage readImage(String fname) {
        File f = new File(fname);
        BufferedImage img = null;
        try {
            img = ImageIO.read(f);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return img;
    }

    // --- Eventos ---

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d;

        g2d = background.createGraphics();
        g2d.setBackground(bgColor);
        g2d.clearRect(0, 0, background.getWidth(), background.getHeight());
        if (bgImage != null)
            g2d.drawImage(bgImage, 0, 0, null);
        g2d.dispose();

        g2d = shapes.createGraphics();
        g2d.setBackground(new Color(0, 0, 0, 0));
        g2d.clearRect(0, 0, shapes.getWidth(), shapes.getHeight());
        for (Turtle turtle : turtles)
            turtle.drawShape(g2d);
        g2d.dispose();

        g.drawImage(background, 0, 0, null);
        g.drawImage(screen, 0, 0, null);
        g.drawImage(shapes, 0, 0, null);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(1);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.put(e.getKeyCode(), 1);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.put(e.getKeyCode(), 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
