/**
 *SieveOfEratosthenesThread
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : MandelbrotThread.java, 2015/10/16
 */
public class SieveOfEratosthenesThread implements Runnable{

    private boolean numbers[];
    private int index;
    private int MAX;

    public SieveOfEratosthenesThread(boolean[] numbers, int index, int MAX) {
        this.numbers = numbers;
        this.index = index;
        this.MAX = MAX;
    }

    @Override
    public void run() {
        if ( numbers[index] )	{				// this is the part for the parallel part
            int counter = 2;				// this is the part for the parallel part
            while ( index * counter < MAX )	{		// this is the part for the parallel part
                numbers[index * counter] = false;	// this is the part for the parallel part
                counter++;				// this is the part for the parallel part
            }						// this is the part for the parallel part
        }

    }
}
