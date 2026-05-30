import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CaesarCipher cipher = new CaesarCipher();
        FileManager fileManager = new FileManager();
        Validator validator = new Validator();

        printMenu();

        while (true) {
            printMenu();
            System.out.print(" >> ");
            String line = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 0 до 4.");
                continue;
            }

            if (choice == 0) {
                printGoodbye();
                break;
            }

            try {
                switch (choice) {
                    case 1 -> handleEncrypt(scanner, cipher, fileManager, validator);
                    case 2 -> handleDecrypt(scanner, cipher, fileManager, validator);
                    case 3 -> handleBruteForce(scanner, fileManager, validator);
                    case 4 -> handleStatisticalAnalysis(scanner, fileManager, validator);
                    default -> System.out.println("Неверный выбор!");
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("  ┌──────────────────────────────────────────────┐");
        System.out.println("  │              ГЛАВНОЕ МЕНЮ                    │");
        System.out.println("  ├──────────────────────────────────────────────┤");
        System.out.println("  │  [1]  Шифровать текст                        │");
        System.out.println("  │  [2]  Расшифровать текст с ключом            │");
        System.out.println("  │  [3]  Brute force (перебор)                  │");
        System.out.println("  │  [4]  Статистический анализ                  │");
        System.out.println("  ├──────────────────────────────────────────────┤");
        System.out.println("  │  [0]  Выход                                  │");
        System.out.println("  └──────────────────────────────────────────────┘");
    }

    private static void printGoodbye() {
        System.out.println();
        System.out.println("  ──────────────────────────────────────────────");
        System.out.println("  Сессия завершена. До встречи!");
        System.out.println("  ──────────────────────────────────────────────");
    }

    private static void printModeHeader(String title) {
        System.out.println();
        System.out.println("  ▶ " + title);
        System.out.println("  " + "─".repeat(44));
    }

    private static void handleEncrypt(Scanner scanner,
                                      CaesarCipher cipher,
                                      FileManager fileManager,
                                      Validator validator) throws Exception {
        printModeHeader("Режим: шифрование");
        System.out.print("Путь к файлу с текстом: ");
        String inputFile = scanner.nextLine().trim();
        System.out.print("Путь к файлу для результата: ");
        String outputFile = scanner.nextLine().trim();
        int key = readKey(scanner, cipher.getAlphabetSize());

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не найден!");
            return;
        }
        if (!validator.isValidOutputFile(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для записи!");
            return;
        }
        if (!validator.isValidKey(key, cipher.getAlphabetSize())) {
            System.out.println("Ошибка: ключ должен быть от 0 до " + (cipher.getAlphabetSize() - 1));
            return;
        }

        String text = fileManager.readFile(inputFile);
        String encrypted = cipher.encrypt(text, key);
        fileManager.writeFile(encrypted, outputFile);
        System.out.println("Готово! Зашифрованный текст сохранён в " + outputFile);
    }

    private static void handleDecrypt(Scanner scanner,
                                      CaesarCipher cipher,
                                      FileManager fileManager,
                                      Validator validator) throws Exception {
        printModeHeader("Режим: расшифровка с ключом");
        System.out.print("Путь к зашифрованному файлу: ");
        String inputFile = scanner.nextLine().trim();
        System.out.print("Путь для сохранения: ");
        String outputFile = scanner.nextLine().trim();
        int key = readKey(scanner, cipher.getAlphabetSize());

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не найден!");
            return;
        }
        if (!validator.isValidOutputFile(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для записи!");
            return;
        }
        if (!validator.isValidKey(key, cipher.getAlphabetSize())) {
            System.out.println("Ошибка: ключ должен быть от 0 до " + (cipher.getAlphabetSize() - 1));
            return;
        }

        String text = fileManager.readFile(inputFile);
        String decrypted = cipher.decrypt(text, key);
        fileManager.writeFile(decrypted, outputFile);
        System.out.println("Готово! Расшифрованный текст сохранён в " + outputFile);
    }

    private static void handleBruteForce(Scanner scanner,
                                         FileManager fileManager,
                                         Validator validator) throws Exception {
        printModeHeader("Режим: brute force");
        System.out.print("Путь к зашифрованному файлу: ");
        String inputFile = scanner.nextLine().trim();
        System.out.print("Путь для сохранения: ");
        String outputFile = scanner.nextLine().trim();
        System.out.print("Путь к файлу-образцу (Enter если нет): ");
        String sampleFile = scanner.nextLine().trim();

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не найден!");
            return;
        }
        if (!validator.isValidOutputFile(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для записи!");
            return;
        }

        BruteForce bruteForce = new BruteForce();
        String result = bruteForce.decryptByBruteForce(inputFile, sampleFile);
        fileManager.writeFile(result, outputFile);
        System.out.println("Готово! Результат сохранён в " + outputFile);
    }

    private static void handleStatisticalAnalysis(Scanner scanner,
                                                  FileManager fileManager,
                                                  Validator validator) throws Exception {
        printModeHeader("Режим: статистический анализ");
        System.out.print("Путь к зашифрованному файлу: ");
        String inputFile = scanner.nextLine().trim();
        System.out.print("Путь для сохранения: ");
        String outputFile = scanner.nextLine().trim();
        System.out.print("Путь к файлу-образцу (Enter если нет): ");
        String sampleFile = scanner.nextLine().trim();

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не найден!");
            return;
        }
        if (!validator.isValidOutputFile(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для записи!");
            return;
        }

        StatisticalAnalyzer analyzer = new StatisticalAnalyzer();
        String result = analyzer.decryptByStatisticalAnalysis(inputFile, sampleFile);
        fileManager.writeFile(result, outputFile);
        System.out.println("Готово! Результат сохранён в " + outputFile);
    }

    private static int readKey(Scanner scanner, int alphabetSize) {
        System.out.print("Ключ (сдвиг от 0 до " + (alphabetSize - 1) + "): ");
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ключ должен быть целым числом.");
        }
    }
}
