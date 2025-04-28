package invertedIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static List<String> getDocumentPaths(String folderPath) {
        List<String> filePaths = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }
        return filePaths;
    }
}
