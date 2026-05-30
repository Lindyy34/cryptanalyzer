import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Validator {
    private static final String[] FORBIDDEN_OUTPUT_NAMES = {
            ".bash_profile", ".bashrc", "hosts", "passwd", "shadow"
    };

    public boolean isFileExists(String filePath) {
        return Files.isRegularFile(Paths.get(filePath.trim()));
    }

    public boolean isValidKey(int key, int alphabetSize) {
        return key >= 0 && key < alphabetSize;
    }

    public int normalizeKey(int key, int alphabetSize) {
        key = key % alphabetSize;
        if (key < 0) {
            key += alphabetSize;
        }
        return key;
    }

    public boolean isValidOutputFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return false;
        }
        String name = Path.of(filePath.trim()).getFileName().toString().toLowerCase();
        for (String forbidden : FORBIDDEN_OUTPUT_NAMES) {
            if (name.equals(forbidden)) {
                return false;
            }
        }
        return true;
    }
}
