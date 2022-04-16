package tdd;

public interface Resource<T> {
    T nextPage(String nextPageToken);
}
