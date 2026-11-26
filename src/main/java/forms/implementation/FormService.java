package forms.implementation;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import forms.interfaces.FormServiceInterface;
import storage.implementations.LocalStorageService;
import storage.interfaces.StorageService;

import java.io.IOException;

public class FormService implements FormServiceInterface {
    //Create a form - form builder
    //TODO: Link form to job when creating the job

    /**
     * Creates a form in the data/forms directory
     * @param name Name of the form to be created
     * @param content Contents of the form
     */
    @Override
    public void createForm(String name, String content) {
        try {
            StorageService storageService = new LocalStorageService();
            storageService.createFile(("data/forms/" + name + ".json"), content);
        } catch (IOException e) {
            //TODO: Send response to client that the file already exists, so name should be changed
            e.printStackTrace();
        }
    }

    //Convert to a generic json object
    //Edit the specific field
    //Overwrite the file

    //Updates a specific field in the form

    /**
     * Updates the value of the field in the specified form
     * @param name The name of the form to update
     * @param key The JSON key whose value will be updated
     * @param value The new value to update to
     * @throws IOException Thrown by {@link StorageService} if an error has occurred with
     * the file
     */
    @Override
    public void updateForm(String name, String key, String value) throws IOException {
        String pathStr = LocalStorageService.formsDirStr + "/" + name + ".json";

        try {
            //Read file and convert to a generic json object
            StorageService storageService = new LocalStorageService();
            String content = storageService.readFile(pathStr);

            JsonObject formJsonObject = JsonParser
                    .parseString(content).getAsJsonObject();

            //Modifying the specified field
            formJsonObject.addProperty(key, value);

            String updatedForm = new GsonBuilder()
                    .setPrettyPrinting()//?
                    .create()
                    .toJson(formJsonObject);

            storageService.updateFile(pathStr, updatedForm);

        } catch (IOException e) {
            //TODO: Send response to client that an error has occurred
            e.printStackTrace();
        }


    }

    //Update the fields in the form
    //Get the form via Http request using target
    @Override
    public String requestForm(String name) throws IOException {
        //Return a String (maybe a generic json object and its converted when about to be sent)
        StorageService storageService = new LocalStorageService();
        String pathStr = LocalStorageService.formsDirStr + "/" + name + ".json";
        String content = "";
        try {
            content = storageService.readFile(pathStr);
            return content;
        } catch (IOException e) {
            //TODO: Send response to client that an error has occurred
            e.printStackTrace();
        }

        return content;
    }
}
