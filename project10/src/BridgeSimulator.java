import java.util.Random;
import java.util.Vector;

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : BridgeSimulator.java, 2015/10/11
 */
public class BridgeSimulator {

    public static void main(String args[]) {
        Bridge bridge = new Bridge();
        final BridgeTrafficControl bridgeTrafficControl = new BridgeTrafficControl(bridge);

        TruckThreadProvider.initialise(bridgeTrafficControl);

        for (int i = 0; i < 4; i++) {
            TruckThreadProvider.getTruckThread().start();
        }
    }
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 ,2015/10/11
 */
class Bridge implements Path, DriveListener {

    public static final int MAX_WEIGHT = 200000;
    public static final int MAX_TRUCKS = 4;

    private Vector<Vehicle> vehicleOnBridge = new Vector<Vehicle>();
    private volatile int weightOnTheBridge = 0;
    private BridgeExitListener bridgeExitListener;

    public BridgeExitListener getBridgeExitListener() {
        return bridgeExitListener;
    }

    public void setBridgeExitListener(BridgeExitListener bridgeExitListener) {
        this.bridgeExitListener = bridgeExitListener;
    }

    public void enter(Vehicle vehicle) {
        vehicleOnBridge.add(vehicle);
        weightOnTheBridge += vehicle.getWeight();

    }

    public void drive(Vehicle vehicle) {
        vehicle.drive(this, this);
    }

    public void exit(Vehicle truck) {
        vehicleOnBridge.remove(truck);
        weightOnTheBridge -= truck.getWeight();
        bridgeExitListener.exited(truck);
    }

    public int getNumberOfTrucksOnTheBridge() {
        return vehicleOnBridge.size();
    }

    public int getWeightOnTheBridge() {
        return weightOnTheBridge;
    }

    @Override
    public synchronized void driveStarted(Vehicle vehicle) {

    }

    @Override
    public synchronized void driveDone(Vehicle vehicle) {
        exit(vehicle);
    }
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : , 2015/10/11
 */
class BridgeTrafficControl implements BridgeExitListener {
    private Bridge bridge;
    private Vector<Vehicle> waitQueue = new Vector<Vehicle>();

    public BridgeTrafficControl(Bridge bridge) {
        this.bridge = bridge;
        this.bridge.setBridgeExitListener(this);
    }

    public void requestEntry(Vehicle vehicle) {
        waitQueue.add(vehicle);
        synchronized (this) {
            while (bridge.getWeightOnTheBridge() + vehicle.getWeight() > Bridge.MAX_WEIGHT
                    || bridge.getNumberOfTrucksOnTheBridge() + 1 > Bridge.MAX_TRUCKS) {
                try {
                    this.wait();
                    if (vehicle == waitQueue.get(0)) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            bridge.enter(vehicle);
        }
        bridge.drive(vehicle);
    }

    @Override
    public void exited(Vehicle vehicle) {
        System.out.println("Truck " + vehicle.id + " existed bridge");
        waitQueue.remove(vehicle);
        synchronized (this) {
            this.notifyAll();
        }
    }
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : , 2015/10/11
 */
interface DriveListener {
    public void driveStarted(Vehicle vehicle);

    public void driveDone(Vehicle vehicle);
}
/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : , 2015/10/11
 */
interface BridgeExitListener {
    public void exited(Vehicle vehicle);
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 :  2015/10/11
 */
interface Path {
    public void enter(Vehicle vehicle);

    public void exit(Vehicle vehicle);
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : BridgeSimulator.java, 2015/10/11
 */
interface Drivable {
    public void drive(DriveListener driveListener, Path path);
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 :  2015/10/11
 */
abstract class Vehicle implements Drivable {
    protected String id;
    protected int weight;

    public Vehicle(String id, int weight) {
        this.id = id;
        this.weight = weight;
    }


    public void drive(DriveListener driveListener, Path path) {
        driveListener.driveStarted(this);
        drive(path);
        driveListener.driveDone(this);
    }


    protected abstract void drive(Path path);

    public int getWeight() {
        return weight;
    }
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : 2015/10/11
 */
class Truck extends Vehicle {

    public Truck(String id, int weight) {
        super(id, weight);
    }

    @Override
    protected void drive(Path path) {

        System.out.println("Driving on bridge truck: " + super.id + " with weight " + super.weight + "lbs");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 :  2015/10/11
 */
class TruckThreadProvider {
    private static BridgeTrafficControl internalBridgeTrafficControl;

    public static void initialise(BridgeTrafficControl bridgeTrafficControl) {
        internalBridgeTrafficControl = bridgeTrafficControl;
    }

    public static Thread getTruckThread() {
        return new Thread() {
            @Override
            public void run() {
                internalBridgeTrafficControl.requestEntry(TruckProvider.getTruck());
            }
        };
    }
}

/**
 * This program simulates crossing of the bridge
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 :  2015/10/11
 */
class TruckProvider {
    private static Random random = new Random();
    private static int i = 0;

    public static Truck getTruck() {
        return new Truck(getI() + "", (random.nextInt(100000 - 100) + 100));
    }

    private static synchronized int getI() {
        return i++;
    }
}