import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.ListJobsResponse;

import java.io.IOException;

public class Service {
    private final Dataflow dataflow;

    public Service(Dataflow dataflow) {
        this.dataflow = dataflow;
    }

    public ListJobsResponse listAllJobs(String projectId, String location) throws IOException {
        return dataflow.projects()
                .locations()
                .jobs()
                .list(projectId, location)
                .execute();
    }
}
