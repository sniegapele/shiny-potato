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
//                System.out.println(received);

                if (received > 0) {
                    removeMultiples(received);
//                    System.out.println(this.getName()+" "+Arrays.toString(numbersPart));
                    prepareSuggestion(received);
//                    System.out.println(currentSuggestion);
                }
//                break;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

    }

    //
//    @Override
//    public void run() {
//        long received = 1;
//        prepareSuggestion(received);
//
//        while (received > 0) {
//            try {
//                received = barrier.waitBarrier(currentSuggestion);
//
//                if (received > 0) {
//                    removeMultiples(received);
//                    prepareSuggestion(received);
//                }
////                else {
////                    barrier.waitForEnd();
////                }
//
//            }catch (Exception e){
//                System.out.println(e);
//                System.out.println("something went wrong with barrier :(");
//            }
//        }
//
////        System.out.println(Arrays.toString(numbersPart));
//    }
//
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

//        i^2+k*i>=lowerBound
//        k>=(lowerBound-i^2)/i
//        lower=12 i=3 --> k=1 --> start=9+3=12
//        lower=11 i=3 --> k=2/3=0 --> start = 9
//        lower=14 i=3 --> k=5/3=1 --> start = 9+3=12
//        lower=2 i=3 --> k=

        long coef = (lowerBound - received * received) / received;

        if (coef < 0) {
            startNumber = received * received;
        } else if (received * received + coef * received < lowerBound) {
            startNumber = received * received + (coef + 1) * received;
        } else {
            startNumber = received * received + coef * received;
        }

//        System.out.println("start: "+startNumber);

        for (long i = startNumber; i <= upperBound; i += received) {
            numbersPart[(int) (i - lowerBound)] = false;
        }

//        if (lowerBound % received == 0) {
//            startNumber = lowerBound / received;
//        } else {
//            startNumber = lowerBound / received + 1;
//        }

//        long endNumber = upperBound / received;

//        for (long i = startNumber; i <= endNumber; i++) {
//            long number = i * received;
//            long index = number - lowerBound;
//            if (numbersPart[(int) index] > 0 && numbersPart[(int) index] != received) {
//                numbersPart[(int) index] = -1;
//            }
//
//        }
////        System.out.println(Arrays.toString(numbersPart));
//    }
//
//    public long[] getNumbersPart() {
//        return numbersPart;
    }

    public long getPrimeNumbers() {
        long counter = 0;

        for (int i = 0; i < numbersPart.length; i++) {
            if (numbersPart[i]) {
//                System.out.println(i + lowerBound);
                counter++;
            }
        }

        return counter;
    }
}
