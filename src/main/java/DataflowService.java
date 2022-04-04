import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;
import com.google.dataflow.v1beta3.JobState;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class DataflowService {
    private static final Set<String> JOB_STATUS_FAILURE = Stream.of(
                    JobState.JOB_STATE_CANCELLED,
                    JobState.JOB_STATE_FAILED,
                    JobState.JOB_STATE_DRAINED,
                    JobState.JOB_STATE_UNKNOWN,
                    JobState.UNRECOGNIZED
            )
            .map(Object::toString)
            .collect(Collectors.toSet());

    private static final String JOB_STATUS_SUCCESS = JobState.JOB_STATE_DONE.toString();

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

    public Job getJobByName(String jobName) throws IOException {
        return listAllJobs().stream()
                .filter(job -> job.getName().equals(jobName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Can't find job by name %s.", jobName)));
    }

    public boolean isJobStatusSuccess(Job job) {
        return JOB_STATUS_SUCCESS.equals(job.getName());
    }

    public boolean isJobStatusFailure(Job job) {
        return JOB_STATUS_FAILURE.contains(job.getCurrentState());
    }
}
