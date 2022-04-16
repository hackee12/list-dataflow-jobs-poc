package tdd.dummies;

import tdd.Page;
import tdd.Resource;

import java.util.List;

public class OneTokenResource implements Resource<Page<String>> {
    @Override
    public Page<String> nextPage(String nextPageToken) {
        if (nextPageToken == null) return new Page<>("firstPage", List.of("a"));
        if ("firstPage".equals(nextPageToken)) return new Page<>(null, List.of("b"));
        throw new RuntimeException(String.format("Invalid page token [%s].", nextPageToken));
    }
}
