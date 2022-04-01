import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;

import java.io.IOException;
import java.util.List;

public class Service {
    private final Dataflow dataflow;

    public Service(Dataflow dataflow) {
        this.dataflow = dataflow;
    }

    public List<Job> listAllJobs(String projectId, String location) throws IOException {
        return dataflow.projects()
                .locations()
                .jobs()
                .list(projectId, location)
                .execute()
                .getJobs();
    }

    public Job getJobById(String projectId, String location, String jobId) throws IOException {
        return dataflow.projects()
                .locations()
                .jobs()
                .get(projectId, location, jobId)
                .execute();
    }
}
