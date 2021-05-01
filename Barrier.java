import java.util.ArrayList;

public class Barrier {
    private int numberOfParticipants;
    private long upperBound;

    private ArrayList<Long> suggestions;

    private long currentSuggestion;
    private long middle;

    public Barrier(int numberOfParticipants, long upperBound) {
        this.numberOfParticipants = numberOfParticipants;
        this.upperBound = upperBound;
        suggestions = new ArrayList<>();
        currentSuggestion = -1;
        middle = (long) Math.sqrt(upperBound);
    }

    public synchronized long waitBarrier(long suggestion) throws InterruptedException {
        suggestions.add(suggestion);
//        long currentSuggestion = -1;

        if (suggestions.size() != numberOfParticipants) {
            wait();
        } else {
            chooseSuggestion();
            suggestions.clear();
            notifyAll();
        }

        return currentSuggestion;
    }

    private void chooseSuggestion() {
//        System.out.println("suggestions "+Arrays.toString(suggestions.toArray()));

        currentSuggestion = upperBound + 1;
        for (int i = 0; i < suggestions.size(); i++) {
            if (suggestions.get(i) > 0) {
                if (suggestions.get(i) < currentSuggestion) {
                    currentSuggestion = suggestions.get(i);
                }
            }
        }

        if (currentSuggestion > middle) {
            currentSuggestion = -1;
        }

//        if (currentSuggestion == upperBound + 1) {
//            currentSuggestion = -1;
//        }

////        System.out.println(currentSuggestion);
//        return currentSuggestion;
    }
//
//    public synchronized void waitForEnd() throws InterruptedException {
//        numberOfParticipants--;
//
//        if(numberOfParticipants!=0){
//            wait();
//        }else{
//            notifyAll();
//        }
//    }
}
