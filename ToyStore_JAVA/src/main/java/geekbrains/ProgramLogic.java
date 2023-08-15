package geekbrains;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class ProgramLogic {
    public static void runGiveaway() {
        Scanner scanner = new Scanner(System.in);
        ToyGiveawayLogic toyGiveaway = new ToyGiveawayLogic();

        System.out.println("Добро пожаловать в розыгрыш призов в магазине игрушек!");

        int toyId = 1;
        boolean shouldContinue = true;
        while (shouldContinue) {
            System.out.print("Введите название разыгрываемой игрушки (или 'Exit' если все игрушки заведены): ");
            String toyName = scanner.nextLine();

            shouldContinue = !shouldExit(toyName);

            if (shouldContinue) {
                int toyDistributionFrequency = readToyDistributionFrequency(scanner);

                ToyCreation toy = new ToyCreation(toyId++, toyName, toyDistributionFrequency);
                toyGiveaway.addToy(toy);
            }
        }

        if (toyGiveaway.hasToys()) {
            createResultsFile(toyGiveaway);
        } else {
            System.out.println("Нет игрушек для розыгрыша.");
        }

        scanner.close();
    }

    private static boolean shouldExit(String input) {
        return input.equalsIgnoreCase("exit");
    }

    private static int readToyDistributionFrequency(Scanner scanner) {
        System.out.print("Введите частоту выпадения (вес) игрушки: ");
        int frequency = scanner.nextInt();
        scanner.nextLine();
        return frequency;
    }

    private static String createOutputFolder() {
        String FOLDER_PATH = "ToyStore_JAVA/Giveaway_results";
        boolean wasSuccessful = new File(FOLDER_PATH).mkdirs();
        if (wasSuccessful) {
            System.out.println();
            System.out.println("Создаём папку " + FOLDER_PATH + " для записи результатов розыгрыша");
        }
        return FOLDER_PATH;
    }

    private static String getFileName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);
        return "giveaway_result_" + timestamp + ".txt";
    }

    private static void writeResultsToFile(ToyGiveawayLogic toyGiveaway, Path filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath.toString())) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            String title = "Розыгрыш призов в магазине игрушек от: " + timestamp;
            System.out.println();
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

    private static void performToyGiveaway(ToyGiveawayLogic toyGiveaway, FileWriter fileWriter) throws IOException {
        int giveawayNumber = 1;
        while (giveawayNumber <= 10 && toyGiveaway.hasToys()) {
            ToyCreation toy = toyGiveaway.getToy();
            if (toy != null) {
                String result = "Розыгрыш №" + giveawayNumber + ". Вы выиграли игрушку под номером " + toy.getToyId() + ", " + toy.getToyName();
                System.out.println(result);
                fileWriter.write(result + "\n");
            }
            giveawayNumber++;
        }
    }

    public static void createResultsFile(ToyGiveawayLogic toyGiveaway) {
        String folderPath = createOutputFolder();
        Path basePath = Paths.get(folderPath);
        String fileName = getFileName();
        Path filePath = basePath.resolve(fileName);
        writeResultsToFile(toyGiveaway, filePath);
    }
}
