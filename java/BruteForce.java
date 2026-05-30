import java.io.IOException;

public class BruteForce {

    public String decryptByBruteForce(String inputFile, String sampleFile) throws IOException {
        FileManager fileManager = new FileManager();
        CaesarCipher cipher = new CaesarCipher();

        String encryptedText = fileManager.readFile(inputFile);
        String sampleText = readSampleIfPresent(sampleFile, fileManager);

        String bestDecryption = "";
        int bestScore = -1;
        int bestKey = 0;

        for (int key = 0; key < cipher.getAlphabetSize(); key++) {
            String decrypted = cipher.decrypt(encryptedText, key);
            int score = evaluateText(decrypted, sampleText);
            if (score > bestScore) {
                bestScore = score;
                bestDecryption = decrypted;
                bestKey = key;
            }
        }

        System.out.println("Найден ключ: " + bestKey + " (оценка: " + bestScore + ")");
        return bestDecryption;
    }

    private String readSampleIfPresent(String sampleFile, FileManager fileManager) throws IOException {
        if (sampleFile != null && !sampleFile.isBlank() && fileManager.fileExists(sampleFile)) {
            return fileManager.readFile(sampleFile);
        }
        return null;
    }

    private int evaluateText(String text, String sampleText) {
        if (sampleText != null) {
            return compareWithSample(text, sampleText);
        }

        int score = 0;
        char[] vowels = {'а', 'е', 'и', 'о', 'у', 'ы', 'э', 'я'};
        char[] punctuation = {'.', ',', '!', '?', ':', '"', '«', '»'};

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                score += 3;
            }
            for (char vowel : vowels) {
                if (c == vowel) {
                    score++;
                    break;
                }
            }
            for (char punct : punctuation) {
                if (c == punct) {
                    score += 2;
                    break;
                }
            }
        }
        return score;
    }

    private int compareWithSample(String decrypted, String sample) {
        int matches = 0;
        String[] decryptedWords = decrypted.toLowerCase().split("[^а-я]+");
        String[] sampleWords = sample.toLowerCase().split("[^а-я]+");

        for (String word : decryptedWords) {
            if (word.length() < 2) {
                continue;
            }
            for (String sampleWord : sampleWords) {
                if (word.equals(sampleWord)) {
                    matches += word.length();
                    break;
                }
            }
        }
        return matches;
    }
}
