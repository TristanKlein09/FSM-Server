package storage.interfaces;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {
    //Initialise the storage environment
    void init() throws IOException;

    //Create files
    void createFile(String pathStr, String content) throws IOException;

    //Update files (overwrite)
    void updateFile(String pathStr, String content) throws IOException;

    //Read files
    String readFile(String pathStr) throws IOException;
}
