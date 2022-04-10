package wip;

import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

@RequiredArgsConstructor
public class Browser<T> {
    private final Resource<Page<T>> resource;
    private final BiPredicate<T, String> matchingFunction;

    private Optional<T> filterPage(Page<T> page, String targetName) {
        Objects.requireNonNull(page, "Page (which is HTTP GET response) is null.");
        if (page.getItems() != null) {
            for (T t : page.getItems()) {
                if (matchingFunction.test(t, targetName)) {
                    return Optional.of(t);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<T> search(String targetName) {
        final Page<T> firstPage = resource.nextPage(null);
        final Optional<T> firstTarget = filterPage(firstPage, targetName);
        if (firstTarget.isPresent()) {
            return firstTarget;
        }
        Page<T> currentPage = firstPage;
        while (currentPage.getNextPageToken() != null) {
            currentPage = resource.nextPage(currentPage.getNextPageToken());
            final Optional<T> currentTarget = filterPage(currentPage, targetName);
            if (currentTarget.isPresent()) {
                return currentTarget;
            }
        }
        return Optional.empty();
    }
}
