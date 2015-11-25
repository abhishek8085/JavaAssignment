/**
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : ZeroOne.java, 2015/10/26
 */
public class ZeroOne extends Thread {
    private String info;
    static Object o = new Object();
    /*becomes true when all threads are created. */
    static boolean threadsCreated = false;
    static int counter = 1;

    public ZeroOne(String info) {
        this.info = info;
    }

    public void run() {
        while (true) {
            synchronized (o) {
                /*checks if the flag threadsCreated is true or not and modify
                the counter accordingly. */
                if (threadsCreated) {
                    counter++;
                    if (counter == 98) {
                        counter = -1;
                    }
                }
                o.notifyAll();
                System.out.print(" " + info + " ");

                try {
                    if (!threadsCreated) {
                        /* Creating 99 threads */
                        Thread current = new ZeroOne((counter++) + "");

                        if (counter > 98) {
                            threadsCreated = true;
                            counter = -2;
                        }
                        current.start();
                    }
                    sleep(100);
                    /*Thread checks themselves goes to wait if they
                    are not suppose to wake up.*/
                    while (!((counter + 1) + "").equals(info)) {
                        o.wait();
                    }

                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String args[]) {
        new ZeroOne("0").start();
    }
}


