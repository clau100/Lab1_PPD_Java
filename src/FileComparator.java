import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileComparator {
    public static boolean areFilesIdentical(String filePath1, String filePath2) {
        try {
            // Read all lines from both files
            List<String> file1Lines = Files.readAllLines(Paths.get(filePath1));
            List<String> file2Lines = Files.readAllLines(Paths.get(filePath2));

            // Compare the contents
            return file1Lines.equals(file2Lines);

        } catch (IOException e) {
            e.printStackTrace();
            // In case of an error, return false
            return false;
        }
    }
}
