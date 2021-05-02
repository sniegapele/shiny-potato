import java.util.Arrays;

public class UnparalleledAlgorithm {
    private long upperBound;
    private int lowerBound;
    private boolean[] numberData;
    private long startTime;

    public UnparalleledAlgorithm(long upperBound) {
        this.upperBound = upperBound;
        this.lowerBound = 2;
        startTime = System.currentTimeMillis();
        numberData = new boolean[(int) (this.upperBound - lowerBound + 1)];
        Arrays.fill(numberData, true);
//        startTime = System.currentTimeMillis();
    }

    public void executeAlgorithm() {

        double nSqrtValue = Math.sqrt(upperBound);

        for (int i = 2; i < nSqrtValue; i++) {
            if (numberData[i - lowerBound]) {
                for (int j = i * i; j <= upperBound; j += i) {
                    numberData[j - lowerBound] = false;
                }
            }
        }

        generateResults();
    }

    private void generateResults() {
//        double time = (System.currentTimeMillis() - startTime) / 1000.;

        long primeNumbers = calculatePrimeNumbers();

        double time = (System.currentTimeMillis() - startTime) / 1000.;

        System.out.println("--------------------------------------");
        System.out.println("Unparalleled algorithm");
        System.out.println("Upper bound: " + upperBound);
        System.out.println("Found prime numbers: " + primeNumbers);
        System.out.println("Elapsed: " + time + " seconds");
        System.out.println("--------------------------------------");
    }

    private long calculatePrimeNumbers() {
        long result = 0;
        for (int i = 0; i < numberData.length; i++) {
            if (numberData[i]) {
                result++;
            }
        }
        return result;
    }
}