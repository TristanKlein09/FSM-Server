import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    @DisplayName("F2: Form updated successfully (valid)")
    void updateForm() throws IOException {
        //Create contents of the form
        JsonObject formJson = new JsonObject();
        formJson.addProperty("Test field 1", "Value 1");
        formJson.addProperty("Test field 2", "Value 2");
        String json = new Gson().toJson(formJson);

        FormServiceInterface formService = new FormService();
        formService.createForm("testForm", json);
        formService.updateForm("testForm", "Test field 1", "Updated value");

        assertTrue(Files.readString(Path.of("data/forms/testForm.json")).contains("Updated value"));
    }
}
