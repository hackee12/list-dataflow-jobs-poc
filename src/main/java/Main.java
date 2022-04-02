import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

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

        var service = new DataflowService(dataflow, new ResourceBoundary(projectId, region));

        List<Job> allJobs = service.listAllJobs();
        Job jobById = service.getJobById(allJobs.get(0).getId());
        System.out.println(jobById);
    }
}
