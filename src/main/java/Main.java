import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.ListJobsResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
    /**
     * @param args [0] projectId;
     *             [1] regional endpoint that contains jobs
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static void main(String[] args) throws GeneralSecurityException, IOException {

        final String projectId = args[0];

        // https://cloud.google.com/dataflow/docs/concepts/regional-endpoints
        // invalid location somehow works
        final String regionalEndpoint = args[1];

        final Dataflow dataflow = new Dataflow(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleCredential.getApplicationDefault()
        );

        var service = new Service(dataflow);
        var allJobs = service.listAllJobs(projectId, regionalEndpoint);
        System.out.println(allJobs);
    }
}
