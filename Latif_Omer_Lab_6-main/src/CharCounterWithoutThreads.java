import java.io.FileReader;
import java.io.IOException;

public class CharCounterWithoutThreads {
    private String filePath;

    public CharCounterWithoutThreads(String filePath) {
        this.filePath = filePath;
    }

    public int countCharacters() {
        int characterCount = 0;
        try (FileReader reader = new FileReader(this.filePath)) {
            while (reader.read() != -1) { // No need to check if it's whitespace
                characterCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return characterCount;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the file path.");
            return;
        }
        CharCounterWithoutThreads counter = new CharCounterWithoutThreads(args[0]);
        System.out.println("Number of characters: " + counter.countCharacters());
    }
}
