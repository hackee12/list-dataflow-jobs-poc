import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dataflow.Dataflow;

import java.io.IOException;
import java.security.GeneralSecurityException;

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
        var allJobs = service.listAllJobs();
        var job = allJobs.get(0);
        var id = job.getId();
        var name = job.getName();
        service.isJobStatusFailure(service.getJobById(id));
        service.isJobStatusSuccess(service.getJobByName(name));
    }
}
