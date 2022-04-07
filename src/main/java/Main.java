import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static java.lang.String.format;

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

        final DataflowService service = new DataflowService(dataflow, new ResourceBoundary(projectId, region));

        var allJobs = service.getFirstListJobsResponsePage();
        var someJob = allJobs.getJobs().get(0);
        System.out.println("Job status: " + someJob.getCurrentState());
        System.out.println("Is job status success: " + service.isJobStatusSuccess(someJob));
        System.out.println("Is job status failure: " + service.isJobStatusFailure(someJob));

        Optional<Job> jobByName = service.getJobByName(someJob.getName());
        Job jobById = service.getJobById(someJob.getId());

        var invalidJobName = "xxx";
        service.getJobByName(invalidJobName)
                .orElseThrow(() -> new RuntimeException(format("Can't find job by name [%s].", invalidJobName)));
    }
}
