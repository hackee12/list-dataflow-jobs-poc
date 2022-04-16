package tdd.resource;

import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.Job;
import com.google.api.services.dataflow.model.ListJobsResponse;
import current.ResourceBoundary;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tdd.model.Page;

@RequiredArgsConstructor
public class DataflowResourceAdapter implements Resource<Page<Job>> {

    private final Dataflow dataflow;
    private final ResourceBoundary resourceBoundary;

    @Override
    @SneakyThrows
    public Page<Job> nextPage(String nextPageToken) {
        final ListJobsResponse jobsResponse = dataflow.projects()
                .locations()
                .jobs()
                .list(resourceBoundary.getProjectId(), resourceBoundary.getRegion())
                .setPageToken(nextPageToken)
                .execute();
        return new Page<>(jobsResponse.getNextPageToken(), jobsResponse.getJobs());
    }
}
