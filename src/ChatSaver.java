import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ChatSaver {

    private String path;
    private Path file;

    public ChatSaver(String path) {
        this.path = path;
        file = Paths.get(path);
    }

    public void write(String message) {
        try {
            Files.write(file, Arrays.asList(message), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> read() {
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
