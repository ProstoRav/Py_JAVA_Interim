package geekbrains;

import java.util.Comparator;
import java.util.PriorityQueue;

class ToyGiveawayLogic {
    private final PriorityQueue<ToyCreation> toyQueue;

    public ToyGiveawayLogic() {
        toyQueue = new PriorityQueue<>(Comparator.comparingInt(ToyCreation::getToyDistributionFrequency).reversed());
    }

    public void addToy(ToyCreation toy) {
        toyQueue.offer(toy);
    }

    public ToyCreation getToy() {
        ToyCreation toy = toyQueue.poll();
        if (toy != null) {
            toy.setToyDistributionFrequency(toy.getToyDistributionFrequency() - 1);
            toyQueue.offer(toy);
        }
        return toy;
    }

    public boolean hasToys() {
        return !toyQueue.isEmpty();
    }
}
