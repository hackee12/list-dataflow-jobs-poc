import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class DataflowService {
    private final Dataflow dataflow;
    private final ResourceBoundary boundary;

    public List<Job> listAllJobs() throws IOException {
        return dataflow.projects()
                .locations()
                .jobs()
                .list(boundary.getProjectId(), boundary.getRegion())
                .execute()
                .getJobs();
    }

    public Job getJobById(String jobId) throws IOException {
        return dataflow.projects()
                .locations()
                .jobs()
                .get(boundary.getProjectId(), boundary.getRegion(), jobId)
                .execute();
    }
}
