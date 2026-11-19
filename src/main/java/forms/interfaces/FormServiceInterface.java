package forms.interfaces;

public interface FormServiceInterface {
    //Create a form - form builder
    //Create form must also link the form to the job
    void createForm(String name, String content);

    //Update a specific field in the form
    void updateForm();

    //Get the form via Http request using target
    void requestForm();
}
