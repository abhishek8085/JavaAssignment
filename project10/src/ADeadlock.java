/**
 * Deadlock code
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : ADeadlock.java, 2015/11/2
 */
public class ADeadlock {

    private static Object mutex1 = new Object();
    private static Object mutex2 = new Object();

    public static void main(String args[]) {
        //start thread1
        new Thread1(mutex1, mutex2).start();
    }
}

/**
 * Deadlock code
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Thread1.java, 2015/11/2
 */
class Thread1 extends Thread {
    private static Object mutex1;
    private static Object mutex2;


    public Thread1(Object mutex1, Object mutex2) {
        this.mutex1 = mutex1;
        this.mutex2 = mutex2;
    }

    @Override
    public void run() {

        synchronized (mutex1) {
            System.out.println("Thread 1 got mutex1");

            Thread2 thread2 = new Thread2(mutex1, mutex2);

            System.out.println("Starting Thread 2");
            thread2.start();
            thread2.isRunning();

            System.out.println("Thread 1 waiting for mutex2");
            synchronized (mutex2) {

            }
        }
    }
}

/**
 * Deadlock code
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Thread2.java, 2015/11/2
 */
class Thread2 extends Thread {
    private static Object mutex1;
    private static Object mutex2;

    private static Object mutex3 = new Object();
    boolean isRunning = false;

    public Thread2(Object mutex1, Object mutex2) {
        this.mutex1 = mutex1;
        this.mutex2 = mutex2;
    }

    public boolean isRunning() {
        synchronized (mutex3) {
            if (!isRunning) {
                try {
                    mutex3.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        System.out.println("Thread 2 started.");
        synchronized (mutex2) {

            synchronized (mutex3) {
                isRunning = true;
                mutex3.notify();
            }

            System.out.println("Thread 2 got mutex2");
            System.out.println("Thread 2 waiting for mutex1");
            synchronized (mutex1) {

            }
        }
    }
}