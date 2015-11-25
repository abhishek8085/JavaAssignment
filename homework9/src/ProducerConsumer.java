import java.util.Arrays;

/**
 *a multi threaded solution to the producer/consumer problem
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : ProducerConsumer.java, 2015/10/25
 */
public class ProducerConsumer {

    public static final int CANDY = 1;
    public static final int WRAPPER = 2;


    public static void main(String[] args) {

        CandyStore candyStore = new CandyStore(5);
        candyStore.startProducing();

        WrappingPaperStore wrappingPaperStore = new WrappingPaperStore(4);
        wrappingPaperStore.startProducing();

        @SuppressWarnings("unchecked")
        CandyWrapperAndStore candyWrapperAndStore = new CandyWrapperAndStore(candyStore, wrappingPaperStore, 12);
        candyWrapperAndStore.startWrappingTheCandies();

        CandyBoxStore candyBoxStore = new CandyBoxStore(10);
        candyBoxStore.startProducingCandyBoxes();

        @SuppressWarnings("unchecked")
        CandyBoxerAndStore candyBoxerAndStore = new CandyBoxerAndStore(12, candyWrapperAndStore, candyBoxStore);
        candyBoxerAndStore.startProducingBoxedCandy();


        for (int i = 0; i < 6; i++) {
            System.out.println(candyBoxerAndStore.getItem());
        }
    }


}

/**
 * produces one piece of candy at a time (candyProducer) and stores the candy
 * in a fixed length storage area (length=k)
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : CandyStore.java, 2015/10/25
 */
class CandyStore implements ProduceListener<Candy>, Store<Candy> {

    private int numberOfCandyToProduce;
    private  Candy[] candies;
    private int candyCounter = 0;

    public CandyStore(int numberOfCandyToProduce) {
        this.numberOfCandyToProduce = numberOfCandyToProduce;
        this.candies = new Candy[numberOfCandyToProduce];
    }

    public void startProducing() {
        new CandyProducer(this).start();
    }

    @Override
    public void produced(Candy item) {
        synchronized (this) {
            candies[candyCounter++] = item;
            this.notify();
        }
    }

