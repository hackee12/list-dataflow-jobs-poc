package tdd;

import java.util.Optional;

public interface Browser<T> {
    Optional<T> search(String targetName);
}
