package current;

import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;
import com.google.api.services.dataflow.model.ListJobsResponse;
import com.google.dataflow.v1beta3.JobState;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor
public class DataflowService {

    private static final String JOB_STATUS_SUCCESS = JobState.JOB_STATE_DONE.toString();
    private static final Set<String> JOB_STATUS_FAILURE =
            Stream.of(
                    JobState.JOB_STATE_CANCELLED,
                    JobState.JOB_STATE_FAILED,
                    JobState.JOB_STATE_DRAINED,
                    JobState.JOB_STATE_UNKNOWN,
                    JobState.UNRECOGNIZED
            ).map(Object::toString).collect(Collectors.toSet());

    private final Dataflow dataflow;
    private final ResourceBoundary boundary;

    private Optional<Job> filterListJobsResponseByJobName(ListJobsResponse response, String jobName) {
        if (null != response.getJobs()) {
            for (Job job : response.getJobs()) {
                if (job.getName().equals(jobName)) {
                    return Optional.of(job);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Primary Job GET.
     * Single call on 'projects.jobs.get'.
     * Lightweight for Client, Server, and Network.
     *
     * @param jobId
     * @return
     * @throws IOException
     */
    public Job getJobById(String jobId) throws IOException {
        return dataflow.projects()
                .locations()
                .jobs()
                .get(boundary.getProjectId(), boundary.getRegion(), jobId)
                .execute();
    }

    /**
     * Secondary Job GET.
     * Iterates through 'projects.jobs.list', deals with pagination.
     * Heavy for Client, Server, and Network.
     *
     * @param jobName
     * @return
     * @throws IOException
     */
    public Optional<Job> getJobByName(String jobName) throws IOException {
        final ListJobsResponse firstPage = getListJobsResponsePage(null);
        final Optional<Job> firstPageJob = filterListJobsResponseByJobName(firstPage, jobName);
        if (firstPageJob.isPresent()) {
            return firstPageJob;
        }
        ListJobsResponse currentPage = firstPage;
        while (currentPage.getNextPageToken() != null) {
            currentPage = getListJobsResponsePage(currentPage.getNextPageToken());
            final Optional<Job> currentPageJob = filterListJobsResponseByJobName(currentPage, jobName);
            if (currentPageJob.isPresent()) {
                return currentPageJob;
            }
        }
        return Optional.empty();
    }

    public ListJobsResponse getFirstListJobsResponsePage() throws IOException {
        return getListJobsResponsePage(null);
    }

    /**
     * @param nextPageToken Set to {@code null} to request the first page.
     *                      Set to {@code 'next_page_token'} of the previous listJobsResponse to request the next page.
     * @return
     * @throws IOException
     */
    public ListJobsResponse getListJobsResponsePage(String nextPageToken) throws IOException {
        return requireNonNull(
                dataflow
                        .projects()
                        .locations()
                        .jobs()
                        .list(boundary.getProjectId(), boundary.getRegion())
                        .setPageToken(nextPageToken)
                        .execute(), "HTTP GET on 'project.jobs.list' returned null."
        );
    }

    public boolean isJobStatusSuccess(Job job) {
        return JOB_STATUS_SUCCESS.equals(job.getName());
    }

    public boolean isJobStatusFailure(Job job) {
        return JOB_STATUS_FAILURE.contains(job.getCurrentState());
    }
}
