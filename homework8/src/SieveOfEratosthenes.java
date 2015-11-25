public class SieveOfEratosthenes {

    final static int FIRSTpRIMEuSED = 2;
    static int MAX;
    final boolean[] numbers;
    int maxActiveThread;

    private ExecutorService<SieveOfEratosthenesThread> threadExecutorService;

    public SieveOfEratosthenes(int max, int maxActiveThread) {
        numbers = new boolean[max];
        this.MAX = max;
        this.maxActiveThread = maxActiveThread;
        threadExecutorService = new FixedExecutorService
                <SieveOfEratosthenesThread>(maxActiveThread);
        threadExecutorService.start();
    }

    public void determinePrimeNumbers() {
        for (int index = 1; index < MAX; index++) {
            numbers[index] = true;
        }

        int limit = (MAX > 10 ? (int) Math.sqrt(MAX) + 1 : 3);

        for (int index = 2; index < limit; index++) {

            threadExecutorService.submit(new SieveOfEratosthenesThread(numbers, index, MAX));

        }
        while (!threadExecutorService.isAllTaskCompleted()) ;
    }

    public void testForPrimeNumber() {
        int[] test = {2, 3, 4, 7, 13, 17, MAX - 1, MAX};
        for (int index = 0; index < test.length; index++) {
            if (test[index] < MAX) {
                System.out.println(test[index] + " = " + numbers[test[index]]);
            }
        }
        threadExecutorService.stop();
    }

    public static void main(String[] args) {

        SieveOfEratosthenes aSieveOfEratosthenes = new SieveOfEratosthenes(20, Integer.parseInt(args[0]));
        aSieveOfEratosthenes.determinePrimeNumbers();
        aSieveOfEratosthenes.testForPrimeNumber();
        //System.exit(0);
    }
}


