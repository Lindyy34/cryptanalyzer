import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatisticalAnalyzer {

    public String decryptByStatisticalAnalysis(String inputFile, String sampleFile) throws IOException {
        FileManager fileManager = new FileManager();
        CaesarCipher cipher = new CaesarCipher();

        String encryptedText = fileManager.readFile(inputFile);
        if (sampleFile != null && !sampleFile.isBlank() && fileManager.fileExists(sampleFile)) {
            String sampleText = fileManager.readFile(sampleFile);
            return decryptWithSample(encryptedText, sampleText, cipher);
        }
        return decryptWithoutSample(encryptedText, cipher);
    }

    private String decryptWithSample(String encrypted, String sample, CaesarCipher cipher) {
        Map<Character, Double> sampleFreq = calculateFrequencyPerThousand(sample);
        Map<Character, Double> encryptedFreq = calculateFrequencyPerThousand(encrypted);

        int bestKey = 0;
        double minDifference = Double.MAX_VALUE;

        for (int key = 0; key < cipher.getAlphabetSize(); key++) {
            double difference = calculateDifference(encryptedFreq, sampleFreq, key, cipher);
            if (difference < minDifference) {
                minDifference = difference;
                bestKey = key;
            }
        }

        System.out.println("Найден ключ: " + bestKey + " (отклонение: " + String.format("%.4f", minDifference) + ")");
        return cipher.decrypt(encrypted, bestKey);
    }

    private String decryptWithoutSample(String encrypted, CaesarCipher cipher) {
        Map<Character, Integer> charCount = new HashMap<>();

        for (int i = 0; i < encrypted.length(); i++) {
            char c = encrypted.charAt(i);
            if (cipher.findCharIndex(c) >= 0) {
                char normalized = Character.toLowerCase(c);
                if (c >= 'А' && c <= 'Я') {
                    normalized = (char) (c + ('а' - 'А'));
                }
                charCount.put(normalized, charCount.getOrDefault(normalized, 0) + 1);
            }
        }

        char mostFrequent = ' ';
        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        int spaceIndex = cipher.findCharIndex(' ');
        int mostFrequentIndex = cipher.findCharIndex(mostFrequent);
        int key = mostFrequentIndex - spaceIndex;
        if (key < 0) {
            key += cipher.getAlphabetSize();
        }

        System.out.println("Самый частый символ: '" + mostFrequent + "', предполагаемый ключ: " + key);
        return cipher.decrypt(encrypted, key);
    }

    private Map<Character, Double> calculateFrequencyPerThousand(String text) {
        Map<Character, Integer> count = new HashMap<>();
        int total = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            count.put(c, count.getOrDefault(c, 0) + 1);
            total++;
        }

        Map<Character, Double> frequency = new HashMap<>();
        if (total == 0) {
            return frequency;
        }

        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            frequency.put(entry.getKey(), entry.getValue() * 1000.0 / total);
        }
        return frequency;
    }

    private double calculateDifference(Map<Character, Double> encryptedFreq,
                                       Map<Character, Double> sampleFreq,
                                       int key,
                                       CaesarCipher cipher) {
        double difference = 0;
        char[] alphabet = cipher.getAlphabet();

        for (int i = 0; i < alphabet.length; i++) {
            int newIndex = (i + key) % alphabet.length;
            char decryptedChar = alphabet[newIndex];

            double encFreq = encryptedFreq.getOrDefault(alphabet[i], 0.0);
            double sampFreq = sampleFreq.getOrDefault(decryptedChar, 0.0);
            double diff = encFreq - sampFreq;
            difference += diff * diff;
        }
        return difference;
    }
}
