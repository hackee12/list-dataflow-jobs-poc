import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

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

        if (isNull(service.getJobById(allJobs.get(0).getId())) ) {
            throw new RuntimeException("Can't find job by id.");
        }
        if (isNull(service.getJobByName(allJobs.get(0).getName()))) {
            throw new RuntimeException("Can't find job by name.");
        }
    }
}
