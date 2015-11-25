import java.awt.image.BufferedImage;

/**
 *MandelbrotThread
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : MandelbrotThread.java, 2015/10/16
 */
public class MandelbrotThread implements Runnable {
    private int x;
    private int y;

    private double zx;
    private double zy;
    private double cX;
    private double cY;
    private BufferedImage theImage;

    public MandelbrotThread(int x, int y, double zx, double zy, double cX, double cY, BufferedImage bufferedImage) {
        this.x = x;
        this.y = y;
        this.zx = zx;
        this.zy = zy;
        this.cX = cX;
        this.cY = cY;
        this.theImage = bufferedImage;
    }


    @Override
    public void run() {
        int iter = 0;
        double tmp;
        while ((zx * zx + zy * zy < 10) && (iter < Mandelbrot.MAX - 1)) {    // this is the part for the parallel part
            tmp = zx * zx - zy * zy + cX;                // this is the part for the parallel part
            zy = 2.0 * zx * zy + cY;                    // this is the part for the parallel part
            zx = tmp;                            // this is the part for the parallel part
            iter++;                            // this is the part for the parallel part
        }                                // this is the part for the parallel part
        if (iter > 0)                            // this is the part for the parallel part
            theImage.setRGB(x, y, Mandelbrot.colors[iter]);            // this is the part for the parallel part
        else                                // this is the part for the parallel part
            theImage.setRGB(x, y, iter | (iter << 8));
    }
}