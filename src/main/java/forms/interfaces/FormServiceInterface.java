package forms.interfaces;

import storage.interfaces.StorageService;

import java.io.IOException;

public interface FormServiceInterface {
    //Create a form - form builder
    //Create form must also link the form to the job
    /**
     * Creates a form in the data/forms directory
     * @param name Name of the form to be created
     * @param content Contents of the form
     */
    void createForm(String name, String content);

    //Updates a specific field in the form
    /**
     * Updates the value of the field in the specified form
     * @param name The name of the form to update
     * @param key The JSON key whose value will be updated
     * @param value The new value to update to
     * @throws IOException Thrown by {@link StorageService} if an error has occurred with
     * the file
     */
    void updateForm(String name, String key, String value) throws IOException;

    //Get the form via Http request using target
    void requestForm();
}
