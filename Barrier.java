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
    }
}