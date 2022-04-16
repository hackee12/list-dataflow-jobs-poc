package tdd.dummies;

import tdd.Page;
import tdd.Resource;

import java.util.List;

public class TwoTokensResource implements Resource<Page<String>> {
    @Override
    public Page<String> nextPage(String nextPageToken) {
        if (nextPageToken == null) return new Page<>("firstPage", List.of("a"));
        if ("firstPage".equals(nextPageToken)) return new Page<>("secondPage", List.of("b"));
        if ("secondPage".equals(nextPageToken)) return new Page<>(null, null);
        throw new RuntimeException(String.format("Invalid page token [%s].", nextPageToken));
    }
}
