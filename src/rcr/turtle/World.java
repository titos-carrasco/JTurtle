package rcr.turtle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Clase que representa el mundo de tortugas
 *
 * @author Roberto Carrasco (titos.carrasco@gmail.com)
 *
 */
public class World extends JPanel implements KeyListener, MouseListener, WindowListener {
    private static final long serialVersionUID = -1056589405033868183L;

    private boolean running;
    private ArrayList<Turtle> turtles;
    private BufferedImage background;
    private BufferedImage bgImage;
    private Color bgColor;
    private BufferedImage screen;
    private JFrame win;

    /**
     * Constructor del mundo de tortugas
     *
     * @param winSize Dimension de la ventana (pantalla)
     * @param title   Titulo de la ventana
     * @param bgColor Color de fondo de a ventana
     */
    public World(Dimension winSize, String title, Color bgColor) {

        running = false;
        turtles = new ArrayList<Turtle>();
        bgImage = null;
        this.bgColor = bgColor;

        background = World.createOpaqueImage(winSize.width, winSize.height);
        Graphics2D g2d = background.createGraphics();
        g2d.setBackground(bgColor);
        g2d.clearRect(0, 0, background.getWidth(), background.getHeight());

        screen = World.createTranslucentImage(winSize.width, winSize.height);
        g2d = screen.createGraphics();
        g2d.setBackground(new Color(255, 255, 255, 0));
        g2d.clearRect(0, 0, screen.getWidth(), screen.getHeight());
        g2d.dispose();

        addKeyListener(this);
        addMouseListener(this);
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

        repaint();
    }

    /**
     * Loop principal del mundo de tortugas.
     *
     * @param fps FPS (frame per seconds) deseados
     */
    public void mainLoop(int fps) {
        running = true;
        long tickExpected = (long) (1000.0 / fps);
        long tickPrev = System.currentTimeMillis();
        while (running) {
            // --- tiempo en ms desde el ciclo anterior
            long tickElapsed = System.currentTimeMillis() - tickPrev;
            if (tickElapsed < tickExpected)
                try {
                    Thread.sleep(tickExpected - tickElapsed);
                } catch (InterruptedException e) {
                }

            long now = System.currentTimeMillis();
            // double dt = (now - tickPrev) / 1000.0;
            tickPrev = now;

            // --- render
            repaint();
        }

        // eso es todo
        win.dispose();
    }

    void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = background.createGraphics();
        g2d.setBackground(bgColor);
        g2d.clearRect(0, 0, background.getWidth(), background.getHeight());
        if (bgImage != null)
            g2d.drawImage(bgImage, 0, 0, null);
        g2d.dispose();
        g.drawImage(background, 0, 0, null);

        g.drawImage(screen, 0, 0, null);
        for (Turtle turtle : turtles) {
            BufferedImage shape = turtle.getShape();
            if (shape == null)
                continue;

            Point2D.Double p = toScreenCoordinates(turtle.getX(), turtle.getY());
            p.x -= shape.getWidth() / 2;
            p.y -= shape.getHeight() / 2;

            g.drawImage(shape, (int) p.x, (int) p.y, null);
        }
    }

    /**
     * Marca el mundo para ser finalizado en el siguiente 1/fps
     */
    public void bye() {
        running = false;
    }

    /**
     * Cambia el fondo del mundoo de tortugas
     *
     * @param color El color del fondo
     */
    public void setBgColor(Color color) {
        bgColor = color;
    }

    /**
     * Establece una imagen de fondo para el mundo de tortugas
     *
     * @param fname El nombre del archivo de imagen a utilizar
     */
    public void setBgImage(String fname) {
        bgImage = fname == null ? null : readImage(fname);
        repaint();
    }

    /**
     * Limpia todo lo que las tortugas han trazado en la ventana
     */
    public void clearScreen() {
        Graphics2D g2d = screen.createGraphics();
        g2d.setBackground(new Color(255, 255, 255, 0));
        g2d.clearRect(0, 0, screen.getWidth(), screen.getHeight());
        g2d.dispose();
        repaint();
    }

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

    /**
     * No implementada
     */
    public void onKeyPressed() {
    }

    /**
     * No implementada
     */
    public void onMouseClick() {
    }

    /**
     * No implementada
     */
    public void onTimer() {
    }

    // --------------------------------------------------------------------

    /**
     * @return
     */
    BufferedImage getScreen() {
        return screen;
    }

    /**
     * @param p2d
     * @return
     */
    Point2D.Double toScreenCoordinates(Point2D.Double p2d) {
        return toScreenCoordinates(p2d.x, p2d.y);
    }

    /**
     * @param x
     * @param y
     * @return
     */
    Point2D.Double toScreenCoordinates(double x, double y) {
        double x0 = screen.getWidth() / 2;
        double y0 = screen.getHeight() / 2;

        x = x0 + x;
        y = y0 + y;
        y = screen.getHeight() - y;

        return new Point2D.Double(x, y);
    }

    /**
     * @param width
     * @param height
     * @return
     */
    static BufferedImage createOpaqueImage(int width, int height) {
        GraphicsConfiguration gconfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        return gconfig.createCompatibleImage(width, height, Transparency.OPAQUE);
    }

    /**
     * @param width
     * @param height
     * @return
     */
    static BufferedImage createTranslucentImage(int width, int height) {
        GraphicsConfiguration gconfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        return gconfig.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    /**
     * @param fname
     * @return
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

    // --------------------------------------------------------------------

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        running = false;
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

    // ------ mouse ------
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // ------ keyboard ------
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
