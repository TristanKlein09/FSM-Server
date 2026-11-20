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

    @Override
    public void requestForm() {
        //Return a String (maybe a generic json object and its converted when about to be sent)

    }


    //Update the fields in the form

    //Get the form via Http request using target
}
