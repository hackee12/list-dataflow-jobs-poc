package demo;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;
import current.DataflowService;
import current.ResourceBoundary;
import wip.Browser;
import wip.DataflowResourceAdapter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws GeneralSecurityException, IOException {

        final String projectId = args[0];
        // https://cloud.google.com/dataflow/docs/concepts/regional-endpoints
        // invalid location somehow works
        final String region = args[1];

        final Dataflow dataflow = new Dataflow(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleCredential.getApplicationDefault()
        );

        final ResourceBoundary boundary = new ResourceBoundary(projectId, region);
        final DataflowService service = new DataflowService(dataflow, boundary);

        var allJobs = service.getFirstListJobsResponsePage();
        var someJob = allJobs.getJobs().get(0);
        System.out.println("Job status: " + someJob.getCurrentState());
        System.out.println("Is job status success: " + service.isJobStatusSuccess(someJob));
        System.out.println("Is job status failure: " + service.isJobStatusFailure(someJob));

        Optional<Job> jobByName = service.getJobByName(someJob.getName());
        Job jobById = service.getJobById(someJob.getId());

        var invalidJobName = "xxx";
//        if (service.getJobByName(invalidJobName).isEmpty()) {
//            System.out.printf("Can't find job by name [%s].%n", invalidJobName);
//        }

        final DataflowResourceAdapter dataflowResourceAdapter = new DataflowResourceAdapter(dataflow, boundary);
        final Browser<Job> browser = new Browser<>(dataflowResourceAdapter, (job, name) -> job.getName().equals(name));
        System.out.println(browser.search(someJob.getName()));
        System.out.println(browser.search(invalidJobName));
    }
}
