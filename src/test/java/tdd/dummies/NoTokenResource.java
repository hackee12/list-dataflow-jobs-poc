package tdd.dummies;

import tdd.Page;
import tdd.Resource;

import java.util.List;

public class NoTokenResource implements Resource<Page<String>> {
    @Override
    public Page<String> nextPage(String nextPageToken) {
        if (nextPageToken == null) return new Page<>(null, List.of("a"));
        throw new RuntimeException(String.format("Invalid page token [%s].", nextPageToken));
    }
}
