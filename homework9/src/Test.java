import java.util.Vector;

/**
 * Created by Abhishek8085 on 28-10-2015.
 */
public class Test {


}


class A{

    private static Object mutex = new Object();
    private static boolean toWake=false;

    void methodA()
    {
        System.out.println(Thread.currentThread().getName()+"waiting to enter");
        synchronized (mutex) {
            synchronized (mutex) {
                System.out.println(Thread.currentThread().getName() + "in method a");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!toWake) {
                    try {
                        mutex.wait();
                        System.out.println("waiting");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    String methodB()
    {
        try
        {
            throw  new NullPointerException("a");

        }
        catch ( Exception e)
        {
            return "exception";
        }
        finally {
            //return "finally";
        }
    }
}


class Th
{
    public static void main(String args[])
    {
       final A a = new A();

        Thread tha = new Thread("Thread1"){
            @Override
            public void run() {
                a.methodA();
            }
        };

        Thread thb = new Thread("Thread2"){
            @Override
            public void run() {
                a.methodB();
            }
        };

        //tha.start();
        //thb.start();

        System.out.println(a.methodB());
    }
}






 class Thread_5c extends Thread    {
    private String info;
    static Vector aVector;

    public Thread_5c ( Vector aVector, String info) {
        this.info = info;
        System.out.println(this.aVector==aVector);
        this.aVector = aVector;
        System.out.println(this.aVector==aVector);
    }

    public void inProtected () {
        System.out.println("locking on "+aVector.get(0));
        synchronized ( aVector )     {
            System.out.println(info + ": is in protected()");
           try {

                sleep(10000);
            }
            catch (  InterruptedException e ) {
                System.err.println("Interrupted!");
            }
            System.out.println(info + ": exit run");
        }
    }

    public void run () {
        inProtected();
    }

    public static void main (String args []) {
        Vector aVector = new Vector();
        aVector.add(1);
        Thread_5c aT5_0 = new Thread_5c(aVector, "first");
        aT5_0.start();

        aVector = new Vector();
        aVector.add(2);
        Thread_5c aT5_1 = new Thread_5c(aVector, "second");
        aT5_1.start();

        aVector = new Vector();
        aVector.add(3);
        Thread_5c aT5_2 = new Thread_5c(aVector, "third");
        aT5_2.start();
    }
}



//File D.java
/*
 * This code will always end up in a dead lock.
 */
 class D extends Thread	{
    private String info;
    Object o_1;
    Object o_2;
    Object stop;

    public D (String info, Object o_1, Object o_2, Object stop) {
        this.info    	= info;
        this.o_1    	= o_1;
        this.o_2    	= o_2;
        this.stop    	= stop;
    }
    public void run () {
        synchronized ( o_1 ) {
            System.out.println(info);
            try {
                synchronized ( stop ) {
                    if ( info == "0")	{
                        new D("1", o_2, o_1, stop).start(); // order of args
                        stop.wait();
                    } else
                        synchronized ( stop ) {
                            stop.notify();
                        }
                }
                synchronized ( o_2 ) {
                    System.out.println("I will not get there");
                }
            } catch ( Exception e ) { }
        }
    }
    public static void main (String args []) {
        Object o_1 = new Object();
        Object o_2 = new Object();
        Object stop = new Object();
        new D("0", o_1, o_2, stop).start();
    }
}








//File DeadLock.java
/*
 * Will end up in a dead lock
 *
 *
 */
 class DeadLock extends Thread	{
    private String info;
    Object o_1;
    Object o_2;
    Object stop;
    static boolean oneIsRunning = false; // is static important?
    // es wird nur ein
    // Objekt erzeugt
    public DeadLock (String info, Object o_1, Object o_2, Object stop) {
        this.info    = info;
        this.o_1    = o_1;
        this.o_2    = o_2;
        this.stop    = stop;
    }
    public void one () {
        synchronized ( o_1 ) {
            System.out.println(info);
            try {
                synchronized ( stop ) {
                    if ( ! oneIsRunning )	{
                        new DeadLock("1", o_1, o_2, stop).start();
                        oneIsRunning = true;
                    }
                    stop.wait();
                }
                synchronized ( o_2 ) {
                    System.out.println("I will not get there");
                }
            } catch ( Exception e ) { }
        }
    }
    public void two () {
        synchronized ( o_2 ) {
            System.out.println(info);
            try {
                synchronized ( stop ) {
                    stop.notify();
                }
                synchronized ( o_1 ) {
                    System.out.println("I will not get there");
                }
            } catch ( Exception e ) { }
        }
    }
    public void run () {
        if ( info.equals("0") )
            one();
        else
            two();
    }
    public static void main (String args []) {
        Object o_1 = new Object();
        Object o_2 = new Object();
        Object stop = new Object();
        new DeadLock("0", o_1, o_2, stop).start();
    }
}









 class M extends Thread    {
    private String info;
    private Vector aVector;

    public M (String info) {
        this.info    = info;
    }
    private synchronized void inProtected () {
        System.err.println(info + ": is in protected()");
        try {
            sleep(1000);
        }
        catch (  InterruptedException e ) {
            System.err.println("Interrupted!");
        }
        System.err.println(info + ": exit run");
    }
    public void run () {
        inProtected();
    }
    public static void main (String args []) {
        Vector aVector = new Vector();
        M aT4_0 = new M("first");
        M at5_0 = new M("second");

        aT4_0.start();
        at5_0.start();
        aT4_0.inProtected();
        at5_0.inProtected();
    }
}

class ClassT extends Thread    {
    private String info;
    private Vector aVector;

    public ClassT (String info, Vector aVector) {
        this.info    = info;
        this.aVector = aVector;
    }

    public static synchronized void staticInProtected1(String s) {
        System.err.println(s + ": ---->");
        try {
            sleep(10000);
        }
        catch (  InterruptedException e ) {
            System.err.println("Interrupted!");
        }

        System.err.println(s + ": <----");
    }

    public static synchronized void staticInProtected2(String s) {
        System.err.println(s + ": ====>");
        try {
            sleep(1000);
        }
        catch (  InterruptedException e ) {
            System.err.println("Interrupted!");
        }
        System.err.println(s + ": ====>");
    }



    public static void main (String args []) {
/*        Vector aVector = new Vector();
        ClassT aClassT_0 = new ClassT("first",  aVector);
        ClassT aClassT_1 = new ClassT("second", aVector);

        ClassT.staticInProtected1("main");
        aClassT_0.start();
        aClassT_1.start();
        aClassT_0.staticInProtected1("aClassT_0");
        aClassT_1.staticInProtected1("aClassT_1");*/

        Thread t = new Thread(){
            @Override
            public void run() {
                ClassT.staticInProtected1("t1");
            }
        };

        Thread t2 = new Thread(){
            @Override
            public void run() {
                ClassT.staticInProtected2("t2");
            }
        };

        t.start();
        t2.start();
    }
}







 class T_6 extends Thread    {
    static Object o = new Object();
    static int    counter = 0;

    public void run () {
        if ( counter++ == 1 )
            o = new Object();

        synchronized ( o ) {
            System.err.println("--->" );
            try {
                sleep(1000);
            }
            catch (  InterruptedException e ) {
                System.err.println("Interrupted!");
            }
            System.err.println("<---" );
        }
    }

    public static void main (String args []) {
        new T_6().start();
        new T_6().start();
        new T_6().start();
    }
}







//File X.java
/*
 * is this output	1 0 1 0 1 ...
 *			...
 * the only possible output?
 *
 * Falsch: es ist nichtgarantiert, in welcher die
 * Threads eintreten.
 */
 class X extends Thread	{
    private String info;
    static Object o = new Object();
    public X (String info) {
        this.info    = info;
    }
    public void run () {
        while ( true )	{
            synchronized ( o ) {
                System.out.println(info);
                try {
                    o.notify();
                    sleep(100);
                    o.wait(1);
                } catch ( Exception e ) { }
            }
        }
    }
    public static void main (String args []) {
        ( new X("0") ).start();
        ( new X("1") ).start();
    }
}



// What is the exceution order?

 class ExceptionsAndInheritance1 {

    public void importantFunction() throws InterruptedException {
        System.out.println("ExceptionsAndInheritance1:importantFunction -->");
        throw new InterruptedException("ExceptionsAndInheritance1.java");
    }

    public static void main(String[] args) {
        try {
            new ExceptionsAndInheritance1().importantFunction();
        } catch (Exception e)	{
            System.out.println("Main ");
            e.printStackTrace();
        }
    }
}



// What is the execution order?

 class ExceptionsAndInheritance2 extends
        ExceptionsAndInheritance1 {

    //  private void importantFunction() {
    public void importantFunction() {
        System.out.println("ExceptionsAndInheritance2:importantFunction -->");
    }

    public static void main(String[] args) {
        ExceptionsAndInheritance2 e2 = new ExceptionsAndInheritance2();
        ExceptionsAndInheritance1 e1 = (ExceptionsAndInheritance2)e2;
        e2.importantFunction();
       try {
            e1.importantFunction();
        } catch (Exception e)	{
            System.out.println("Main ");
            e.printStackTrace();
        }
    }
}









