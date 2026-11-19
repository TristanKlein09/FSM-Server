package forms.implementation;

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
            //TODO: Send request to client that the file already exists, so name should be changed
            e.printStackTrace();
        }
    }

    //Updates a specific field in the form
    @Override
    public void updateForm() {

    }

    @Override
    public void requestForm() {

    }


    //Update the fields in the form

    //Get the form via Http request using target
}
