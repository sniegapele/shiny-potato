/*
    Kotryna Šlepetytė
    Informatika, 3k. 2gr.
    Užduotis: 3. Pirminių skaičių generavimas "Eratosthenes" rėčio metodu.
*/

public class Main {
//    private static double unparalleledTime;
//    private static double paralleledTime;

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 1 && Long.parseLong(args[0]) > 1) {
            UnparalleledAlgorithm unparalleledAlgorithm = new UnparalleledAlgorithm(Long.parseLong(args[0]));
            unparalleledAlgorithm.executeAlgorithm();

        } else if (args.length == 2 && Integer.parseInt(args[0]) > 0 && Long.parseLong(args[1]) > 1) {
            ParalleledAlgorithm paralleledAlgorithm = new ParalleledAlgorithm(Integer.parseInt(args[0]),
                    Long.parseLong(args[1]));
        paralleledAlgorithm.executeAlgorithm();
        } else {
            System.out.println("Incorrect parameters!");
        }


//        if (args.length != 2) {
//            System.out.println("Invalid number of arguments. Must be: number of threads, upper bound");
//            return;
//        }
//
//        int numberOfThreads = Integer.parseInt(args[0]);
//        long upperBound = Long.parseLong(args[1]);
//
//        if (!(numberOfThreads > 0 && upperBound > 2)) {
//            System.out.println("Invalid parameters. Must be positive integers.");
//            return;
//        }
//
//        for (int i = 2; i <= 8; i++) {
//
//            numberOfThreads = i;
//
//            ParalleledAlgorithm paralleledAlgorithm = new ParalleledAlgorithm(numberOfThreads, upperBound);
//            UnparalleledAlgorithm unparalleledAlgorithm = new UnparalleledAlgorithm(upperBound);
//
//            ArrayList<Long> firstResult = unparalleledAlgorithm.run();
//            ArrayList<Long> secondResult = paralleledAlgorithm.run();
//
//            unparalleledTime = unparalleledAlgorithm.getTime();
//            paralleledTime = paralleledAlgorithm.getTime();
//
//            runTest(firstResult, secondResult);
//
//            System.out.println("number of threads: " + numberOfThreads + " " + unparalleledTime + " " + paralleledTime + " " + unparalleledTime / paralleledTime);
//            System.out.println("----------------");
//        }
    }

//    private static void runTest(ArrayList<Long> first, ArrayList<Long> second) {
//        if (first.size() != second.size()) {
//            System.out.println("Sizes are equal: FAILED");
//            return;
//        } else {
//            System.out.println("Sizes are equal: OK");
//        }
//
//        for (int i = 0; i < first.size(); i++) {
//            if (!first.get(i).equals(second.get(i))) {
//                System.out.println("Equality test: FAILED");
//                return;
//            }
//        }
//        System.out.println("Equality test: OK");
//    }
}
