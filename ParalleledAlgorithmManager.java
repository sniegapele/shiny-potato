import java.util.Arrays;

public class ParalleledAlgorithmManager {
    private int upperBound;
    private boolean[] numbersData;

    private WorkingThread[] threads;

    private int lastPrime;
    private double middleBound;

    private long startTime;

    public ParalleledAlgorithmManager(int upperBound, int numberOfThreads) {
        this.upperBound = upperBound;

        this.numbersData = new boolean[upperBound - 1];
        Arrays.fill(numbersData, true);

        threads = new WorkingThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new WorkingThread(this, upperBound);
        }

        lastPrime = 1;
        middleBound = Math.sqrt(upperBound);

        startTime = System.currentTimeMillis();
    }

    public void executeAlgorithm() throws InterruptedException {
        for (WorkingThread thread : threads) {
            thread.start();
        }
        for (WorkingThread thread : threads) {
            thread.join();
        }
        generateResults();
    }

    public synchronized int getNextPrime() {
        for (int i = lastPrime + 1; i < middleBound; i++) {
            if (numbersData[i - 2]) {
                lastPrime = i;
                return lastPrime;
            }
        }
        return -1;
    }

    public boolean getNumbersData(int number) {
        return numbersData[number - 2];
    }

    public void removeFromPrimeList(int number) {
        if (numbersData[number - 2]) {
                numbersData[number - 2] = false;
        }
    }

    private void generateResults() {
        int numberOfPrimeNumbers = countPrimeNumbers();

        double time = (System.currentTimeMillis() - startTime) / 1000.;

        System.out.println("--------------------------------------");
        System.out.println("Paralleled algorithm - manager mode");
        System.out.println("Number of threads: " + threads.length);
        System.out.println("Found prime numbers: " + numberOfPrimeNumbers);
        System.out.println("Elapsed: " + time + " seconds");
        System.out.println("--------------------------------------");
    }

    private int countPrimeNumbers() {
        int counter = 0;

        for (int i = 0; i < numbersData.length; i++) {
            if (numbersData[i]) {
                counter++;
            }
        }
        return counter;
    }
}
