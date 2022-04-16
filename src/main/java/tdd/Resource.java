package tdd;

import java.io.IOException;

public interface Resource<T> {
    T nextPage(String nextPageToken) throws IOException;
}
