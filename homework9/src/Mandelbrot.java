// original from: http://rosettacode.org/wiki/Mandelbrot_set#Java
// modified for color

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {

    public static final int MAX = 5000;
    private final int LENGTH = 800;
    private final double ZOOM = 1000;
    private BufferedImage theImage;
    public static int[] colors = new int[MAX];
    int availableProcessor = Runtime.getRuntime().availableProcessors();

    private ExecutorService<MandelbrotThread> threadExecutorService = new FixedExecutorService<MandelbrotThread>(availableProcessor);

    public Mandelbrot() {
        super("Mandelbrot Set");
        threadExecutorService.start();
        initColors();
        setBounds(100, 100, LENGTH, LENGTH);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void createSet() {
        theImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        double zx, zy, cX, cY;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                zx = zy = 0;
                cX = (x - LENGTH) / ZOOM;
                cY = (y - LENGTH) / ZOOM;

                threadExecutorService.submit(new MandelbrotThread(x, y, zx, zy, cX, cY, theImage));
            }

        }
        repaint();

        while (!threadExecutorService.isAllTaskCompleted()) ;
        threadExecutorService.stop();
    }

    public void initColors() {
        for (int index = 0; index < MAX; index++) {
            colors[index] = Color.HSBtoRGB(index / 256f, 1, index / (index + 8f));
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(theImage, 0, 0, this);
    }

    public static void main(String[] args) {
        Mandelbrot aMandelbrot = new Mandelbrot();
        aMandelbrot.setVisible(true);
        aMandelbrot.createSet();

    }
}
