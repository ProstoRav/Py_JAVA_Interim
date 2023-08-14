package geekbrains;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.PriorityQueue;

class Toy {
    private final int toyId;
    private final String toyName;
    private int toyDistributionFrequency;

    public Toy(int toyId, String toyName, int toyDistributionFrequency) {
        this.toyId = toyId;
        this.toyName = toyName;
        this.toyDistributionFrequency = toyDistributionFrequency;
    }

    public int getToyId() {
        return toyId;
    }

    public String getToyName() {
        return toyName;
    }

    public int getToyDistributionFrequency() {
        return toyDistributionFrequency;
    }

    public void setToyDistributionFrequency(int toyDistributionFrequency) {
        this.toyDistributionFrequency = toyDistributionFrequency;
    }
}

class ToyGiveaway {
    private final PriorityQueue<Toy> toyQueue;

    public ToyGiveaway() {
        toyQueue = new PriorityQueue<>(Comparator.comparingInt(Toy::getToyDistributionFrequency).reversed());
    }

    public void addToy(Toy toy) {
        toyQueue.offer(toy);
    }

    public Toy getToy() {
        Toy toy = toyQueue.poll();
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

public class Main {
    public static void main(String[] args) {
        ToyGiveaway toyGiveaway = new ToyGiveaway();

        toyGiveaway.addToy(new Toy(1, "Конструктор", 2));
        toyGiveaway.addToy(new Toy(2, "Робот", 2));
        toyGiveaway.addToy(new Toy(3, "Кукла", 6));

        try {
            FileWriter fileWriter = new FileWriter("toy_giveaway_results.txt");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            String title = "Розыгрыш призов в магазине игрушек от: " + timestamp;
            System.out.println(title);
            fileWriter.write(title + "\n");
            fileWriter.write("\n");

            for (int i = 0; i < 10; i++) {
                int t = i + 1;
                if (toyGiveaway.hasToys()) {

                    Toy toy = toyGiveaway.getToy();

                    if (toy != null) {
                        String result ="Розыгрыш №" + t + ". Вы выиграли игрушку под номером " + toy.getToyId() + ", " + toy.getToyName();
                        System.out.println(result);
                        fileWriter.write(result + "\n");
                    }
                }
            }

            fileWriter.close();
            System.out.println();
            System.out.println("Результаты розыгрыша записаны в файл toy_giveaway_results.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}