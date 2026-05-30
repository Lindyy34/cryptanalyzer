import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileManager {

    public String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath.trim());
        try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines.collect(Collectors.joining(System.lineSeparator()));
        }
    }

    public void writeFile(String content, String filePath) throws IOException {
        Path path = Paths.get(filePath.trim());
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    public boolean fileExists(String filePath) {
        return Files.isRegularFile(Paths.get(filePath.trim()));
    }
}
