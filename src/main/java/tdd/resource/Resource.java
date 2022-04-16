package tdd.resource;

public interface Resource<T> {
    T nextPage(String nextPageToken);
}
