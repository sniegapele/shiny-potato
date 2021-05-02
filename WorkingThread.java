public class WorkingThread extends Thread {
    private ParalleledAlgorithmManager manager;
    private int upperBound;

    private int receivedPrimeNumber;

    public WorkingThread(ParalleledAlgorithmManager manager, int upperBound) {
        this.manager = manager;
        this.upperBound = upperBound;
    }

    @Override
    public void run() {
        receivedPrimeNumber = manager.getNextPrime();
        while (receivedPrimeNumber > 0) {
            markAsNotPrime(receivedPrimeNumber);
            receivedPrimeNumber = manager.getNextPrime();
        }
    }

    private void markAsNotPrime(int receivedPrimeNumber) {
        for (int i = receivedPrimeNumber * receivedPrimeNumber; i <= upperBound; i += receivedPrimeNumber) {
            if (manager.getNumbersData(i)) {
                manager.removeFromPrimeList(i);
            }
        }
    }
}
