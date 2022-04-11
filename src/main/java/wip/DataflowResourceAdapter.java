package wip;

import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;
import com.google.api.services.dataflow.model.ListJobsResponse;
import current.ResourceBoundary;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class DataflowResourceAdapter implements Resource<Page<Job>> {

    private final Dataflow dataflow;
    private final ResourceBoundary resourceBoundary;

    @Override
    public Page<Job> nextPage(String nextPageToken) throws IOException {
        final ListJobsResponse jobsResponse = dataflow.projects()
                .locations()
                .jobs()
                .list(resourceBoundary.getProjectId(), resourceBoundary.getRegion())
                .setPageToken(nextPageToken)
                .execute();
        return new Page<>(jobsResponse.getNextPageToken(), jobsResponse.getJobs());
    }
}
