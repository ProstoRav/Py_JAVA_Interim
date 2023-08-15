package geekbrains;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

class Logic {
    public static void createResultsFile(ToyGiveaway toyGiveaway) {
        String folderPath = createOutputFolder();
        Path basePath = Paths.get(folderPath);
        String fileName = getFileName();
        Path filePath = basePath.resolve(fileName);
        writeResultsToFile(toyGiveaway, filePath);
    }

    private static String createOutputFolder() {
        String folderPath = "ToyStore_JAVA/Giveaway_results";
        boolean wasSuccessful = new File(folderPath).mkdirs();
        if (wasSuccessful) {
            System.out.println("Создаём папку " + folderPath + " для записи результатов розыгрыша");
            System.out.println();
        }
        return folderPath;
    }

    private static String getFileName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);
        return "giveaway_result_" + timestamp + ".txt";
    }

    private static void writeResultsToFile(ToyGiveaway toyGiveaway, Path filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath.toString())) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            String title = "Розыгрыш призов в магазине игрушек от: " + timestamp;
            System.out.println(title);
            fileWriter.write(title + "\n");
            fileWriter.write("\n");

            performToyGiveaway(toyGiveaway, fileWriter);

            System.out.println();
            System.out.println("Результаты розыгрыша записаны в файл " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void performToyGiveaway(ToyGiveaway toyGiveaway, FileWriter fileWriter) throws IOException {
        int t = 1;
        while (t <= 10 && toyGiveaway.hasToys()) {
            Toy toy = toyGiveaway.getToy();
            if (toy != null) {
                String result = "Розыгрыш №" + t + ". Вы выиграли игрушку под номером " + toy.getToyId() + ", " + toy.getToyName();
                System.out.println(result);
                fileWriter.write(result + "\n");
            }
            t++;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ToyGiveaway toyGiveaway = new ToyGiveaway();

        toyGiveaway.addToy(new Toy(1, "Конструктор", 2));
        toyGiveaway.addToy(new Toy(2, "Робот", 2));
        toyGiveaway.addToy(new Toy(3, "Кукла", 6));

        Logic.createResultsFile(toyGiveaway);

        System.exit(0);
    }
}