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

        System.out.println("Upper bound: " + upperBound);
        System.out.println("Found prime numbers: " + primeNumbers);
        System.out.println("Elapsed: " + time + " seconds");
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

//
//    public ArrayList<Long> run() {
//        init();
//
//        long startTime = System.currentTimeMillis();
//
//        //iki saknies
//        for (int i = 0; i < numbers.length; i++) {
//            long currentNumber = numbers[i];
//            if (currentNumber > 0 && i != numbers.length - 1) {
//                for (int j = i + 1; j < numbers.length; j++) {
//                    if (numbers[j] > 0) {
//                        if (numbers[j] % currentNumber == 0) {
//                            numbers[j] = -1;
//                        }
//                    }
//                }
//            }
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
//        numbers = new long[(int) (upperBound - 1)];
//
//        for (int i = 0; i < numbers.length; i++) {
//            numbers[i] = i + 2;
//        }
//    }
//
//    private ArrayList<Long> generateResult() {
//        ArrayList<Long> result = new ArrayList<>();
//
//        for (int i = 0; i < numbers.length; i++) {
//            if (numbers[i] > 0) {
//                result.add(numbers[i]);
//            }
//        }
//
//        return result;
//    }
//
//    public double getTime() {
//        return time;
//    }
}
