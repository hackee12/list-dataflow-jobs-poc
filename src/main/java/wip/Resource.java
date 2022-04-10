package wip;

public interface Resource<T> {
    T nextPage(String nextPageToken);
}
