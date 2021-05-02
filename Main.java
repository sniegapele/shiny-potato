/*
    Kotryna Slepetyte
    Informatika, 3k. 2gr.
    Uzduotis: 3. Pirminiu skaiciu generavimas "Eratosthenes" recio metodu.
*/

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length == 1 && Long.parseLong(args[0]) > 1) {

            UnparalleledAlgorithm unparalleledAlgorithm = new UnparalleledAlgorithm(Long.parseLong(args[0]));
            unparalleledAlgorithm.executeAlgorithm();

        } else if (args.length == 3 && args[0].trim().equals("b") && Integer.parseInt(args[1]) > 0 && Long.parseLong(args[2]) > 1) {

            ParalleledAlgorithmBarrier paralleledAlgorithm = new ParalleledAlgorithmBarrier(Integer.parseInt(args[1]),
                    Long.parseLong(args[2]));
            paralleledAlgorithm.executeAlgorithm();

        } else if (args.length == 3 && args[0].trim().equals("m") && Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[2]) > 1) {

            ParalleledAlgorithmManager paralleledAlgorithm = new ParalleledAlgorithmManager(Integer.parseInt(args[2]),
                    Integer.parseInt(args[1]));
            paralleledAlgorithm.executeAlgorithm();

        } else {
            System.out.println("Incorrect parameters!");
        }
    }
}