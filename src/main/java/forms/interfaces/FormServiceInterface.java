package forms.interfaces;

import java.io.IOException;

public interface FormServiceInterface {
    //Create a form - form builder
    //Create form must also link the form to the job
    void createForm(String name, String content);

    //Updates a specific field in the form
    void updateForm(String name, String key, String value) throws IOException;

    //Get the form via Http request using target
    void requestForm();
}
