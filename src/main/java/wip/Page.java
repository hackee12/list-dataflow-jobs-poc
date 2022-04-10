package wip;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Page<T> {
    private final String nextPageToken;
    private final List<T> items;
}
