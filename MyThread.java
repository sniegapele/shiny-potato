import java.util.Arrays;

public class MyThread extends Thread {
    private Barrier barrier;

    private long lowerBound;
    private long upperBound;

    private long globalUpperBound;
    private long middle;

    private long currentSuggestion;

    private boolean[] numbersPart;

    public MyThread(long lowerBound, long upperBound, Barrier barrier, long globalUpperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        this.globalUpperBound = globalUpperBound;
        this.middle = (long) Math.sqrt(globalUpperBound);

        this.barrier = barrier;

        currentSuggestion = lowerBound;

        this.numbersPart = new boolean[(int) (upperBound - lowerBound + 1)];
        Arrays.fill(numbersPart, true);
    }

    @Override
    public void run() {
        long received = 1;
        prepareSuggestion(received);

        while (received > 0) {
            try {
                received = barrier.waitBarrier(currentSuggestion);

                if (received > 0) {
                    removeMultiples(received);
                    prepareSuggestion(received);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    private void prepareSuggestion(long received) {
        if (lowerBound > middle) {
            currentSuggestion = -1;
            return;
        }

        if (received >= upperBound) {
            currentSuggestion = -1;
            return;
        }

        if (received + 1 == lowerBound || received >= lowerBound) {
            boolean found = false;
            long startPosition;

            if (received + 1 == lowerBound) {
                startPosition = lowerBound;
            } else {
                startPosition = received + 1;
            }

            for (long index = startPosition; index <= upperBound; index++) {
                if (numbersPart[(int) (index - lowerBound)]) {
                    currentSuggestion = index;
                    found = true;
                    break;
                }
            }

            if (!found) {
                currentSuggestion = -1;
            }
        }
    }

    private void removeMultiples(long received) {
        long startNumber;

        long coef = (lowerBound - received * received) / received;

        if (coef < 0) {
            startNumber = received * received;
        } else if (received * received + coef * received < lowerBound) {
            startNumber = received * received + (coef + 1) * received;
        } else {
            startNumber = received * received + coef * received;
        }

        for (long i = startNumber; i <= upperBound; i += received) {
            numbersPart[(int) (i - lowerBound)] = false;
        }
    }

    public long getPrimeNumbers() {
        long counter = 0;

        for (int i = 0; i < numbersPart.length; i++) {
            if (numbersPart[i]) {
                counter++;
            }
        }

        return counter;
    }
}