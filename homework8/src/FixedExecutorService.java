
import java.util.Vector;

/**
 * Listener which executor invoke
 * after task completion
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : FixedExecutorService.java, 2015/10/16
 */
public class FixedExecutorService<T extends Runnable> implements
        ExecutorService<T>, ExecutorStore {

    private int maxExecutors;
    private ThreadPool<Executor> tThreadPool = new ThreadPool<Executor>();
    private Vector<T> tasks = new Vector<T>();
    private final Object mutex = new Object();
    private SchedulerThread schedulerThread = new SchedulerThread();
    private boolean stopFlag;

    public FixedExecutorService(int maxExecutors) {
        this.maxExecutors = maxExecutors;
    }


    @Override
    public void start() {
        schedulerThread.start();
    }


    /**
     * SchedulerThread
     *
     * @author Abhishek Indukumar
     * 
     * @version 1.0 : SchedulerThread.java, 2015/10/16
     */
    private class SchedulerThread extends Thread {
        @Override
        public void run() {

            for (int i = 0; i < maxExecutors; i++) {
                Executor executor = new Executor("Executor " + i,
                        FixedExecutorService.this);
                executor.start();
                tThreadPool.put(executor);
            }

            synchronized (mutex) {
                while (!stopFlag) {
                    while (!stopFlag && tasks.isEmpty()) {
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                        }
                    }

                    if (stopFlag) {
                        System.out.println("Shutting down Task scheduler.");
                    } else {
                        tThreadPool.get().setTask(tasks.remove(0));
                    }

                }
            }

            while (!isAllTaskCompleted()) ;

            while (stopFlag && !tThreadPool.isEmpty()) {
                tThreadPool.get().stopExecutor();
            }
        }
    }

    @Override
    public void submit(T task) {
        synchronized (mutex) {
            tasks.add(task);
            mutex.notify();
        }
    }

    @Override
    public void stop() {
        stopFlag = true;
        schedulerThread.interrupt();
    }

    @Override
    public boolean isAllTaskCompleted() {
        return tasks.isEmpty() && tThreadPool.getItemsInPool() == maxExecutors;
    }


    @Override
    public void put(Executor executor) {
        tThreadPool.put(executor);
    }
}

/**
 * Executor Thread
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Executor.java, 2015/10/16
 */
class Executor extends Thread {
    private Runnable task = null;
    private ExecutorStore executorStore;
    private final Object mutex = new Object();
    private boolean stopFlag;

    public Executor(String name, ExecutorStore executorStore) {
        super(name);
        this.executorStore = executorStore;
    }

    /**
     * Sets task for executor
     *
     * @param task task
     */
    public void setTask(Runnable task) {
        synchronized (mutex) {
            this.task = task;
            mutex.notifyAll();
        }
    }

    @Override
    public void run() {
        while (!stopFlag) {
            synchronized (mutex) {
                while (task == null && !stopFlag) {
                    try {
                        mutex.wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (stopFlag) {
                    System.out.println("Shutting down " + getName());
                    return;
                }
                try {
                    task.run();
                } finally {
                    task = null;
                    executorStore.put(this);
                }
            }
        }
    }

    /**
     * called to stop executor
     */
    public void stopExecutor() {
        stopFlag = true;
        this.interrupt();
    }
}

/**
 * Listener which executor invoke
 * after task completion
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : ExecutorStore.java, 2015/10/16
 */
interface ExecutorStore {
    void put(Executor executor);
}

/**
 * All Executors should implement this interface.
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : ExecutorService.java, 2015/10/16
 */
interface ExecutorService<T extends Runnable> {

    /**
     * Should start executor service.
     */
    void start();

    /**
     * Submit task for executor service.
     *
     * @param thread task
     */
    void submit(T thread);

    /**
     * Should stop executor service after all
     * executors are completed.
     */
    void stop();

    /**
     * Return true if all the tasks are completed.
     *
     * @return true if all the tasks are completed.
     */
    boolean isAllTaskCompleted();
}

/**
 * Thread Pool
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : ThreadPool.java, 2015/10/16
 */
class ThreadPool<T extends Runnable> implements Pool<T> {
    private Vector<T> threads = new Vector<T>();
    private final Object mutex = new Object();

    @Override
    public void put(T item) {
        threads.add(item);
        synchronized (mutex) {
            mutex.notifyAll();
        }
    }

    @Override
    public T get() {
        synchronized (mutex) {
            while (threads.isEmpty()) {
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return threads.remove(0);
        }
    }

    @Override
    public boolean isEmpty() {
        return threads.isEmpty();
    }

    @Override
    public int getItemsInPool() {
        return threads.size();
    }
}

/**
 * All pools should implement this interface.
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Pool.java, 2015/10/16
 */
interface Pool<T> {
    /**
     * Puts the item in the pool.
     *
     * @param item item
     */
    void put(T item);

    /**
     * Gets the item from the pool
     *
     * @return item from the pool
     */
    T get();

    /**
     * Return true if pool is empty
     * else false
     *
     * @return Return true if pool is empty
     * else false
     */
    boolean isEmpty();

    /**
     * Return number of items in the pool
     *
     * @return number of items in the pool
     */
    int getItemsInPool();
}