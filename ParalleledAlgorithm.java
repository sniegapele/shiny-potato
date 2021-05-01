public class ParalleledAlgorithm {
    private int numberOfThreads;
    private long upperBound;
    private long startTime;

    private Barrier barrier;

    private MyThread[] threads;

    public ParalleledAlgorithm(int numberOfThreads, long upperBound) {
        this.numberOfThreads = numberOfThreads;
        this.upperBound = upperBound;
        this.barrier = new Barrier(numberOfThreads, upperBound);
        startTime = System.currentTimeMillis();
    }

    public void executeAlgorithm() throws InterruptedException {
        initThreads();
//        startTime = System.currentTimeMillis();
        for (MyThread thread : threads) {
            thread.start();
        }
        for (MyThread thread : threads) {
            thread.join();
        }

        generateResults();
    }

    private void initThreads() {
        threads = new MyThread[numberOfThreads];

        long numberOfNumbers = (upperBound - 1) / numberOfThreads - 1;
        long leftovers = (upperBound - 1) % numberOfThreads;

        long startNumber = 2;

        for (int i = 0; i < numberOfThreads; i++) {
            if (leftovers > 0) {
                threads[i] = new MyThread(startNumber, Math.min(startNumber + numberOfNumbers + 1, upperBound), barrier, upperBound);
                startNumber = startNumber + numberOfNumbers + 2;
                leftovers--;
            } else {
                threads[i] = new MyThread(startNumber, Math.min(startNumber + numberOfNumbers, upperBound), barrier, upperBound);
                startNumber = startNumber + numberOfNumbers + 1;
            }
        }
    }

    private void generateResults() {
//        double time = (System.currentTimeMillis() - startTime) / 1000.;

        long primeNumbers = getPrimeNumbers();

        double time = (System.currentTimeMillis() - startTime) / 1000.;

        System.out.println("Upper bound: " + upperBound);
        System.out.println("Found prime numbers: " + primeNumbers);
        System.out.println("Elapsed: " + time + " seconds");
    }

    private long getPrimeNumbers() {
        long result = 0;

        for (MyThread thread : threads) {
            result += thread.getPrimeNumbers();
        }

        return result;
    }
//
//    public ArrayList<Long> run() throws InterruptedException {
//        init();
//
//        long startTime = System.currentTimeMillis();
//
//        for (MyThread thread : threads) {
//            thread.start();
//        }
//        for (MyThread thread : threads) {
//            thread.join();
//        }
//
//        ArrayList<Long> result = generateResult();
//
//        time = (System.currentTimeMillis() - startTime) / 1000.;
//
//        return result;
//    }
//
//    private void init() {
//        barrier = new Barrier(numberOfThreads, upperBound);
//        threads = new MyThread[numberOfThreads];
//
//        long numberOfNumbers = (upperBound - 1) / numberOfThreads - 1;
//        long leftovers = (upperBound - 1) % numberOfThreads;
//
//        long startNumber = 2;
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            if (leftovers > 0) {
//                threads[i] = new MyThread(startNumber, Math.min(startNumber + numberOfNumbers + 1, upperBound), barrier);
//                startNumber = startNumber + numberOfNumbers + 2;
//                leftovers--;
//            } else {
//                threads[i] = new MyThread(startNumber, Math.min(startNumber + numberOfNumbers, upperBound), barrier);
//                startNumber = startNumber + numberOfNumbers + 1;
//            }
//        }
//    }
//
//    private ArrayList<Long> generateResult() {
//        ArrayList<Long> result = new ArrayList<>();
//
//        for (MyThread thread : threads) {
//            for (int i = 0; i < thread.getNumbersPart().length; i++) {
//                if (thread.getNumbersPart()[i] > 0) {
////                    System.out.println(thread.getNumbersPart()[i]);
//                    result.add(thread.getNumbersPart()[i]);
//                }
//            }
//        }
//
////        System.out.println(Arrays.toString(result.toArray()));
//        return result;
//    }
//
//    public double getTime() {
//        return time;
//    }
}
