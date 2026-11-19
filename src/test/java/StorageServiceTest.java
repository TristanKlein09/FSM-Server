import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import storage.implementations.LocalStorageService;
import storage.interfaces.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageServiceTest {
    //Checks if the data directory has been created correctly
    @Test
    @Order(1)
    @DisplayName("Sto1: Correct initialisation of the data directory (valid)")
    void correctInit() throws IOException {
        StorageService storageService = new LocalStorageService();
        storageService.init();

        assertEquals(true, Files.exists(Path.of("data")));
    }

    //Creates a file with text in it and checks if the file has been
    @Test
    @Order(2)
    @DisplayName("Sto2: Successfully creates a file (valid)")
    void fileCreation() throws IOException {
        StorageService storageService = new LocalStorageService();
        String pathStr = "data/test.txt";
        boolean deleted = Files.deleteIfExists(Path.of("data/test.txt"));

        storageService.createFile(pathStr, "Test");

        assertEquals("Test", Files.readString(Path.of(pathStr)));
    }

    @Test
    @Order(3)
    @DisplayName("Sto3: Successfully reads a file (valid)")
    void fileRead() throws IOException {
        StorageService storageService = new LocalStorageService();
        String pathStr = "data/test.txt";
        Files.writeString(Path.of(pathStr), "Test");

        assertEquals("Test", storageService.readFile(pathStr));
    }

    @Test
    @Order(4)
    @DisplayName("Sto4: Successfully updates a file")
    void fileUpdate() throws IOException {
        StorageService storageService = new LocalStorageService();
        String pathStr = "data/test.txt";
        storageService.updateFile(pathStr, "Updated");

        assertEquals("Updated", Files.readString(Path.of(pathStr)));
    }

}
