package storage.implementations;

import storage.interfaces.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalStorageService implements StorageService {
    private Path dataDir;
    private Path formsDir;

    public static final String dataDirStr = "data";
    public static final String formsDirStr = "data/forms";
    /**
     * Initialises the 'data' file directory
     * @throws IOException thrown if there has been an issue creating the directory
     */
    @Override
    public void init() throws IOException {
        //Initialises the data directory
        this.dataDir = Path.of(this.dataDirStr);
        this.formsDir = Path.of(this.formsDirStr);

        Files.createDirectories(this.dataDir);
        Files.createDirectories(this.formsDir);
    }

    /**
     *
     * @param pathStr Path where the new file should be located
     * @param content Content of the file to create
     * @throws IOException thrown if there has been issue creating the file
     */
    @Override
    public void createFile(String pathStr, String content) throws IOException {
        Path path = Path.of(pathStr);
        Files.createFile(path);

        //Write the contents to the file
        Files.writeString(path, content);
    }

    /**
     *
     * @param pathStr Path to the file
     * @param content New contents of the file
     * @throws IOException thrown if there has been an issue writing to the file
     */
    @Override
    public void updateFile(String pathStr, String content) throws IOException {
        Path path = Path.of(pathStr);
        Files.writeString(path, content);
    }

    /**
     *Reads the contents of a file
     * @param pathStr path to the file
     * @return contents of the file
     * @throws IOException thrown when a file doesn't exist or there has been an issue reading it
     */
    @Override
    public String readFile(String pathStr) throws IOException {
        Path path = Path.of(pathStr);

        return Files.readString(path);
    }
}
