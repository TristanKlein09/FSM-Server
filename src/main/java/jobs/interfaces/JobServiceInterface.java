package jobs.interfaces;

public interface JobServiceInterface {
    //Create a form
    //Update a form
    //Link the form to the job
    //Get the form via Http request using target

    //Create a job
    //Adds a job to the database
    //Associates a certain form with it
    public void createJob(String content, String form);


    //Update a job based on certain metrics
    public void updateJob();

    //Get job info
    public void getJob(String name);
}