    @Override
    public boolean isAvailable(int quantity) {
        synchronized (this) {
            while (candies.length < quantity || candies[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public Candy getItem() {
        synchronized (this) {
            while (candies.length == 0 || candies[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Candy returnCandy = candies[0];
            candies = Arrays.copyOfRange(candies, 1, candies.length);
            candyCounter--;
            return returnCandy;
        }
    }

    private class CandyProducer extends Thread {
        private ProduceListener<Candy> produceListener;
        private int candyCounter = 0;

        public CandyProducer(ProduceListener<Candy> produceListener) {
            super("CandyProducer");
            this.produceListener = produceListener;
        }

        @Override
        public void run() {
            while (candyCounter < numberOfCandyToProduce) {
                produceListener.produced(new Candy("Candy " + candyCounter++));
            }
        }
    }
}

/**
 * produces one piece of 3 pieces candy wrapping paper at a time
 * (candyWrappingPaperProducer) and stores the wrapping paper in 3 different
 * locations in a fixed length storage area ((length=l).
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : WrappingPaperStore.java, 2015/10/25
 */
class WrappingPaperStore implements ProduceListener<Wrapper>, Store<Wrapper> {
    private int numberOfWrappingPaperToProduce;
    private Wrapper[] wrappers;
    private int wrapperCounter = 0;

    public WrappingPaperStore(int numberOfWrappingPaperToProduce) {
        this.numberOfWrappingPaperToProduce = numberOfWrappingPaperToProduce *3;
        this.wrappers = new Wrapper[numberOfWrappingPaperToProduce * 3];
    }

    public void startProducing() {
        new WrappingPaperProducer(this).start();
    }

    @Override
    public void produced(Wrapper item) {
        synchronized (this) {
            wrappers[wrapperCounter++] = item;
            this.notify();
        }
    }

    @Override
    public boolean isAvailable(int quantity) {
        synchronized (this) {
            while (wrappers.length < quantity || wrappers[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public Wrapper getItem() {
        synchronized (this) {
            while (wrappers.length == 0 || wrappers[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Wrapper returnWrapper = wrappers[0];
            wrappers = Arrays.copyOfRange(wrappers, 1, wrappers.length);
            wrapperCounter--;
            return returnWrapper;
        }

    }

    class WrappingPaperProducer extends Thread {
        private ProduceListener<Wrapper> produceListener;
        private int wrapperCounter = 0;

        public WrappingPaperProducer(ProduceListener<Wrapper> produceListener) {
            super("WrappingPaperProducer");
            this.produceListener = produceListener;
        }

        @Override
        public void run() {
            while (wrapperCounter < numberOfWrappingPaperToProduce) {
                produceListener.produced(new Wrapper());
                produceListener.produced(new Wrapper());
                produceListener.produced(new Wrapper());
                wrapperCounter += 3;
            }
        }
    }


}

/**
 * consumes one piece of candy and one piece of wrapping paper and wraps the
 * candy in wrapping paper, if a candy and wrapping paper exists, and stores
 * the wrapped candy in a fixed length storage area ((length=m, m > 4)
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : CandyWrapperAndStore.java, 2015/10/25
 */
class CandyWrapperAndStore<T extends CandyStore, V extends WrappingPaperStore> implements ProduceListener<Wrapper>, Store<Wrapper> {
    private T candyStore;
    private V wrappingPaperStore;
    private int numberCandiesToWrap;
    private int wrappedStoreCounter = 0;
    private Wrapper[] wrappers;


    public CandyWrapperAndStore(T candyStore, V wrappingPaperStore, int numberCandiesToWrap) {
        this.candyStore = candyStore;
        this.wrappingPaperStore = wrappingPaperStore;
        this.numberCandiesToWrap = numberCandiesToWrap;
        if (numberCandiesToWrap < 4) {
            throw new IllegalArgumentException("Number Candies To Wrap should be grate than 4");
        }
        this.wrappers = new Wrapper[numberCandiesToWrap];
    }

    public void startWrappingTheCandies() {
        new CandyWrapperThread(this).start();
    }

    @Override
    public void produced(Wrapper item) {
        synchronized (this) {
            wrappers[wrappedStoreCounter++] = item;
            this.notify();
        }
    }

    public boolean isEmpty()
    {
        return wrappers.length==0 || wrappers[0]==null;
    }

    @Override
    public boolean isAvailable(int quantity) {
        synchronized (this) {
            while (wrappers.length < quantity || wrappers[0] == null || wrappers[quantity-1]==null ) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public Wrapper getItem() {
        synchronized (this) {
            while (wrappers.length == 0 || wrappers[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Wrapper returnWrapper = wrappers[0];
            wrappers = Arrays.copyOfRange(wrappers, 1, wrappers.length);
            wrappedStoreCounter--;
            return returnWrapper;
        }
    }

    private class CandyWrapperThread extends Thread {
        private ProduceListener<Wrapper> produceListener;
        private int wrappedCounter;

        public CandyWrapperThread(ProduceListener<Wrapper> produceListener) {
            super("CandyWrapper");
            this.produceListener = produceListener;
        }

        @Override
        public void run() {
            while (wrappedCounter++ < numberCandiesToWrap) {

                //System.out.println("Trying to get 1 candy");
                if (candyStore.isAvailable(1))
                {
                    //System.out.println("Got 1 candy");
                }

                //System.out.println("Trying to get 1 piece of wrapping paper");
                if ( wrappingPaperStore.isAvailable(1) )
                {
                    //System.out.println("Got 1 piece of wrapping paper.");
                }

                //System.out.println("Wrapping candy.....");

                Wrapper wrapper = wrappingPaperStore.getItem();
                wrapper.setCandy(candyStore.getItem());
                produceListener.produced(wrapper);
            }
        }
    }
}

/**
 *  creates candy boxes, and stores the boxes in a fixed length storage area
 *  (length=m). A box can hold at most 4 pieces.
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : CandyBoxStore.java, 2015/10/25
 */
class CandyBoxStore implements ProduceListener<CandyBox>, Store<CandyBox> {
    private int numberOfBoxToProduce;
    private CandyBox[] candyBoxes;
    private int candyBoxStoreCounter = 0;

    public CandyBoxStore(int numberOfBoxToProduce) {
        this.numberOfBoxToProduce = numberOfBoxToProduce;
        this.candyBoxes = new CandyBox[numberOfBoxToProduce];
    }

    public void startProducingCandyBoxes() {
        new CandyBoxThread(this).start();
    }

    @Override
    public void produced(CandyBox item) {
        synchronized (this) {
            candyBoxes[candyBoxStoreCounter++] = item;
            this.notify();
        }
    }

    @Override
    public boolean isAvailable(int quantity) {
        synchronized (this) {
            while (candyBoxes.length < quantity || candyBoxes[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public CandyBox getItem() {
        synchronized (this) {
            while (candyBoxes.length == 0 || candyBoxes[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            CandyBox candyBox = candyBoxes[0];
            candyBoxes = Arrays.copyOfRange(candyBoxes, 1, candyBoxes.length);
            candyBoxStoreCounter--;
            return candyBox;
        }
    }

    private class CandyBoxThread extends Thread {
        private ProduceListener<CandyBox> produceListener;
        private int candyBoxCounter = 0;

        public CandyBoxThread(ProduceListener<CandyBox> produceListener) {
            super("CandyBoxProducer");
            this.produceListener = produceListener;
        }

        @Override
        public void run() {
            while (candyBoxCounter++ < numberOfBoxToProduce) {
                produceListener.produced(new CandyBox());
            }
        }
    }

}

/**
 *  fills a box if enough wrapped candy is avaible to fill a complete
 *  box, and stores the filled boxes in a fixed length storage
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : CandyBoxerAndStore.java, 2015/10/25
 */
class CandyBoxerAndStore<T extends CandyWrapperAndStore, U extends CandyBoxStore> implements ProduceListener<CandyBox>, Store<CandyBox> {
    private int numberOfCandyBoxToProduce;
    private T candyWrapperAndStore;
    private U candyBoxStore;
    private int candyBoxerAndStoreCounter = 0;
    private CandyBox[] candyBoxes;

    public CandyBoxerAndStore(int numberOfCandyBoxToProduce, T candyWrapperAndStore, U candyBoxStore) {
        this.numberOfCandyBoxToProduce = numberOfCandyBoxToProduce;
        this.candyWrapperAndStore = candyWrapperAndStore;
        this.candyBoxStore = candyBoxStore;
        this.candyBoxes = new CandyBox[numberOfCandyBoxToProduce];
    }

    public void startProducingBoxedCandy() {
        new CandyBoxerThread(this).start();
    }

    @Override
    public void produced(CandyBox item) {
        synchronized (this) {
            candyBoxes[candyBoxerAndStoreCounter++] = item;
            this.notify();
        }
    }

    @Override
    public boolean isAvailable(int quantity) {
        synchronized (this) {
            while (candyBoxes.length < quantity || candyBoxes[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    @Override
    public CandyBox getItem() {
        synchronized (this) {
            while (candyBoxes.length == 0 || candyBoxes[0] == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            CandyBox candyBox = candyBoxes[0];
            candyBoxes = Arrays.copyOfRange(candyBoxes, 1, candyBoxes.length);
            candyBoxerAndStoreCounter--;
            return candyBox;
        }
    }

    private class CandyBoxerThread extends Thread {
        private ProduceListener<CandyBox> produceListener;
        private int candyBoxCounter = 0;

        public CandyBoxerThread(ProduceListener<CandyBox> produceListener) {
            super("CandyBoxer");
            this.produceListener = produceListener;
        }

        @Override
        public void run() {
            while (candyBoxCounter++ < numberOfCandyBoxToProduce) {

                //System.out.println("Trying to get candy box.");
                if (candyBoxStore.isAvailable(1))
                {
                    //System.out.println("Got candy box");
                }

                //System.out.println("Trying to get 3 wrapped candies");
                if (candyWrapperAndStore.isAvailable(1))
                {
                    //System.out.println("Got 3 wrapped candies.");
                }

                //System.out.println("Putting 3 candies in a box");

                CandyBox candyBox = candyBoxStore.getItem();

                int counter =0;
                while( !candyWrapperAndStore.isEmpty() )
                {
                    if (counter++==4)
                    {
                        produceListener.produced(candyBox);
                        candyBox = candyBoxStore.getItem();
                        counter=0;
                        continue;
                    }
                    candyBox.putCandyWrapper(candyWrapperAndStore.getItem());
                }

                if (candyBox.hasCandies())
                {
                    produceListener.produced(candyBox);
                }
            }
        }
    }
}

/**
 * All stores should implement this
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Store.java, 2015/10/25
 */
interface Store<T> {

    boolean isAvailable(int quantity);

    T getItem();
}

/**
 * Candy
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Candy.java, 2015/10/25
 */
class Candy {
    private String candyName;

    public Candy(String candyName) {
        this.candyName = candyName;
    }

    public String getCandyName() {
        return candyName;
    }


    @Override
    public String toString() {
        return "Candy{" +
                "candyName='" + candyName + '\'' +
                '}';
    }
}

/**
 * Wrapper
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Wrapper.java, 2015/10/25
 */
class Wrapper {
    private Candy candy;

    public Candy getCandy() {
        return candy;
    }

    public void setCandy(Candy candy) {
        this.candy = candy;
    }

    @Override
    public String toString() {
        return "Wrapper{" +
                "candy=" + candy +
                '}';
    }
}

/**
 * CandyBox
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : CandyBox.java, 2015/10/25
 */
class CandyBox {
    private Wrapper[] wrappers = new Wrapper[4];
    private int wrapperCounter = 0;

    public void putCandyWrapper(Wrapper wrapper) {
        wrappers[wrapperCounter++] = wrapper;
    }

    public boolean hasCandies()
    {
        return wrapperCounter >0;
    }

    @Override
    public String toString() {
        return "CandyBox{" +
                "wrappers=" + Arrays.toString(wrappers) +
                '}';
    }
}

/**
 * Listener used to intimate production event
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : ProduceListener.java, 2015/10/25
 */
interface ProduceListener<T> {
    void produced(T item);
}



