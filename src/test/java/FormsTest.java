import forms.implementation.FormService;
import forms.interfaces.FormServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import storage.implementations.LocalStorageService;
import storage.interfaces.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormsTest {
    //Resets the data dir before each test
    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Path.of("data/forms/TestForm.json"));
        Files.deleteIfExists(Path.of("data/forms"));

        StorageService storageService = new LocalStorageService();
        storageService.init();
    }

    @Test
    @DisplayName("F1: Form is created successfully (valid)")
    void createForm() throws IOException {
        FormServiceInterface formService = new FormService();
        formService.createForm("testForm", "Test form");

        assertEquals("Test form", Files.readString(Path.of("data/forms/testForm.json")));
    }
}
